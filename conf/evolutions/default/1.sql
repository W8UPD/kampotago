# -- !Ups
CREATE TABLE contacts (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  callsign VARCHAR(20) NOT NULL,
  exchange VARCHAR(20) NOT NULL,
  band VARCHAR(5) NOT NULL,
  mode VARCHAR(10) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  UNIQUE (callsign, band, mode, active)
);

# -- !Downs
DROP TABLE contacts;
