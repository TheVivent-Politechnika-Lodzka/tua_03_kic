CREATE OR REPLACE VIEW auth_view AS SELECT login, password, access_level FROM account JOIN access_level ON account.id = access_level.account_id WHERE account.active = true AND account.confirmed = true;

-- uprawnienia dla konta MOK
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE data_client TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE data_specialist TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE data_administrator TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE account TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE account_details TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE access_level TO ssbd03mok;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE reset_password_token TO ssbd03mok;

GRANT SELECT ON TABLE appointment TO ssbd03mok;
GRANT SELECT ON TABLE implant TO ssbd03mok;
GRANT SELECT ON TABLE implant_review TO ssbd03mok;
GRANT SELECT,INSERT ON TABLE active_account_token TO ssbd03mok;

-- uprawnienia dla konta MOP
GRANT SELECT ON TABLE data_client TO ssbd03mop;
GRANT SELECT ON TABLE data_specialist TO ssbd03mop;
GRANT SELECT ON TABLE data_administrator TO ssbd03mop;
GRANT SELECT ON TABLE account TO ssbd03mop;
GRANT SELECT ON TABLE account_details TO ssbd03mop;
GRANT SELECT ON TABLE access_level TO ssbd03mop;

GRANT SELECT,INSERT,UPDATE ON TABLE appointment TO ssbd03mop;
GRANT SELECT,INSERT,UPDATE ON TABLE implant TO ssbd03mop;
GRANT SELECT,INSERT,DELETE ON TABLE implant_review TO ssbd03mop;

-- uprawnienia dla konta AUTH
GRANT SELECT ON TABLE auth_view TO ssbd03auth;