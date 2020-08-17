-- Table: jwt_schema."user"

-- DROP TABLE jwt_schema."user";

CREATE TABLE jwt_schema."user"
(
    id bigint NOT NULL,
    external_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    first_name character varying(255) COLLATE pg_catalog."default",
    middle_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    full_name character varying(255) COLLATE pg_catalog."default",
    address character varying(255) COLLATE pg_catalog."default",
    email_address character varying(255) COLLATE pg_catalog."default",
    contact_number character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    created_at timestamp without time zone,
    modified_at timestamp without time zone,
    modified_by character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT user_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE jwt_schema."user"
    OWNER to postgres;