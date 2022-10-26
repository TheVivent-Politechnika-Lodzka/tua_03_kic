--
--
----
---- Name: access_level; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create user 'ssbd03admin'@'%' identified by 'cyberpunk2077';
create user 'ssbd03mok'@'%' identified by 'cyberpunk2077';
create user 'ssbd03mop'@'%' identified by 'cyberpunk2077';
create user 'ssbd03auth'@'%' identified by 'cyberpunk2077';

grant all privileges on *.* to 'ssbd03'@'%';
grant all privileges on *.* to 'ssbd03admin'@'%';
--
create TABLE access_level (
    access_level character varying(31) NOT NULL,
    id uuid NOT NULL default(uuid()),
    created_at bigint NOT NULL,
    last_modified bigint NOT NULL,
    version bigint,
    active boolean NOT NULL,
    account_id uuid NOT NULL
);


--
----
---- Name: account; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE account (
    id uuid NOT NULL default(uuid()),
    created_at bigint NOT NULL,
    last_modified bigint NOT NULL,
    version bigint,
    active boolean NOT NULL,
    confirmed boolean NOT NULL,
    login character varying(20) NOT NULL,
    password character varying(128) NOT NULL,
    url character varying(2000) NOT NULL
);

--
----
---- Name: account_confirmation_token; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE account_confirmation_token (
    id uuid NOT NULL default(uuid()),
    expdate bigint NOT NULL,
    token character varying(255) NOT NULL,
    version bigint,
    account_id uuid NOT NULL
);
--
--
--alter table account_confirmation_token OWNER TO ssbd03admin;
--
----
---- Name: account_details; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE account_details (
    email character varying(128) NOT NULL,
    first_name character varying(30) NOT NULL,
    language character varying(16) NOT NULL,
    last_name character varying(30) NOT NULL,
    id uuid NOT NULL default(uuid())
);
--
--
--alter table account_details OWNER TO ssbd03admin;
--
----
---- Name: appointment; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE appointment (
    id uuid NOT NULL default(uuid()),
    created_at bigint NOT NULL,
    last_modified bigint NOT NULL,
    version bigint,
    description text NOT NULL,
    enddate bigint NOT NULL,
    price integer NOT NULL,
    startdate bigint NOT NULL,
    status character varying(255) NOT NULL,
    client_id uuid NOT NULL,
    implant_id uuid,
    specialist_id uuid NOT NULL
);
--
--
--alter table appointment OWNER TO ssbd03admin;
--
----
---- Name: auth_view; Type: VIEW; Schema: public; Owner: ssbd03admin
----
--
create view auth_view as
 select account.login,
    account.password,
    access_level.access_level
   from (account
     join access_level on ((account.id = access_level.account_id)))
  where ((account.active = true) and (account.confirmed = true));
