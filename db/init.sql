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
INSERT INTO `account` VALUES
                          ('7bf950b9-9e33-4d4e-bfb6-2720ea8a7d5b',1667850714,1667850714,1,1,1,'client','9cba73c31ac15d21512382ce6b21e83f8b9fddd31196ff4f54559a8e29add1e3bc4038c86c9bee7512d0d8ea72ec9480580dc677a9f172b46366ecb5198615cc','https://i.pinimg.com/originals/53/a2/86/53a286fffffddc37fda5e209723039ce.jpg'),
                          ('e29003bd-a723-466a-828f-3c3e9d7e69a6',1667850714,1667850714,1,1,1,'administrator','9cba73c31ac15d21512382ce6b21e83f8b9fddd31196ff4f54559a8e29add1e3bc4038c86c9bee7512d0d8ea72ec9480580dc677a9f172b46366ecb5198615cc','https://i0.wp.com/grynieznane.pl/wp-content/uploads/2020/12/a1-19.jpg?resize=350%2C393&ssl=1'),
                          ('bb64810f-e097-4014-9f9b-a7286a25c676',1667850714,1667850714,1,1,1,'specAdmin','9cba73c31ac15d21512382ce6b21e83f8b9fddd31196ff4f54559a8e29add1e3bc4038c86c9bee7512d0d8ea72ec9480580dc677a9f172b46366ecb5198615cc','https://cdnb.artstation.com/p/assets/images/images/028/593/729/large/katerina-argonskaya-cyberplague-doc-5-774.jpg?1594914723'),
                          ('4c19f644-a7df-410b-85eb-fc13440f80d1',1667850714,1667850714,1,1,1,'spec','9cba73c31ac15d21512382ce6b21e83f8b9fddd31196ff4f54559a8e29add1e3bc4038c86c9bee7512d0d8ea72ec9480580dc677a9f172b46366ecb5198615cc','https://i.pinimg.com/originals/12/da/11/12da11fad537e03c4b40af7d2e12bfce.jpg'),
                          ('1a531d4d-3dc6-4754-b537-ff3cd316f83f',1667850714,1667850714,1,1,1,'clientAdmin','9cba73c31ac15d21512382ce6b21e83f8b9fddd31196ff4f54559a8e29add1e3bc4038c86c9bee7512d0d8ea72ec9480580dc677a9f172b46366ecb5198615cc','https://cdnb.artstation.com/p/assets/images/images/027/342/675/large/ekaterina-ladanuk-sithh-s.jpg?1591263812');

INSERT INTO `account_details` VALUES
                                  ('szuryssbdclient@gmail.com','Sasai','pl','Arasaka','7bf950b9-9e33-4d4e-bfb6-2720ea8a7d5b'),
                                  ('szuryssbd3@gmail.com','Rache','pl','Bartmoss','e29003bd-a723-466a-828f-3c3e9d7e69a6'),
                                  ('szuryssbd2@gmail.com','Yui','en','Arasaka','bb64810f-e097-4014-9f9b-a7286a25c676'),
                                  ('szuryssbdspec@gmail.com','Johnny','pl','Silverhand','4c19f644-a7df-410b-85eb-fc13440f80d1'),
                                  ('szuryssbd@gmail.com','Adam','pl','Smasher','1a531d4d-3dc6-4754-b537-ff3cd316f83f');

INSERT INTO `access_level` VALUES
                               ('CLIENT','23be3625-46ce-4558-a429-216619838cf6',1667850714,1667850714,1,1,'7bf950b9-9e33-4d4e-bfb6-2720ea8a7d5b'),
                               ('SPECIALIST','efd9cf44-6f8d-474e-be54-53546cfce7fb',1667850714,1667850714,1,1,'bb64810f-e097-4014-9f9b-a7286a25c676'),
                               ('ADMINISTRATOR','950a6cb7-5bf8-40e0-b366-6e3fa00a9459',1667850714,1667850714,1,1,'1a531d4d-3dc6-4754-b537-ff3cd316f83f'),
                               ('CLIENT','64334757-8c58-4e0f-bad3-bf9575698fc8',1667850714,1667850714,1,1,'1a531d4d-3dc6-4754-b537-ff3cd316f83f'),
                               ('ADMINISTRATOR','b5692330-5cab-4307-9e7d-d54a2cbf6dce',1667850714,1667850714,1,1,'e29003bd-a723-466a-828f-3c3e9d7e69a6'),
                               ('ADMINISTRATOR','14bc9620-020c-4e57-9ce9-e778353c1933',1667850714,1667850714,1,1,'bb64810f-e097-4014-9f9b-a7286a25c676'),
                               ('SPECIALIST','a80bafa8-0266-49ee-a029-f839ed80c292',1667850714,1667850714,1,1,'4c19f644-a7df-410b-85eb-fc13440f80d1');

