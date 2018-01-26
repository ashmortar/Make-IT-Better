SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS businesses (
    id int PRIMARY KEY auto_increment,
    type VARCHAR,
    name VARCHAR,
    phone VARCHAR,
    website VARCHAR,
    hours VARCHAR,
    specialty VARCHAR,
    glutenFree BOOLEAN,
    food VARCHAR,
    atmosphere VARCHAR,
    hasTaps BOOLEAN,
    hasCocktails BOOLEAN,
    fairTrade BOOLEAN,
    needReservation BOOLEAN,
    hasBar BOOLEAN
);

CREATE TABLE IF NOT EXISTS causes (
    id int PRIMARY KEY auto_increment,
    name VARCHAR,
    type VARCHAR,
    description VARCHAR,
    phone VARCHAR
);

CREATE TABLE IF NOT EXISTS causes_businesses (
    id int PRIMARY KEY auto_increment,
    businessId INTEGER,
    causeId INTEGER
);

CREATE TABLE IF NOT EXISTS addresses (
    id int PRIMARY KEY auto_increment,
    street VARCHAR,
    city VARCHAR,
    state VARCHAR,
    zip VARCHAR
);

CREATE TABLE IF NOT EXISTS addresses_businesses (
    id int PRIMARY KEY auto_increment,
    businessId INTEGER,
    addressId INTEGER
);

CREATE TABLE IF NOT EXISTS addresses_causes (
    id int PRIMARY KEY auto_increment,
    addressId INTEGER,
    causeId INTEGER
);

CREATE TABLE IF NOT EXISTS tours (
    id int PRIMARY KEY auto_increment,
    startPoint INTEGER,
    endPoint INTEGER,
    waypoints VARCHAR,
    directions VARCHAR
);