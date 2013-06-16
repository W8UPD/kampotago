# -- !Ups
ALTER TABLE contacts ADD COLUMN contacted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

# -- !Downs
ALTER TABLE contacts DROP COLUMN contacted_at;