INSERT INTO `data_administrator` VALUES
                                     ('klientadmin@kic.agency','556615151','950a6cb7-5bf8-40e0-b366-6e3fa00a9459'),
                                     ('administrator@kic.agency','596468753','b5692330-5cab-4307-9e7d-d54a2cbf6dce'),
                                     ('specadmin@kic.agency','846548753','14bc9620-020c-4e57-9ce9-e778353c1933');

INSERT INTO `data_client` VALUES
                              ('51651615556','956456465','23be3625-46ce-4558-a429-216619838cf6'),
                              ('51611516115','556615151','64334757-8c58-4e0f-bad3-bf9575698fc8');

INSERT INTO `data_specialist` VALUES
                                  ('specadmin@kic.agency','846548753','efd9cf44-6f8d-474e-be54-53546cfce7fb'),
                                  ('specjalista@gmail.com','895111554','a80bafa8-0266-49ee-a029-f839ed80c292');

INSERT INTO `implant_popularity_aggregate` VALUES
                                               ('c6fe07dd-b31a-4989-9ec6-009525343246',1667850714,1667850714,1,0),
                                               ('17d97f61-3abf-413e-b50f-364891b68a37',1667850714,1667850714,1,0),
                                               ('921bf40a-87a0-4f5f-9127-6dcb1123d275',1667850714,1667850714,1,0),
                                               ('7605d5af-5ad6-4813-a0a0-b3bc9830aa10',1667850714,1667850714,1,0),
                                               ('b58203da-5855-43b8-9e49-ddd33b17f983',1667850714,1667850714,1,0);

