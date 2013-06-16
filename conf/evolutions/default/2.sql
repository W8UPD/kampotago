# -- !Ups
ALTER TABLE contacts ADD COLUMN station_id INTEGER NOT NULL;

# -- !Downs
ALTER TABLE contacts DROP COLUMN station_id;
