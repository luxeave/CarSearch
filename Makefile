.PHONY: install install-java install-maven build deploy clean

# Java configuration
JAVA_VERSION = 23

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

# Main install target that installs everything
install: install-dev-tools
	@echo "All components installed successfully"

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
all: install build deploy

# Help target
help:
	@echo "Available targets:"
	@echo "  install     - Install and configure all components"
	@echo "  install-dev-tools - Install Java and Maven"
	@echo "  build      - Build Java application with Maven"
	@echo "  deploy     - Start the Spring Boot application"
	@echo "  clean      - Clean up build artifacts"
	@echo "  all        - Run all targets in sequence"
