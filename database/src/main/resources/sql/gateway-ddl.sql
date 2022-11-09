--
-- PostgreSQL database dump
--

-- Dumped from database version 11.14
-- Dumped by pg_dump version 11.14

-- Started on 2022-02-24 15:53:32

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 203 (class 1259 OID 25236)
-- Name: msp_meta; Type: TABLE; Schema: msp; Owner: postgres
--

CREATE TABLE msp.msp_meta (
    msp_id integer NOT NULL,
    has_hold boolean,
    has_no_parking_zone boolean,
    has_operating_zone boolean,
    has_parking boolean,
    has_pref_parking_zone boolean,
    has_speed_limit_zone boolean,
    has_station boolean,
    has_station_status boolean,
    has_vehicle boolean,
    is_enabled boolean,
    logo_format character varying(4),
    logo_url character varying(200),
    name character varying(50) NOT NULL,
    operator character varying(50),
    price_list_text character varying(255),
    primary_color character varying(10),
    secondary_color character varying(10),
    type character varying(20),
    url character varying(200),
    url_webview boolean
);


ALTER TABLE msp.msp_meta OWNER TO postgres;

--
-- TOC entry 2707 (class 2606 OID 25243)
-- Name: msp_meta msp_meta_pkey; Type: CONSTRAINT; Schema: msp; Owner: postgres
--

ALTER TABLE ONLY msp.msp_meta
    ADD CONSTRAINT msp_meta_pkey PRIMARY KEY (msp_id);


-- Completed on 2022-02-24 15:53:32

--
-- PostgreSQL database dump complete
--

