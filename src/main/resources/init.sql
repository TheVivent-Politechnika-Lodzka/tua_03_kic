-- indeksy
CREATE INDEX access_level_account_id ON access_level USING btree(account_id);
CREATE INDEX login_account ON account USING btree(login);

-- uprawnienia dla konta MOK
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE data_client TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE data_doctor TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE data_administrator TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE account TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE account_details TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE access_level TO ssbd03mok;

-- uprawnienia dla konta AUTH
-- zastanowić się, czy nie wystarczy sam SELECT
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE account TO ssbd03auth;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE access_level TO ssbd03auth;