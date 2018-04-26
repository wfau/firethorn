
CREATE TABLE "TAP_SCHEMA"."schemas" ("schema_name" VARCHAR(1000), "description" VARCHAR(MAX), "utype" VARCHAR(MAX), PRIMARY KEY("schema_name"));
CREATE TABLE "TAP_SCHEMA"."tables" ("schema_name" VARCHAR(1000), "table_name" VARCHAR(1000), "table_type" VARCHAR(MAX), "description" VARCHAR(MAX), "utype" VARCHAR(MAX), PRIMARY KEY("table_name"));
CREATE TABLE "TAP_SCHEMA"."columns" ("table_name" VARCHAR(1000), "column_name" VARCHAR(1000), "description" VARCHAR(MAX), "unit" VARCHAR(MAX), "ucd" VARCHAR(MAX), "utype" VARCHAR(MAX), "datatype" VARCHAR(MAX), "size" INTEGER, "principal" INTEGER, "indexed" INTEGER, "std" INTEGER, PRIMARY KEY("table_name","column_name"));
CREATE TABLE "TAP_SCHEMA"."keys" ("key_id" VARCHAR(1000), "from_table" VARCHAR(1000), "target_table" VARCHAR(1000), "description" VARCHAR(MAX), "utype" VARCHAR(MAX), PRIMARY KEY("key_id"));
CREATE TABLE "TAP_SCHEMA"."key_columns" ("key_id" VARCHAR(1000), "from_column" VARCHAR(1000), "target_column" VARCHAR(1000), PRIMARY KEY("key_id"));

INSERT INTO "TAP_SCHEMA"."schemas" VALUES ('TAP_SCHEMA', 'Set of tables listing and describing the schemas, tables and columns published in this TAP service.', NULL);

INSERT INTO "TAP_SCHEMA"."tables" VALUES ('TAP_SCHEMA', 'TAP_SCHEMA.schemas', 'table', 'List of schemas published in this TAP service.', NULL);
INSERT INTO "TAP_SCHEMA"."tables" VALUES ('TAP_SCHEMA', 'TAP_SCHEMA.tables', 'table', 'List of tables published in this TAP service.', NULL);
INSERT INTO "TAP_SCHEMA"."tables" VALUES ('TAP_SCHEMA', 'TAP_SCHEMA.columns', 'table', 'List of columns of all tables listed in TAP_SCHEMA.TABLES and published in this TAP service.', NULL);
INSERT INTO "TAP_SCHEMA"."tables" VALUES ('TAP_SCHEMA', 'TAP_SCHEMA.keys', 'table', 'List all foreign keys but provides just the tables linked by the foreign key. To know which columns of these tables are linked, see in TAP_SCHEMA.key_columns using the key_id.', NULL);
INSERT INTO "TAP_SCHEMA"."tables" VALUES ('TAP_SCHEMA', 'TAP_SCHEMA.key_columns', 'table', 'List all foreign keys but provides just the columns linked by the foreign key. To know the table of these columns, see in TAP_SCHEMA.keys using the key_id.', NULL);

INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.schemas', 'schema_name', 'schema name, possibly qualified', '', '', '', 'VARCHAR(MAX)', -1, 1, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.schemas', 'description', 'brief description of schema', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.schemas', 'utype', 'UTYPE if schema corresponds to a data model', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.tables', 'schema_name', 'the schema name from TAP_SCHEMA.schemas', '', '', '', 'VARCHAR(MAX)', -1, 1, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.tables', 'table_name', 'table name as it should be used in queries', '', '', '', 'VARCHAR(MAX)', -1, 1, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.tables', 'table_type', 'one of: table, view', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.tables', 'description', 'brief description of table', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.tables', 'utype', 'UTYPE if table corresponds to a data model', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.columns', 'table_name', 'table name from TAP_SCHEMA.tables', '', '', '', 'VARCHAR(MAX)', -1, 1, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.columns', 'column_name', 'column name', '', '', '', 'VARCHAR(MAX)', -1, 1, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.columns', 'description', 'brief description of column', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.columns', 'unit', 'unit in VO standard format', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.columns', 'ucd', 'UCD of column if any', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.columns', 'utype', 'UTYPE of column if any', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.columns', 'datatype', 'ADQL datatype as in section 2.5', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.columns', '"size"', 'length of variable length datatypes', '', '', '', 'INTEGER', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.columns', 'arraysize', 'length of variable length datatypes', '', '', '', 'INTEGER', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.columns', 'principal', 'a principal column; 1 means true, 0 means false', '', '', '', 'INTEGER', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.columns', 'indexed', 'an indexed column; 1 means true, 0 means false', '', '', '', 'INTEGER', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.columns', 'std', 'a standard column; 1 means true, 0 means false', '', '', '', 'INTEGER', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.keys', 'key_id', 'unique key identifier', '', '', '', 'VARCHAR(MAX)', -1, 1, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.keys', 'from_table', 'fully qualified table name', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.keys', 'target_table', 'fully qualified table name', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.keys', 'description', 'description of this key', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.keys', 'utype', 'utype of this key', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.key_columns', 'key_id', 'unique key identifier', '', '', '', 'VARCHAR(MAX)', -1, 1, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.key_columns', 'from_column', 'key column name in the from_table', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);
INSERT INTO "TAP_SCHEMA"."columns" VALUES ('TAP_SCHEMA.key_columns', 'target_column', 'key column name in the target_table', '', '', '', 'VARCHAR(MAX)', -1, 0, 0, 1);

