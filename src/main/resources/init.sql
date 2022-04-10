-- indeksy
CREATE INDEX access_level_account_id ON access_level USING btree(account_id);
CREATE INDEX login_account ON account USING btree(login);
CREATE OR REPLACE VIEW auth_view AS SELECT login, password, access_level FROM account JOIN access_level ON account.id = access_level.account_id WHERE account.active = true AND account.confirmed = true

-- uprawnienia dla konta MOK
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE data_client TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE data_doctor TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE data_administrator TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE account TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE account_details TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE access_level TO ssbd03mok;

-- uprawnienia dla konta MOP
GRANT SELECT ON TABLE data_client TO ssbd03mop;
GRANT SELECT ON TABLE data_doctor TO ssbd03mop;
GRANT SELECT ON TABLE data_administrator TO ssbd03mop;
GRANT SELECT ON TABLE account TO ssbd03mop;
GRANT SELECT ON TABLE account_details TO ssbd03mop;
GRANT SELECT ON TABLE access_level TO ssbd03mop;

-- uprawnienia dla konta AUTH
GRANT SELECT ON TABLE auth_view TO ssbd03auth;