.PHONY: install install-java install-maven install-postgres populate-db build deploy clean

# PostgreSQL configuration
DB_NAME = car_search
DB_USER = postgres
DB_PASSWORD = postgres
DB_HOST = localhost
DB_PORT = 5432

install-java:
	@echo "Installing Java 23..."
	@sudo apt-get update
	@sudo apt-get install -y wget gpg
	@wget https://download.oracle.com/java/23/latest/jdk-23_linux-x64_bin.deb
	@sudo dpkg -i jdk-23_linux-x64_bin.deb
	@sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-23.0.1-oracle-x64/bin/java 1
	@sudo update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/jdk-23.0.1-oracle-x64/bin/javac 1
	@rm jdk-23_linux-x64_bin.deb
	@echo "Java 23 installed successfully"
	@java -version

install-maven:
	@echo "Installing Maven..."
	@sudo apt-get update
	@sudo apt-get install -y maven
	@echo "Maven installed successfully"
	@mvn -version

install-dev-tools: install-java install-maven
	@echo "Development tools installation completed"

install-postgres:
	@echo "Checking if PostgreSQL is installed..."
	@if ! command -v psql >/dev/null; then \
		echo "Installing PostgreSQL..."; \
		sudo apt-get update && \
		sudo apt-get install -y postgresql postgresql-contrib; \
	fi
	@echo "Starting PostgreSQL service..."
	@sudo service postgresql start
	@echo "Creating database if it doesn't exist..."
	@psql -d postgres -c "SELECT 1 FROM pg_database WHERE datname = '$(DB_NAME)'" | grep -q 1 || \
		createdb $(DB_NAME)
	@echo "Creating user if it doesn't exist..."
	@psql -d postgres -c "SELECT 1 FROM pg_user WHERE usename = '$(DB_USER)'" | grep -q 1 || \
		createuser --createdb $(DB_USER)
	@echo "Setting user password..."
	@psql -d postgres -c "ALTER USER $(DB_USER) WITH PASSWORD '$(DB_PASSWORD)'"
	@echo "Granting privileges..."
	@psql -d postgres -c "GRANT ALL PRIVILEGES ON DATABASE $(DB_NAME) TO $(DB_USER)"
	@echo "PostgreSQL setup completed"

# Main install target that installs everything
install: install-dev-tools install-postgres
	@echo "All components installed successfully"

populate-db:
	@echo "Populating database with dummy data..."
	@PGPASSWORD=$(DB_PASSWORD) psql -h $(DB_HOST) -p $(DB_PORT) -U $(DB_USER) -d $(DB_NAME) -c "\
		CREATE TABLE IF NOT EXISTS cars ( \
			id SERIAL PRIMARY KEY, \
			model VARCHAR(100) NOT NULL, \
			length_cm INTEGER NOT NULL, \
			weight_kg INTEGER NOT NULL, \
			max_velocity_km_h INTEGER NOT NULL, \
			color VARCHAR(50) NOT NULL, \
			created_at TIMESTAMP NOT NULL, \
			updated_at TIMESTAMP NOT NULL \
		); \
		INSERT INTO cars (model, length_cm, weight_kg, max_velocity_km_h, color, created_at, updated_at) VALUES \
		('Tesla Model 3', 469, 1800, 261, 'red', NOW(), NOW()), \
		('Toyota Camry', 485, 1600, 180, 'blue', NOW(), NOW()), \
		('BMW M3', 475, 1730, 250, 'black', NOW(), NOW()), \
		('Porsche 911', 450, 1500, 310, 'white', NOW(), NOW()), \
		('Audi RS6', 495, 2075, 305, 'gray', NOW(), NOW()) \
		ON CONFLICT DO NOTHING;"

build:
	@echo "Building application..."
	@mvn clean install

deploy:
	@echo "Starting application..."
	@mvn spring-boot:run

clean:
	@echo "Cleaning up..."
	@find . -type d -name "__pycache__" -exec rm -r {} + 2>/dev/null || true
	@find . -type f -name "*.pyc" -delete
	@find . -type f -name "*.pyo" -delete
	@find . -type f -name "*.pyd" -delete

# Default target
all: install populate-db build deploy

# Help target
help:
	@echo "Available targets:"
	@echo "  install     - Install and configure all components"
	@echo "  install-dev-tools - Install Java and Maven"
	@echo "  install-postgres - Install and configure PostgreSQL"
	@echo "  populate-db - Populate database with dummy data"
	@echo "  build      - Build Java application with Maven"
	@echo "  deploy     - Start the Spring Boot application"
	@echo "  clean      - Clean up build artifacts"
	@echo "  all        - Run all targets in sequence"
