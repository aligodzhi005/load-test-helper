CREATE SCHEMA IF NOT EXISTS LOAD_TEST_HELPER;

SET SCHEMA LOAD_TEST_HELPER;

CREATE TABLE IF NOT EXISTS "table_group"
(
    id INTEGER NOT NULL,
    certificate VARCHAR(255),
    domain VARCHAR(255),
    duration INTEGER,
    master_run BOOLEAN,
    profile VARCHAR(255) NOT NULL,
    server VARCHAR(255),
    server_param VARCHAR(255),
    test_param VARCHAR(255),
    scenario_id VARCHAR(255),
    PRIMARY KEY(id)
)
