create or replace view auth_view as select login, password, access_level from account join access_level on account.id = access_level.account_id where account.active = true and account.confirmed = true;

-- uprawnienia dla konta MOK
grant select,insert,delete,update on TABLE data_client to ssbd03mok;
grant select,insert,delete,update on TABLE data_specialist to ssbd03mok;
grant select,insert,delete,update on TABLE data_administrator to ssbd03mok;
grant select,insert,delete,update on TABLE account to ssbd03mok;
grant select,insert,delete,update on TABLE account_details to ssbd03mok;
grant select,insert,delete,update on TABLE access_level to ssbd03mok;
grant select,insert,delete,update on TABLE reset_password_token to ssbd03mok;
grant select,insert,delete,update on TABLE account_confirmation_token to ssbd03mok;
grant select,insert,delete,update on TABLE refresh_token to ssbd03mok;

grant select on TABLE appointment to ssbd03mok;
grant select on TABLE implant to ssbd03mok;
grant select on TABLE implant_review to ssbd03mok;

-- uprawnienia dla konta MOP
grant select on TABLE data_client to ssbd03mop;
grant select on TABLE data_specialist to ssbd03mop;
grant select on TABLE data_administrator to ssbd03mop;
grant select on TABLE account to ssbd03mop;
grant select on TABLE account_details to ssbd03mop;
grant select on TABLE access_level to ssbd03mop;

grant select,insert,update on TABLE appointment to ssbd03mop;
grant select,insert,update on TABLE implant to ssbd03mop;
grant select,insert,delete on TABLE implant_review to ssbd03mop;
grant select,insert,delete on TABLE implant_backup_in_appointment to ssbd03mop;
grant select,insert,delete on TABLE implant_popularity_aggregate to ssbd03mop;

-- uprawnienia dla konta AUTH
grant select on TABLE auth_view to ssbd03auth;