INSERT INTO `implant` VALUES
                          ('241f6628-3281-4416-aa2e-0a0e569c8144',1667850714,1667850714,1,0,'Ręka wykonana z tytanu z 4 siłownikami klasy AB pozwalającymi wygenerować siłę 1582J czyli 3 razy wiekszą niż MAG-08. Zabieg wykonany na pełnym znieczuleniu, zabieg można wykonać zrówno mając rekę, jak również już z amputowaną ręką.',43199,'https://i.wpimg.pl/c/646x/m.gadzetomania.pl/joihnny-720x405-ec53e56439bdc8de.png','NiceHands SP.Z.O.O.','Ręka z tytanu',750,'17d97f61-3abf-413e-b50f-364891b68a37'),
                          ('63b04182-3db1-42d0-a998-0bd4c59e1715',1667850714,1667850714,1,0,'Pierwszą przełomową innowacją jest nasza funkcja „powiększania według własnego uznania”. Korzystając z unikalnego podwójnego FOV i 2-krotnego zoomu optycznego, użytkownik może szybko ręcznie przełączać podwójne FOV. Większe FOV o ogniskowej 25 mm jest używane do wyszukiwania celu, a mniejsze FOV o ogniskowej 50 mm do identyfikacji celu. Spełnij potrzebę patrzenia daleko, dokładnie i wyraźnie.',1800,'https://api.culture.pl/sites/default/files/styles/440_auto/public/2020-11/cyberpunk2077-outnumbered_but_not_outgunned-rgb-en.jpg?itok=ktGxus53','Nokto S.A.','Noktowizor',100,'7605d5af-5ad6-4813-a0a0-b3bc9830aa10'),
                          ('fc375f57-b2c7-4e13-8abf-1d185e8e300d',1667850714,1667850714,1,0,'Pierwszą przełomową innowacją jest nasza funkcja „powiększania według własnego uznania”. Korzystając z unikalnego podwójnego FOV i 2-krotnego zoomu optycznego, użytkownik może szybko ręcznie przełączać podwójne FOV. Większe FOV o ogniskowej 25 mm jest używane do wyszukiwania celu, a mniejsze FOV o ogniskowej 50 mm do identyfikacji celu. Spełnij potrzebę patrzenia daleko, dokładnie i wyraźnie.',1800,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSHTlU2gK88_xJfgB5Wm0Rk0qYY4bf_-Y-gfw&usqp=CAU','Nokto S.A.','Włosy-kable',250,'c6fe07dd-b31a-4989-9ec6-009525343246'),
                          ('9d2a44fd-0789-4f3f-84aa-a5504dcdd2d4',1667850714,1667850714,1,0,'Implant słuchowy montowany po obu skroniach pozwala usłyszeć na odległość do 300m. Dodatkowo w zestawie naprowadzacz laserowy do precyzyjnego określenia źródła dźwięku. Zabieg nieinwazyjny wykonany w znieczuleniu miejscowym, nie jest wymagana opieka pozabiegowa.',43199,'https://cdn3.whatculture.com/images/2020/06/f9c87f9aba31102e-600x338.jpg','HearMeOUT SP.Z.O.O.','Zestaw słuchowy',820,'921bf40a-87a0-4f5f-9127-6dcb1123d275'),
                          ('5c52cf96-f9b2-430a-86d9-c0fe3c2fe21c',1667850714,1667850714,1,0,'Głownia o wyraźnie zaznaczonej krzywiźnie (o wyraźnym łuku) brzucha ostrza i ostrym, lekko wklęsłym spadku grzbietu w kierunku czubka. Często wklęsły odcinek grzbietu przy czubku jest zaostrzony, lub posiada możliwość naostrzenia. Ze względu na bardzo dobre właściwości tnące przy równocześnie wysokiej zdolności do przebijania profil ten jest powszechnie wykorzystywany w nożach bojowych i roboczych ogólnego przeznaczenia.  Wadą głowni typu bowie jest stosunkowo niska wytrzymałość czubka.',7200,'https://assets.reedpopcdn.com/cyberpunk-2077-ostrza-modliszkowe-jak-zdobyc-1607957173057.jpg/BROK/thumbnail/1600x900/quality/100/cyberpunk-2077-ostrza-modliszkowe-jak-zdobyc-1607957173057.jpg','Factory knifes sharp','Ostrze automatyczne',100,'b58203da-5855-43b8-9e49-ddd33b17f983');

INSERT INTO `appointment` VALUES
                              ('328406d7-6fbf-4dd7-96d1-2fec81f90a36',1667850714,1667850714,1,'8 godzin przed zabiegiem nie można nic spożywać. Na rekonwalenscencje należy przeznaczyć 10 dni. ',1667685114,100,1667677914,'ACCEPTED','1a531d4d-3dc6-4754-b537-ff3cd316f83f','63b04182-3db1-42d0-a998-0bd4c59e1715','4c19f644-a7df-410b-85eb-fc13440f80d1'),
                              ('843b3cbd-4b51-464b-bef9-4c8e95e19a48',1667850714,1667850714,1,'8 godzin przed zabiegiem nie można nic spożywać. Na rekonwalenscencje należy przeznaczyć 10 dni. ',1667771514,100,1667764314,'ACCEPTED','1a531d4d-3dc6-4754-b537-ff3cd316f83f','63b04182-3db1-42d0-a998-0bd4c59e1715','4c19f644-a7df-410b-85eb-fc13440f80d1'),
                              ('8876cb66-eb8f-4c04-9d02-7df98fa69d41',1667850714,1667850714,1,'8 godzin przed zabiegiem nie można nic spożywać. Na rekonwalenscencje należy przeznaczyć 10 dni. ',1667598714,100,1667591514,'ACCEPTED','1a531d4d-3dc6-4754-b537-ff3cd316f83f','63b04182-3db1-42d0-a998-0bd4c59e1715','4c19f644-a7df-410b-85eb-fc13440f80d1'),
                              ('35bb52a6-4653-47a8-bd0f-8fbf0919e9fe',1667850714,1667850714,1,'8 godzin przed zabiegiem nie można nic spożywać. Na rekonwalenscencje należy przeznaczyć 10 dni. ',1667944314,250,1667937114,'PENDING','1a531d4d-3dc6-4754-b537-ff3cd316f83f','fc375f57-b2c7-4e13-8abf-1d185e8e300d','4c19f644-a7df-410b-85eb-fc13440f80d1'),
                              ('5e11dbba-28bc-460f-a78e-cf5d9d62c2ab',1667850714,1667850714,1,'8 godzin przed zabiegiem nie można nic spożywać. Na rekonwalenscencje należy przeznaczyć 10 dni. ',1668030714,250,1668023514,'PENDING','1a531d4d-3dc6-4754-b537-ff3cd316f83f','fc375f57-b2c7-4e13-8abf-1d185e8e300d','4c19f644-a7df-410b-85eb-fc13440f80d1'),
                              ('9649d31d-f08e-43e6-8b80-fa4e94f8e572',1667850714,1667850714,1,'8 godzin przed zabiegiem nie można nic spożywać. Na rekonwalenscencje należy przeznaczyć 10 dni. ',1668117114,250,1668109914,'PENDING','1a531d4d-3dc6-4754-b537-ff3cd316f83f','fc375f57-b2c7-4e13-8abf-1d185e8e300d','4c19f644-a7df-410b-85eb-fc13440f80d1');


INSERT INTO `implant_backup_in_appointment` VALUES
                                                ('Pierwszą przełomową innowacją jest nasza funkcja „powiększania według własnego uznania”. Korzystając z unikalnego podwójnego FOV i 2-krotnego zoomu optycznego, użytkownik może szybko ręcznie przełączać podwójne FOV. Większe FOV o ogniskowej 25 mm jest używane do wyszukiwania celu, a mniejsze FOV o ogniskowej 50 mm do identyfikacji celu. Spełnij potrzebę patrzenia daleko, dokładnie i wyraźnie.',1800,'Nokto S.A.','Noktowizor',100,'328406d7-6fbf-4dd7-96d1-2fec81f90a36'),
                                                ('Pierwszą przełomową innowacją jest nasza funkcja „powiększania według własnego uznania”. Korzystając z unikalnego podwójnego FOV i 2-krotnego zoomu optycznego, użytkownik może szybko ręcznie przełączać podwójne FOV. Większe FOV o ogniskowej 25 mm jest używane do wyszukiwania celu, a mniejsze FOV o ogniskowej 50 mm do identyfikacji celu. Spełnij potrzebę patrzenia daleko, dokładnie i wyraźnie.',1800,'Nokto S.A.','Noktowizor',100,'843b3cbd-4b51-464b-bef9-4c8e95e19a48'),
                                                ('Pierwszą przełomową innowacją jest nasza funkcja „powiększania według własnego uznania”. Korzystając z unikalnego podwójnego FOV i 2-krotnego zoomu optycznego, użytkownik może szybko ręcznie przełączać podwójne FOV. Większe FOV o ogniskowej 25 mm jest używane do wyszukiwania celu, a mniejsze FOV o ogniskowej 50 mm do identyfikacji celu. Spełnij potrzebę patrzenia daleko, dokładnie i wyraźnie.',1800,'Nokto S.A.','Noktowizor',100,'8876cb66-eb8f-4c04-9d02-7df98fa69d41'),
                                                ('Pierwszą przełomową innowacją jest nasza funkcja „powiększania według własnego uznania”. Korzystając z unikalnego podwójnego FOV i 2-krotnego zoomu optycznego, użytkownik może szybko ręcznie przełączać podwójne FOV. Większe FOV o ogniskowej 25 mm jest używane do wyszukiwania celu, a mniejsze FOV o ogniskowej 50 mm do identyfikacji celu. Spełnij potrzebę patrzenia daleko, dokładnie i wyraźnie.',1800,'Nokto S.A.','Włosy-kable',250,'35bb52a6-4653-47a8-bd0f-8fbf0919e9fe'),
                                                ('Pierwszą przełomową innowacją jest nasza funkcja „powiększania według własnego uznania”. Korzystając z unikalnego podwójnego FOV i 2-krotnego zoomu optycznego, użytkownik może szybko ręcznie przełączać podwójne FOV. Większe FOV o ogniskowej 25 mm jest używane do wyszukiwania celu, a mniejsze FOV o ogniskowej 50 mm do identyfikacji celu. Spełnij potrzebę patrzenia daleko, dokładnie i wyraźnie.',1800,'Nokto S.A.','Włosy-kable',250,'5e11dbba-28bc-460f-a78e-cf5d9d62c2ab'),
                                                ('Pierwszą przełomową innowacją jest nasza funkcja „powiększania według własnego uznania”. Korzystając z unikalnego podwójnego FOV i 2-krotnego zoomu optycznego, użytkownik może szybko ręcznie przełączać podwójne FOV. Większe FOV o ogniskowej 25 mm jest używane do wyszukiwania celu, a mniejsze FOV o ogniskowej 50 mm do identyfikacji celu. Spełnij potrzebę patrzenia daleko, dokładnie i wyraźnie.',1800,'Nokto S.A.','Włosy-kable',250,'9649d31d-f08e-43e6-8b80-fa4e94f8e572');

INSERT INTO `implant_review` VALUES
    ('3922c25f-c642-40ef-9d1a-2cb35ff023b5',1667850714,1667850714,1,5,'Wszystko sprawnie, Pani wykonująca zabieg bardzo miła, przyjemna. Polecam','7bf950b9-9e33-4d4e-bfb6-2720ea8a7d5b','5c52cf96-f9b2-430a-86d9-c0fe3c2fe21c');

INSERT INTO `refresh_token` VALUES
    ('57a303ee-d0b5-405a-a010-daa837666a43',1668455557,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbmlzdHJhdG9yIiwiZXhwIjoxNjY4NDU1NTU3fQ.wnA_fctbKrgMs-ETq9ftBwutp3YfDYLU2C2Ro6glaBw',1,'e29003bd-a723-466a-828f-3c3e9d7e69a6');
