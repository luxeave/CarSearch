-- First drop the default value (serial sequence)
ALTER TABLE cars ALTER COLUMN id DROP DEFAULT;

-- Change the column type to bigint
ALTER TABLE cars ALTER COLUMN id TYPE bigint;

-- Create a new bigint sequence
CREATE SEQUENCE cars_id_seq AS bigint;

-- Set the current value of the sequence to the maximum id
SELECT setval('cars_id_seq', COALESCE((SELECT MAX(id) FROM cars), 0));

-- Set the new sequence as the default value
ALTER TABLE cars ALTER COLUMN id SET DEFAULT nextval('cars_id_seq');
