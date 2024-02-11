INSERT INTO users (name, login, password, telegram_id, created_timestamp, updated_timestamp)
VALUES ('Nikolai', 'nikolai', '_', 0, now(), now()),
       ('Ekaterina', 'ekaterina', '_', 1, now(), now());

INSERT INTO account (name, group_name, type, currency, owner_id, created_timestamp, updated_timestamp)
VALUES ('Nikolai Euro Cahs', 'Nikolai', 'CASH', 'EUR', 1, now(), now()),
       ('Nikolai Euro Card', 'Nikolai', 'CARD', 'EUR', 1, now(), now()),
       ('Nikolai Euro Bank Account', 'Nikolai', 'BANK_ACCOUNT', 'EUR', 1, now(), now()),
       ('Ekaterina Euro Cahs', 'Ekaterina', 'CASH', 'EUR', 2, now(), now()),
       ('Ekaterina Euro Card', 'Ekaterina', 'CARD', 'EUR', 2, now(), now()),
       ('Ekaterina Euro Bank Account', 'Ekaterina', 'BANK_ACCOUNT', 'EUR', 2, now(), now());

INSERT INTO account_payment_type(account_type, payment_method)
VALUES ('CASH', 'BANKNOTE'),
       ('CARD', 'CARD');

INSERT INTO user_default_account (user_id, type, account_id)
VALUES (1, 'CASH', 1),
       (1, 'CARD', 2),
       (2, 'CASH', 4),
       (2, 'CARD', 5);