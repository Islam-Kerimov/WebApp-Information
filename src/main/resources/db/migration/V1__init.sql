CREATE TABLE IF NOT EXISTS currency_type
(
    currency_id SERIAL PRIMARY KEY,
    num_code    BPCHAR(3) UNIQUE   NOT NULL,
    char_code   BPCHAR(3) UNIQUE   NOT NULL,
    log_nominal INT2               NOT NULL,
    name        VARCHAR(64) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS currency_rate
(
    id          SERIAL PRIMARY KEY,
    currency_id INTEGER NOT NULL,
    value       NUMERIC(6, 4),
    date        DATE    NOT NULL,
    UNIQUE (currency_id, date),
    FOREIGN KEY (currency_id) REFERENCES currency_type (currency_id)
);

CREATE TABLE IF NOT EXISTS city_name
(
    city_id   INTEGER PRIMARY KEY,
    name      VARCHAR(32) UNIQUE   NOT NULL,
    longitude NUMERIC(6, 4) UNIQUE NOT NULL,
    latitude  NUMERIC(6, 4) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS weather
(
    id              SERIAL PRIMARY KEY,
    city_id         INTEGER                  NOT NULL,
    name            VARCHAR(32)              NOT NULL,
    description     VARCHAR(32)              NOT NULL,
    temperature     NUMERIC(4, 2)            NOT NULL,
    temperature_min NUMERIC(4, 2)            NOT NULL,
    temperature_max NUMERIC(4, 2)            NOT NULL,
    humidity        INT2                     NOT NULL,
    pressure        INT2                     NOT NULL,
    visibility      INTEGER                  NOT NULL,
    wind_speed      NUMERIC(5, 2)            NOT NULL,
    cloudiness      INT2                     NOT NULL,
    date_create     TIMESTAMP WITH TIME ZONE NOT NULL,
    timezone        INT2                     NOT NULL,
    UNIQUE (city_id, date_create, timezone),
    FOREIGN KEY (city_id) REFERENCES city_name (city_id)
);

CREATE UNIQUE INDEX IF NOT EXISTS index_weather_city_id_date_timezone_index ON weather (city_id, date_create, timezone);

CREATE TABLE IF NOT EXISTS news_source
(
    source_id SERIAL PRIMARY KEY,
    name      VARCHAR(64) UNIQUE NOT NULL,
    url       VARCHAR UNIQUE     NOT NULL
);

CREATE TABLE IF NOT EXISTS news_body
(
    id          SERIAL PRIMARY KEY,
    source_id   INTEGER                  NOT NULL,
    title       VARCHAR(128)             NOT NULL,
    category    VARCHAR(64)              NOT NULL,
    description TEXT,
    url_news    VARCHAR                  NOT NULL,
    public_date TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (title, description, url_news, public_date),
    FOREIGN KEY (source_id) REFERENCES news_source (source_id)
);