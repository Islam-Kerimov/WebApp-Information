CREATE TABLE IF NOT EXISTS task_properties
(
    id              SERIAL PRIMARY KEY,
    cron_expression VARCHAR(32) NOT NULL,
    type_object     VARCHAR(32) NOT NULL,
    options         VARCHAR(128)
);

INSERT INTO task_properties (cron_expression, type_object, options)
VALUES ('*/5 * * * * *', 'WEATHER', '498817'),
       ('*/5 * * * * *', 'WEATHER', '558418'),
       ('*/5 * * * * *', 'WEATHER', '551487'),
       ('*/5 * * * * *', 'WEATHER', '524901'),
       ('*/5 * * * * *', 'CURRENCY', null),
       ('*/5 * * * * *', 'NEWS', 'https://www.vedomosti.ru/rss/news'),
       ('*/5 * * * * *', 'NEWS', 'https://news.ru/rss/'),
       ('*/5 * * * * *', 'NEWS', 'https://ria.ru/export/rss2/archive/index.xml'),
       ('*/5 * * * * *', 'NEWS', 'https://news.rambler.ru/rss/world/');