--
--
--alter table auth_view OWNER TO ssbd03admin;
--
----
---- Name: data_administrator; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE data_administrator (
    email character varying(128) NOT NULL,
    phone_number character varying(9) NOT NULL,
    id uuid NOT NULL default(uuid())
);
--
--
--alter table data_administrator OWNER TO ssbd03admin;
--
----
---- Name: data_client; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE data_client (
    pesel character varying(11) NOT NULL,
    phone_number character varying(9) NOT NULL,
    id uuid NOT NULL default(uuid())
);
--
--
--alter table data_client OWNER TO ssbd03admin;
--
----
---- Name: data_specialist; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE data_specialist (
    email character varying(128) NOT NULL,
    phone_number character varying(9) NOT NULL,
    id uuid NOT NULL default(uuid())
);
--
--
--alter table data_specialist OWNER TO ssbd03admin;
--
----
---- Name: implant; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE implant (
    id uuid NOT NULL default(uuid()),
    created_at bigint NOT NULL,
    last_modified bigint NOT NULL,
    version bigint,
    archived boolean NOT NULL,
    description character varying(1000) NOT NULL,
    duration integer NOT NULL,
    image character varying(2000),
    manufacturer character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    price integer NOT NULL,
    popularity uuid
);
--
--
--alter table implant OWNER TO ssbd03admin;
--
----
---- Name: implant_backup_in_appointment; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE implant_backup_in_appointment (
    description character varying(1000) NOT NULL,
    duration integer NOT NULL,
    manufacturer character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    price integer NOT NULL,
    id uuid NOT NULL default(uuid())
);
--
--
--alter table implant_backup_in_appointment OWNER TO ssbd03admin;
--
----
---- Name: implant_popularity_aggregate; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE implant_popularity_aggregate (
    id uuid NOT NULL default(uuid()),
    created_at bigint NOT NULL,
    last_modified bigint NOT NULL,
    version bigint,
    popularity integer DEFAULT 0 NOT NULL
);
--
--
--alter table implant_popularity_aggregate OWNER TO ssbd03admin;
--
----
---- Name: implant_review; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE implant_review (
    id uuid NOT NULL default(uuid()),
    created_at bigint NOT NULL,
    last_modified bigint NOT NULL,
    version bigint,
    rating double precision NOT NULL,
    review character varying(1000) NOT NULL,
    client_id uuid NOT NULL,
    implant_id uuid NOT NULL
--    CONSTRAINT implant_review_rating_check CHECK ((rating <= (5)::double precision))
);
--
--
--alter table implant_review OWNER TO ssbd03admin;
--
----
---- Name: refresh_token; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE refresh_token (
    id uuid NOT NULL default(uuid()),
    expdate bigint NOT NULL,
    token character varying(255) NOT NULL,
    version bigint,
    account_id uuid NOT NULL
);
--
--
--alter table refresh_token OWNER TO ssbd03admin;
--
----
---- Name: reset_password_token; Type: TABLE; Schema: public; Owner: ssbd03admin
----
--
create TABLE reset_password_token (
    id uuid NOT NULL default(uuid()),
    expdate bigint NOT NULL,
    token character varying(255) NOT NULL,
    version bigint,
    account_id uuid NOT NULL
);
--
--
--alter table reset_password_token OWNER TO ssbd03admin;
--
----
---- Name: access_level access_level_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table access_level
    ADD CONSTRAINT access_level_pkey PRIMARY KEY (id);
--
--
----
---- Name: account_confirmation_token account_confirmation_token_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table  account_confirmation_token
    ADD CONSTRAINT account_confirmation_token_pkey PRIMARY KEY (id);
--
--
----
---- Name: account_details account_details_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table account_details
    ADD CONSTRAINT account_details_pkey PRIMARY KEY (id);
--
--
----
---- Name: account_details account_email_unique; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table account_details
    ADD CONSTRAINT account_email_unique UNIQUE (email);
--
--
----
---- Name: account account_login_unique; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table account
    ADD CONSTRAINT account_login_unique UNIQUE (login);
--
--
----
---- Name: account account_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);
--
--
----
---- Name: appointment appointment_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table appointment
    ADD CONSTRAINT appointment_pkey PRIMARY KEY (id);
--
--
----
---- Name: access_level constraint_unique_access_level_for_account; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table access_level
    ADD CONSTRAINT constraint_unique_access_level_for_account UNIQUE (account_id, access_level);
--
--
----
---- Name: data_administrator data_administrator_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table data_administrator
    ADD CONSTRAINT data_administrator_pkey PRIMARY KEY (id);
--
--
----
---- Name: data_client data_client_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table data_client
    ADD CONSTRAINT data_client_pkey PRIMARY KEY (id);
--
--
----
---- Name: data_specialist data_specialist_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table data_specialist
    ADD CONSTRAINT data_specialist_pkey PRIMARY KEY (id);
--
--
----
---- Name: implant_backup_in_appointment implant_backup_in_appointment_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table implant_backup_in_appointment
    ADD CONSTRAINT implant_backup_in_appointment_pkey PRIMARY KEY (id);
--
--
----
---- Name: implant implant_name_unique; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table implant
    ADD CONSTRAINT implant_name_unique UNIQUE (name);
--
--
----
---- Name: implant implant_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table implant
    ADD CONSTRAINT implant_pkey PRIMARY KEY (id);
--
--
----
---- Name: implant_popularity_aggregate implant_popularity_aggregate_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table implant_popularity_aggregate
    ADD CONSTRAINT implant_popularity_aggregate_pkey PRIMARY KEY (id);
--
--
----
---- Name: implant_review implant_review_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table implant_review
    ADD CONSTRAINT implant_review_pkey PRIMARY KEY (id);
--
--
----
---- Name: implant_review one_review_per_client_per_implant; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table implant_review
    ADD CONSTRAINT one_review_per_client_per_implant UNIQUE (client_id, implant_id);
--
--
----
---- Name: refresh_token refresh_token_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table refresh_token
    ADD CONSTRAINT refresh_token_pkey PRIMARY KEY (id);
--
--
----
---- Name: reset_password_token reset_password_token_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table reset_password_token
    ADD CONSTRAINT reset_password_token_pkey PRIMARY KEY (id);
