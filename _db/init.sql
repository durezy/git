CREATE TABLE IF NOT EXISTS public.oddelenie
(
  id integer NOT NULL DEFAULT nextval('"Oddelenie_id_seq"'::regclass),
  nazov character varying(30),
  CONSTRAINT oddelenie_id PRIMARY KEY (id)

CREATE TABLE IF NOT EXISTS public.lekar
(
  id integer NOT NULL DEFAULT nextval('"Lekar_id_seq"'::regclass),
  oddelenie_id integer NOT NULL DEFAULT nextval('"Lekar_oddelenie_id_seq"'::regclass),
  meno character varying(100),
  CONSTRAINT lekar_id PRIMARY KEY (id),
  CONSTRAINT oddelenie_id FOREIGN KEY (oddelenie_id)
      REFERENCES public.oddelenie (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)

CREATE TABLE IF NOT EXISTS public.pacient
(
  id integer NOT NULL DEFAULT nextval('"Pacient_id_seq"'::regclass),
  meno character varying(100),
  bydlisko character varying(100),
  telefon character varying(15),
  pohlavie character varying(2),
  pacient_hospitalizovany boolean,
  ca boolean,
  CONSTRAINT pacient_id PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS public.lieci
(
  lekar_id integer NOT NULL DEFAULT nextval('"Lieci_lekar_id_seq"'::regclass),
  pacient_id integer NOT NULL DEFAULT nextval('"Lieci_pacient_id_seq"'::regclass),
  CONSTRAINT lekar_id FOREIGN KEY (lekar_id)
      REFERENCES public.lekar (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT pacient_id FOREIGN KEY (pacient_id)
      REFERENCES public.pacient (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)

CREATE TABLE IF NOT EXISTS public.diagnoza
(
  id integer NOT NULL DEFAULT nextval('"Diagnoza_id_seq"'::regclass),
  oddelenie_id integer NOT NULL DEFAULT nextval('diagnoza_oddelenie_id_seq'::regclass),
  nazov character varying(30),
  CONSTRAINT diagnoza_id PRIMARY KEY (id),
  CONSTRAINT oddelenie_id FOREIGN KEY (oddelenie_id)
      REFERENCES public.oddelenie (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)

CREATE TABLE IF NOT EXISTS public.vykon
(
  id integer NOT NULL DEFAULT nextval('"Vykon_id_seq"'::regclass),
  druh character varying(30),
  CONSTRAINT vykon_id PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS public.navsteva
(
  id integer NOT NULL DEFAULT nextval('"Navsteva_id_seq"'::regclass),
  pacient_id integer NOT NULL DEFAULT nextval('"Navsteva_pacient_id_seq"'::regclass),
  vykon_id integer NOT NULL DEFAULT nextval('"Navsteva_vykon_id_seq"'::regclass),
  datum date,
  cas time without time zone,
  popis text,
  diagnoza_id integer NOT NULL DEFAULT nextval('navsteva_diagnoza_id_seq'::regclass),
  lekar_id integer NOT NULL DEFAULT nextval('navsteva_lekar_id_seq'::regclass),
  CONSTRAINT navsteva_id PRIMARY KEY (id),
  CONSTRAINT diagnoza_id FOREIGN KEY (diagnoza_id)
      REFERENCES public.diagnoza (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT lekar_id FOREIGN KEY (lekar_id)
      REFERENCES public.lekar (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT pacient_id FOREIGN KEY (pacient_id)
      REFERENCES public.pacient (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT vykon_id FOREIGN KEY (vykon_id)
      REFERENCES public.vykon (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)

