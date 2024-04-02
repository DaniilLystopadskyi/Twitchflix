DROP TABLE IF EXISTS MOVIE,STREAM,USER;

CREATE TABLE 
MOVIE (
  MovieId INT PRIMARY KEY AUTO_INCREMENT,
  Title VARCHAR(128) NOT NULL,
  ImgURI VARCHAR(256) NOT NULL,
  Duration TIME NOT NULL
);

CREATE TABLE 
STREAM (
 StreamId INT PRIMARY KEY AUTO_INCREMENT,
 UserId INT,
 Title VARCHAR(128) NOT NULL,
 ImgURI VARCHAR(256) NOT NULL
);

CREATE TABLE
USER (
 UserId INT PRIMARY KEY AUTO_INCREMENT,
 Name VARCHAR(64),
 Email VARCHAR(64),
 Password VARCHAR(128)
);


INSERT INTO MOVIE VALUES
 (1,'Popeye the Sailor Meets Ali Baba''s Forty Thieves',
 '/images/1/Popeye__ia_thumb.jpg','00:16:58'),
 (2,'The Letter','/images/2/Letter__ia_thumb.jpg','00:06:30'),
 (3,'Charlie Chaplin''s "The Vagabond"',
 '/images/3/Charlie__ia_thumb.jpg','00:24:43'),
 (4,'Night of the Living Dead','/images/4/NLD__ia_thumb.jpg',
 '01:35:17'),
 (5,'Big Buck Bunny','/images/5/BBB__ia_thumb.jpg','00:09:56');






 
 
 
 

