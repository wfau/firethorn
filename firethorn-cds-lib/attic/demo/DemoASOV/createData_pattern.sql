
CREATE TABLE data ( id int8 PRIMARY KEY, name varchar(100), ra float8, dec float8, coord spoint, type varchar(32) );

COPY data (id,name,ra,dec,type) FROM '<DUMPFILE>' WITH CSV HEADER;


-- Note: The following lines work only if PgSphere is installed

UPDATE data SET coord = spoint(radians(ra), radians(dec));

CREATE INDEX coord_idx ON data USING gist(coord);

