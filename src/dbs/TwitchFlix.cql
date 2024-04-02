CREATE KEYSPACE IF NOT EXISTS twitchflix
WITH replication = {'class':'SimpleStrategy','replication_factor' :1 };

USE twitchflix;

DROP TABLE IF EXISTS mp;

CREATE TABLE IF NOT EXISTS mp (
   id int PRIMARY KEY,
   uri text
);

DROP TABLE IF EXISTS avi;

CREATE TABLE IF NOT EXISTS avi (
   id int PRIMARY KEY,
   uri text
);

DROP TABLE IF EXISTS mpg;

CREATE TABLE IF NOT EXISTS mpg (
   id int PRIMARY KEY,
   uri text
);

DROP TABLE IF EXISTS ogv;

CREATE TABLE IF NOT EXISTS ogv (
   id int PRIMARY KEY,
   uri text
);

DROP TABLE IF EXISTS stream;

CREATE TABLE IF NOT EXISTS stream (
   id int PRIMARY KEY,
   uri text
);


INSERT INTO avi (id,uri) VALUES (1,'/videos/source/1/PopeyeAliBaba.avi');
INSERT INTO mpg (id,uri) VALUES (1,'/videos/source/1/PopeyeAliBaba.mpg');
INSERT INTO mp (id,uri) VALUES (1,'/videos/source/1/PopeyeAliBaba_512kb.mp4');
INSERT INTO ogv (id,uri) VALUES (1,'/videos/source/1/PopeyeAliBaba.ogv');
INSERT INTO avi (id,uri) VALUES (2,'/videos/source/2/tl.avi');
INSERT INTO ogv (id,uri) VALUES (2,'/videos/source/2/tl.ogv');
INSERT INTO mp (id,uri) VALUES (2,'/videos/source/2/tl_512kb.mp4');
INSERT INTO mp (id,uri) VALUES (3,'/videos/source/3/CC_1916_07_10_TheVagabond.mp4');
INSERT INTO mpg (id,uri) VALUES (3,'/videos/source/3/CC_1916_07_10_TheVagabond.mpg');
INSERT INTO ogv (id,uri) VALUES (3,'/videos/source/3/CC_1916_07_10_TheVagabond.ogv');
INSERT INTO mp (id,uri) VALUES (4,'/videos/source/4/night_of_the_living_dead.mp4');
INSERT INTO ogv (id,uri) VALUES (4,'/videos/source/4/night_of_the_living_dead.ogv');
INSERT INTO avi (id,uri) VALUES (5,'/videos/source/5/big_buck_bunny_720p_surround.avi');
INSERT INTO mp (id,uri) VALUES (5,'/videos/source/5/big_buck_bunny_720p_surround.mp4');
INSERT INTO ogv (id,uri) VALUES (5,'/videos/source/5/big_buck_bunny_720p_surround.ogv');
