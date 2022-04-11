-- indeksy
CREATE INDEX access_level_account_id ON access_level USING btree(account_id);
CREATE INDEX review_client_id ON review USING btree(client_id);
CREATE INDEX review_implant_id ON review USING btree(implant_id);
CREATE INDEX appointment_client_id ON appointment USING btree(client_id);
CREATE INDEX appointment_specialist_id ON appointment USING btree(specialist_id);
CREATE INDEX appointment_implant_id ON appointment USING btree(implant_id);
CREATE INDEX account_login ON account USING btree(login);
CREATE OR REPLACE VIEW auth_view AS SELECT login, password, access_level FROM account JOIN access_level ON account.id = access_level.account_id WHERE account.active = true AND account.confirmed = true

-- wygenerowanie użytkownika administratora
-- login: administrator
-- hasło: Password123!
INSERT INTO account (active, confirmed, login, password, version, id) VALUES (true, true, 'administrator', '9cba73c31ac15d21512382ce6b21e83f8b9fddd31196ff4f54559a8e29add1e3bc4038c86c9bee7512d0d8ea72ec9480580dc677a9f172b46366ecb5198615cc', 0, '00000000-0000-0000-0000-000000000000');
INSERT INTO account_details (email, firstName, surname, id) VALUES ('admin@kic.pl', 'admin', 'admin', '00000000-0000-0000-0000-000000000000')
INSERT INTO access_level (account_id, active, version, access_level, id)  values ('00000000-0000-0000-0000-000000000000', true, 0, 'ADMINISTRATOR', '00000000-0000-0000-0000-000000000000');
INSERT INTO data_administrator (phone_number, id) VALUES ('123-456-789', '00000000-0000-0000-0000-000000000000');
INSERT INTO access_level (account_id, active, version, access_level, id)  values ('00000000-0000-0000-0000-000000000000', true, 0, 'CLIENT', '00000000-0000-0000-0000-000000000001');
INSERT INTO data_client (pesel, id) VALUES ('12345678901', '00000000-0000-0000-0000-000000000001');
INSERT INTO access_level (account_id, active, version, access_level, id)  values ('00000000-0000-0000-0000-000000000000', true, 0, 'SPECIALIST', '00000000-0000-0000-0000-000000000002');
INSERT INTO data_specialist (phone_number, id) VALUES ('123-456-789', '00000000-0000-0000-0000-000000000002');

-- uprawnienia dla konta MOK
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE data_client TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE data_specialist TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE data_administrator TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE account TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE account_details TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE access_level TO ssbd03mok;

GRANT SELECT ON TABLE appointment TO ssbd03mok;
GRANT SELECT ON TABLE implant TO ssbd03mok;
GRANT SELECT ON TABLE review TO ssbd03mok;

-- uprawnienia dla konta MOP
GRANT SELECT ON TABLE data_client TO ssbd03mop;
GRANT SELECT ON TABLE data_specialist TO ssbd03mop;
GRANT SELECT ON TABLE data_administrator TO ssbd03mop;
GRANT SELECT ON TABLE account TO ssbd03mop;
GRANT SELECT ON TABLE account_details TO ssbd03mop;
GRANT SELECT ON TABLE access_level TO ssbd03mop;

GRANT SELECT,INSERT,UPDATE ON TABLE appointment TO ssbd03mop;
GRANT SELECT,INSERT,UPDATE ON TABLE implant TO ssbd03mop;
GRANT SELECT,INSERT,DELETE ON TABLE review TO ssbd03mop;

-- uprawnienia dla konta AUTH
GRANT SELECT ON TABLE auth_view TO ssbd03auth;