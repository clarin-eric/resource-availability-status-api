--DONE IN JAVA CODE
--CREATE DATABASE stormcrawler;

--DON'T ASK, stupid mysql, quote stackoverflow: To go along with @ypercube's comment that CURRENT_TIMESTAMP is stored as UTC but retrieved as the current timezone
--WHYYYY????????
--TO offset this retarded behaviour, I set timezone to 0 so it doesn't do any conversions to explicitly written variables.
SET @@global.time_zone = '+00:00';

CREATE TABLE urls(
 url VARCHAR(255),
 status VARCHAR(16) DEFAULT 'DISCOVERED',
 nextfetchdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 metadata TEXT,
 bucket SMALLINT DEFAULT 0,
 host VARCHAR(128),
 record VARCHAR(255),
 collection VARCHAR(255),
 expectedMimeType VARCHAR(255),
 PRIMARY KEY(url)
);

CREATE TABLE status(
 url VARCHAR(255),
 statusCode INT(32),
 method VARCHAR(128),
 contentType VARCHAR(255),
 byteSize INT(255),
 duration INT(128),
 timestamp TIMESTAMP,
 redirectCount INT(32),
 FOREIGN KEY (url) REFERENCES urls (url)
 ON DELETE CASCADE
 ON UPDATE CASCADE
);

INSERT INTO urls(url,record,collection,expectedMimeType)
VALUES
("http://www.ailla.org/waiting.html", null, null, null),
("http://www.ailla.org/audio_files/EMP1M1B1.mp3", null, null, null),
("http://www.ailla.org/audio_files/WBA1M3A2.mp3", null, null, null),
("http://www.ailla.org/text_files/WBA1M1A2a.mp3", null, null, null),
("http://www.ailla.org/audio_files/KUA2M1A1.mp3", null, null, null),
("http://www.ailla.org/text_files/KUA2M1.pdf", null, null, null),
("http://www.ailla.org/audio_files/sarixojani.mp3", null, null, null),
("http://www.ailla.org/audio_files/TEH11M7A1sa.mp3", null, null, null),
("http://www.ailla.org/text_files/TEH11M7.pdf", null, null, null),
("http://dspin.dwds.de:8088/ddc-sru/dta/", null, null, null),
("http://dspin.dwds.de:8088/ddc-sru/grenzboten/", null, null, null),
("http://dspin.dwds.de:8088/ddc-sru/rem/", null, null, null),
("http://www.deutschestextarchiv.de/rem/?d=M084E-N1.xml", null, null, null),
("http://www.deutschestextarchiv.de/rem/?d=M220P-N1.xml", null, null, null),
("http://www.deutschestextarchiv.de/rem/?d=M119-N1.xml", null, null, null),
("http://www.deutschestextarchiv.de/rem/?d=M171-G1.xml", null, null, null),
("http://www.deutschestextarchiv.de/rem/?d=M185-N1.xml", null, null, null),
("http://www.deutschestextarchiv.de/rem/?d=M048P-N1.xml", null, null, null),
("http://www.deutschestextarchiv.de/rem/?d=M112-G1.xml", null, null, null),
("https://www.google.com", null, "Google", null),
("https://maps.google.com", null, "Google", null),
("https://drive.google.com", null, "Google", null);

--Should be same date as the tests:
SET @then = '2019-10-11 00:00:00';

INSERT INTO status(url,method,statusCode,contentType,byteSize,duration,timestamp,redirectCount)
VALUES
("http://www.ailla.org/waiting.html", "HEAD", 200, "text/html; charset=UTF-8", 100, 132, @then, 0),
("http://www.ailla.org/audio_files/EMP1M1B1.mp3", "GET",  400, "text/html; charset=UTF-8", 0, 46, @then, 0),
("http://www.ailla.org/audio_files/WBA1M3A2.mp3", "GET",  400, "text/html; charset=UTF-8", 0, 46, @then, 0),
("http://www.ailla.org/text_files/WBA1M1A2a.mp3", "GET",  400, "text/html; charset=UTF-8", 0, 46, @then, 0),
("http://www.ailla.org/audio_files/KUA2M1A1.mp3", "GET",  400, "text/html; charset=UTF-8", 0, 56, @then, 0),
("http://www.ailla.org/text_files/KUA2M1.pdf", "HEAD",  200, "text/html; charset=UTF-8", 0, 51, @then, 0),
("http://www.ailla.org/audio_files/sarixojani.mp3", "GET",  400, "text/html; charset=UTF-8", 0, 48, @then, 0),
("http://www.ailla.org/audio_files/TEH11M7A1sa.mp3", "GET",  400, "text/html; charset=UTF-8", 0, 48, @then, 0),
("http://www.ailla.org/text_files/TEH11M7.pdf", "HEAD",  200, "text/html; charset=UTF-8", 0, 57, @then, 0),
("http://dspin.dwds.de:8088/ddc-sru/dta/", "HEAD",  200, "application/xml;charset=utf-8", 2094, 67, @then, 0),
("http://dspin.dwds.de:8088/ddc-sru/grenzboten/", "HEAD",  200, "application/xml;charset=utf-8", 2273, 57, @then, 0),
("http://dspin.dwds.de:8088/ddc-sru/rem/", "HEAD",  200, "application/xml;charset=utf-8", 2497, 58, @then, 0),
("http://www.deutschestextarchiv.de/rem/?d=M084E-N1.xml", "HEAD",  200, "text/html; charset=utf-8", 0, 591, @then, 0),
("http://www.deutschestextarchiv.de/rem/?d=M220P-N1.xml", "HEAD",  200, "text/html; charset=utf-8", 0, 592, @then, 0),
("http://www.deutschestextarchiv.de/rem/?d=M119-N1.xml", "HEAD",  200, "text/html; charset=utf-8", 0, 602, @then, 0),
("http://www.deutschestextarchiv.de/rem/?d=M171-G1.xml", "HEAD",  200, "text/html; charset=utf-8", 0, 613, @then, 0),
("http://www.deutschestextarchiv.de/rem/?d=M185-N1.xml", "HEAD",  200, "text/html; charset=utf-8", 0, 605, @then, 0),
("http://www.deutschestextarchiv.de/rem/?d=M048P-N1.xml", "HEAD",  200, "text/html; charset=utf-8", 0, 599, @then, 0),
("http://www.deutschestextarchiv.de/rem/?d=M112-G1.xml", "HEAD",  200, "text/html; charset=utf-8", 0, 591, @then, 0),
("https://www.google.com", "HEAD",  200, "text/html; charset=ISO-8859-1", 0, 222, @then, 0),
("https://maps.google.com", "HEAD",  200, "text/html; charset=UTF-8", 0, 440, @then, 2),
("https://drive.google.com", "HEAD",  200, "text/html; charset=UTF-8", 73232, 413, @then, 1);

CREATE VIEW statusView AS
SELECT u.url, record, collection, expectedMimeType, statusCode, method, contentType, byteSize, duration, timestamp, redirectCount
FROM (
	SELECT st.*
	FROM status st
	INNER JOIN
		(SELECT url, MAX(timestamp) AS maxTimestamp
		FROM status
		GROUP BY url) groupedst
	ON st.url=groupedst.url
    AND st.timestamp=groupedst.maxTimestamp
 ) s RIGHT JOIN urls u
ON s.url=u.url;