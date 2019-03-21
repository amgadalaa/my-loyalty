
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 16394)
-- Name: user_management; Type: SCHEMA; Schema: -; Owner: postgres
--
DROP SCHEMA IF EXISTS user_management cascade;

CREATE SCHEMA user_management;


ALTER SCHEMA user_management OWNER TO postgres;

--
-- TOC entry 1 (class 3079 OID 12924)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2830 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 201 (class 1259 OID 16471)
-- Name: cpnt; Type: TABLE; Schema: user_management; Owner: postgres
--

CREATE TABLE user_management.cpnt (
    cpnt_id integer NOT NULL,
    full_address character varying(500) NOT NULL,
    name character varying(500) NOT NULL,
    is_enabled boolean DEFAULT true NOT NULL,
    is_deleted boolean DEFAULT false NOT NULL,
    owner_user integer NOT NULL
);


ALTER TABLE user_management.cpnt OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16469)
-- Name: cpnt_cpnt_id_seq; Type: SEQUENCE; Schema: user_management; Owner: postgres
--

CREATE SEQUENCE user_management.cpnt_cpnt_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE user_management.cpnt_cpnt_id_seq OWNER TO postgres;

--
-- TOC entry 2831 (class 0 OID 0)
-- Dependencies: 200
-- Name: cpnt_cpnt_id_seq; Type: SEQUENCE OWNED BY; Schema: user_management; Owner: postgres
--

ALTER SEQUENCE user_management.cpnt_cpnt_id_seq OWNED BY user_management.cpnt.cpnt_id;


--
-- TOC entry 199 (class 1259 OID 16459)
-- Name: lk_user_type; Type: TABLE; Schema: user_management; Owner: postgres
--

CREATE TABLE user_management.lk_user_type (
    user_type_id smallint NOT NULL,
    description character varying(100) NOT NULL
);


ALTER TABLE user_management.lk_user_type OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 16451)
-- Name: system_users; Type: TABLE; Schema: user_management; Owner: postgres
--

CREATE TABLE user_management.system_users (
    user_id integer NOT NULL,
    username character varying(100) NOT NULL,
    password character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    full_name character varying(100) NOT NULL,
    is_enabled boolean DEFAULT true NOT NULL,
    is_deleted boolean DEFAULT false NOT NULL,
    user_type smallint NOT NULL
);


ALTER TABLE user_management.system_users OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 16449)
-- Name: system_users_user_id_seq; Type: SEQUENCE; Schema: user_management; Owner: postgres
--

CREATE SEQUENCE user_management.system_users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE user_management.system_users_user_id_seq OWNER TO postgres;

--
-- TOC entry 2832 (class 0 OID 0)
-- Dependencies: 197
-- Name: system_users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: user_management; Owner: postgres
--

ALTER SEQUENCE user_management.system_users_user_id_seq OWNED BY user_management.system_users.user_id;


--
-- TOC entry 2685 (class 2604 OID 16474)
-- Name: cpnt cpnt_id; Type: DEFAULT; Schema: user_management; Owner: postgres
--

ALTER TABLE ONLY user_management.cpnt ALTER COLUMN cpnt_id SET DEFAULT nextval('user_management.cpnt_cpnt_id_seq'::regclass);


--
-- TOC entry 2682 (class 2604 OID 16454)
-- Name: system_users user_id; Type: DEFAULT; Schema: user_management; Owner: postgres
--

ALTER TABLE ONLY user_management.system_users ALTER COLUMN user_id SET DEFAULT nextval('user_management.system_users_user_id_seq'::regclass);

--
-- TOC entry 2833 (class 0 OID 0)
-- Dependencies: 200
-- Name: cpnt_cpnt_id_seq; Type: SEQUENCE SET; Schema: user_management; Owner: postgres
--

SELECT pg_catalog.setval('user_management.cpnt_cpnt_id_seq', 16, true);


--
-- TOC entry 2834 (class 0 OID 0)
-- Dependencies: 197
-- Name: system_users_user_id_seq; Type: SEQUENCE SET; Schema: user_management; Owner: postgres
--

SELECT pg_catalog.setval('user_management.system_users_user_id_seq', 13, true);


--
-- TOC entry 2689 (class 2606 OID 16458)
-- Name: system_users SYSTEM_USERS_pkey; Type: CONSTRAINT; Schema: user_management; Owner: postgres
--

ALTER TABLE ONLY user_management.system_users
    ADD CONSTRAINT "SYSTEM_USERS_pkey" PRIMARY KEY (user_id);


--
-- TOC entry 2693 (class 2606 OID 16481)
-- Name: cpnt cpnt_pkey; Type: CONSTRAINT; Schema: user_management; Owner: postgres
--

ALTER TABLE ONLY user_management.cpnt
    ADD CONSTRAINT cpnt_pkey PRIMARY KEY (cpnt_id);


--
-- TOC entry 2691 (class 2606 OID 16463)
-- Name: lk_user_type lk_user_type_pkey; Type: CONSTRAINT; Schema: user_management; Owner: postgres
--

ALTER TABLE ONLY user_management.lk_user_type
    ADD CONSTRAINT lk_user_type_pkey PRIMARY KEY (user_type_id);


--
-- TOC entry 2695 (class 2606 OID 16482)
-- Name: cpnt cpnt_owner_user; Type: FK CONSTRAINT; Schema: user_management; Owner: postgres
--

ALTER TABLE ONLY user_management.cpnt
    ADD CONSTRAINT cpnt_owner_user FOREIGN KEY (owner_user) REFERENCES user_management.system_users(user_id);


--
-- TOC entry 2694 (class 2606 OID 16464)
-- Name: system_users users_lk_users; Type: FK CONSTRAINT; Schema: user_management; Owner: postgres
--

ALTER TABLE ONLY user_management.system_users
    ADD CONSTRAINT users_lk_users FOREIGN KEY (user_type) REFERENCES user_management.lk_user_type(user_type_id);


--
-- TOC entry 2829 (class 0 OID 0)
-- Dependencies: 7
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2018-09-25 07:29:22

--
-- PostgreSQL database dump complete
--