--
--
----
---- Name: reset_password_token uk_3hxjramqybf5xw60qbw9l4xfa; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table reset_password_token
    ADD CONSTRAINT reset_password_token_account_unique UNIQUE (account_id);
--
--
----
---- Name: account_confirmation_token uk_4k22ftr9c823k47k8uqbj6m42; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table account_confirmation_token
    ADD CONSTRAINT account_confirmation_token_account_unique UNIQUE (account_id);
--
--
----
---- Name: account_confirmation_token uk_841pjdfe9wg0m5wj5d0wr0qad; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table account_confirmation_token
    ADD CONSTRAINT account_confirmation_token_token_unique UNIQUE (token);
--
--
----
---- Name: reset_password_token uk_m1q62so82atxntbpu9nrwu9o0; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table reset_password_token
    ADD CONSTRAINT reset_password_token_token_unique UNIQUE (token);
--
--
----
---- Name: refresh_token uk_r4k4edos30bx9neoq81mdvwph; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table refresh_token
    ADD CONSTRAINT refresh_token_unique UNIQUE (token);
--
--
----
---- Name: access_level_account_id; Type: INDEX; Schema: public; Owner: ssbd03admin
----
--
create index access_level_account_id on access_level (account_id);
--
--
----
---- Name: account_id; Type: INDEX; Schema: public; Owner: ssbd03admin
----
--
create index account_id on account_confirmation_token (account_id);
--
--
----
---- Name: account_login; Type: INDEX; Schema: public; Owner: ssbd03admin
----
--
create index account_login on account (login);
--
--
----
---- Name: appointment_client_id; Type: INDEX; Schema: public; Owner: ssbd03admin
----
--
create index appointment_client_id on appointment (client_id);
--
--
----
---- Name: appointment_implant_id; Type: INDEX; Schema: public; Owner: ssbd03admin
----
--
create index appointment_implant_id on appointment (implant_id);
--
--
----
---- Name: appointment_specialist_id; Type: INDEX; Schema: public; Owner: ssbd03admin
----
--
create index appointment_specialist_id on appointment (specialist_id);
--
--
----
---- Name: implant_id; Type: INDEX; Schema: public; Owner: ssbd03admin
----
--
create index implant_id on implant (id);
--
--
----
---- Name: implant_name; Type: INDEX; Schema: public; Owner: ssbd03admin
----
--
create index implant_name on implant (name);
--
--
----
---- Name: review_client_id; Type: INDEX; Schema: public; Owner: ssbd03admin
----
--
create index review_client_id on implant_review (client_id);
--
--
----
---- Name: review_implant_id; Type: INDEX; Schema: public; Owner: ssbd03admin
----
--
create index review_implant_id on implant_review (implant_id);
--
--
----
---- Name: token; Type: INDEX; Schema: public; Owner: ssbd03admin
----
--
create index token on refresh_token (token);
--
--
----
---- Name: appointment fk22f178ro8b2aufalu6sknok2v; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table appointment
    ADD CONSTRAINT FOREIGN KEY (implant_id) REFERENCES implant(id);
--
--
----
---- Name: data_specialist fk2ry059qi2w097t81qpho8r5a7; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table data_specialist
    ADD CONSTRAINT FOREIGN KEY (id) REFERENCES access_level(id);
--
--
----
---- Name: appointment fk3dx0tfrg64ioj41v1gqoysewi; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table appointment
    ADD CONSTRAINT FOREIGN KEY (specialist_id) REFERENCES account(id);
--
--
----
---- Name: data_client fk3jsohmf2ss916i95ikptv85dr; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table data_client
    ADD CONSTRAINT FOREIGN KEY (id) REFERENCES access_level(id);
--
--
----
---- Name: appointment fk3sx0gg7hcewwg1d4x8lrlky2j; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table appointment
    ADD CONSTRAINT FOREIGN KEY (client_id) REFERENCES account(id);
--
--
----
---- Name: implant_backup_in_appointment fk4bs05da8wmi2t8ojsni5ga92g; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table implant_backup_in_appointment
    ADD CONSTRAINT FOREIGN KEY (id) REFERENCES appointment(id);
--
--
----
---- Name: data_administrator fk853iph8s7u1bsdorh3krndn6; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table data_administrator
    ADD CONSTRAINT FOREIGN KEY (id) REFERENCES access_level(id);
