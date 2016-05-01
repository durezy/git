INSERT INTO public.oddelenie(
nazov)
VALUES 
('Gynekologia'),
('Ortopedia'),
('Kardiologia'),
('Psychiatria'),
('Chirurgia'),
();

INSERT INTO public.lekar(
oddelenie_id, meno)
VALUES 
(1, 'Andrej Svitan'),
(1, 'Denis Smetana'),
(2, 'Adam Vysoky'),
(2, 'Monika Pozicna'),
(3, 'Dana Pekna'),
(3, 'Robert Rysavica'),
(4, 'Pavol Idmaj'),
(4, 'Peter Hluchy'),
(5, 'Alena Hlucha'),
(5, 'Carmen Tvrda');

INSERT INTO public.pacient(
meno, bydlisko, telefon, pohlavie, pacient_hospitalizovany, pacient_vylieceny)
VALUES 
('Yroslava Markova', 'Cigerova 12, Bratislava, SK', '675768699', 'f', TRUE, TRUE),
('Nemo Jelen', 'Ursulinska 1, Kosice, SK', '656754763', 'm', FALSE, TRUE),
('Gennadi Kozlow', 'Wnadowa 11, Krakow, PL', '465465776879', 'm', FALSE, TRUE),
('Stas Laska', 'Nejaka Adresa', '456789087654', 'm', FALSE, TRUE),
('Anet Brathova', 'Dalsia Adresa', '8736487648', 'f', TRUE, TRUE),
('Michal Vrbovsky', 'U dvoch dedov, Bratislava, SK', '7384437843', 'm', TRUE, TRUE),
('Patrik Straka', 'Druha Strana 43, Piestany, SK', '78437834Q6', ?, FALSE, FALSE),
('Adela Jeste Nevecerela', 'Ulica rozpravok 1, Niekde, SK', 'f', ?, TRUE, TRUE),
('Maly Mirko', 'Adresa', 'm', ?, FALSE, FALSE);

INSERT INTO public.lieci(
lekar_id, pacient_id)
VALUES
(1, 1),
(2, 1),
(4, 3),
(5, 4),
(7, 5),
(8, 6),
(9, 7),
(3, 8),
(6, 9);

INSERT INTO public.diagnoza(
oddelenie_id, nazov)
VALUES
(1, 'Porod'),
(2, 'Zlomenina'),
(3, 'Infarkt'),
(4, 'Depresia'),
(5, 'Zapal');

INSERT INTO public.vykon(
druh)
VALUES
('Kontrola'),
('Operacia'),
('Odber');

INSERT INTO public.navsteva(
pacient_id, vykon_id, datum, cas, popis, diagnoza_id, lekar_id)
VALUES
(1, 1, '2012-10-02', '11:41:15', 'nejaky popis1', 1, 1),
(3, 1, '2016-04-10', '10:51:01', 'nejaky popis2', 2, 2),
(5, 2, '2015-06-09', '22:21:15', 'nejaky popis3', 3, 5),
(2, 3, '2016-04-10', '19:61:15', 'nejaky popis4', 4, 4),
(7, 3, '2016-09-12', '01:21:00', 'nejaky popis5', 5, 8);