--
--
----
---- Name: account_details fk9o8nl2635anpwg0c11383rr67; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table account_details
    ADD CONSTRAINT FOREIGN KEY (id) REFERENCES account(id);
--
--
----
---- Name: account_confirmation_token fkax6m3bhchub4vregkdu7q9wba; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table account_confirmation_token
    ADD CONSTRAINT FOREIGN KEY (account_id) REFERENCES account(id);
--
--
----
---- Name: implant fkd3aoyh3umw0tvaaherc5bo32p; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table implant
    ADD CONSTRAINT FOREIGN KEY (popularity) REFERENCES implant_popularity_aggregate(id);
--
--
----
---- Name: reset_password_token fkf8fxk9jthoxy8c8vrl05r7h40; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table reset_password_token
    ADD CONSTRAINT FOREIGN KEY (account_id) REFERENCES account(id);
--
--
----
---- Name: refresh_token fkiox3wo9jixvp9boxfheq7l99w; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table refresh_token
    ADD CONSTRAINT FOREIGN KEY (account_id) REFERENCES account(id);
--
--
----
---- Name: implant_review fkkmj7f07xsng4glmuowys4j4pj; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table implant_review
    ADD CONSTRAINT FOREIGN KEY (implant_id) REFERENCES implant(id);
--
--
----
---- Name: access_level fkl6ljrvl5w8nhxvrrqt0pu4uou; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table access_level
    ADD CONSTRAINT FOREIGN KEY (account_id) REFERENCES account(id);
--
--
----
---- Name: implant_review fktnosdiu28xgltjb43bdiivd8y; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
----
--
alter table implant_review
    ADD CONSTRAINT FOREIGN KEY (client_id) REFERENCES account(id);
--
--
----
---- Name: TABLE access_level; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select,insert,delete,update on TABLE access_level to 'ssbd03mok'@'%';
grant select on TABLE access_level to ssbd03mop;
--
--
----
---- Name: TABLE account; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select,insert,delete,update on TABLE account to 'ssbd03mok'@'%';
grant select on TABLE account to ssbd03mop;
--
--
----
---- Name: TABLE account_confirmation_token; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select,insert,delete,update on TABLE account_confirmation_token to 'ssbd03mok'@'%';
--
--
----
---- Name: TABLE account_details; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select,insert,delete,update on TABLE account_details to 'ssbd03mok'@'%';
grant select on TABLE account_details to 'ssbd03mop'@'%';
--
--
----
---- Name: TABLE appointment; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select on TABLE appointment to 'ssbd03mok'@'%';
grant select,insert,update on TABLE appointment to 'ssbd03mop'@'%';
--
--
----
---- Name: TABLE auth_view; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select on TABLE auth_view to 'ssbd03auth'@'%';
--
--
----
---- Name: TABLE data_administrator; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select,insert,delete,update on TABLE data_administrator to 'ssbd03mok'@'%';
grant select on TABLE data_administrator to 'ssbd03mop'@'%';
--
--
----
---- Name: TABLE data_client; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select,insert,delete,update on TABLE data_client to 'ssbd03mok'@'%';
grant select on TABLE data_client to 'ssbd03mop'@'%';
--
--
----
---- Name: TABLE data_specialist; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select,insert,delete,update on TABLE data_specialist to 'ssbd03mok'@'%';
grant select on TABLE data_specialist to 'ssbd03mop'@'%';
--
--
----
---- Name: TABLE implant; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select on TABLE implant to 'ssbd03mok'@'%';
grant select,insert,update on TABLE implant to 'ssbd03mop'@'%';
--
--
----
---- Name: TABLE implant_backup_in_appointment; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select,insert,delete on TABLE implant_backup_in_appointment to 'ssbd03mop'@'%';
--
--
----
---- Name: TABLE implant_popularity_aggregate; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select,insert,delete,update on TABLE implant_popularity_aggregate to 'ssbd03mop'@'%';
--
--
----
---- Name: TABLE implant_review; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select on TABLE implant_review to 'ssbd03mok'@'%';
grant select,insert,delete on TABLE implant_review to 'ssbd03mop'@'%';
--
--
----
---- Name: TABLE refresh_token; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select,insert,delete,update on TABLE refresh_token to 'ssbd03mok'@'%';
--
--
----
---- Name: TABLE reset_password_token; Type: ACL; Schema: public; Owner: ssbd03admin
----
--
grant select,insert,delete,update on TABLE reset_password_token to 'ssbd03mok'@'%';
--
--
----
---- PostgreSQL database dump complete
----
--
