CREATE TABLE Readers (
                readerId INTEGER NOT NULL,
                readerName VARCHAR NOT NULL,
                phone VARCHAR NOT NULL,
                address VARCHAR NOT NULL,
                CONSTRAINT readerid PRIMARY KEY (readerId)
);


CREATE TABLE Cities (
                cityId INTEGER NOT NULL,
                cityName VARCHAR NOT NULL,
                CONSTRAINT cityid PRIMARY KEY (cityId)
);


CREATE TABLE Publishers (
                publisherId INTEGER NOT NULL,
                cityId INTEGER NOT NULL,
                publisherName VARCHAR NOT NULL,
                CONSTRAINT publisherid PRIMARY KEY (publisherId)
);


CREATE TABLE Authors (
                authorId INTEGER NOT NULL,
                authorName VARCHAR NOT NULL,
                CONSTRAINT authorid PRIMARY KEY (authorId)
);


CREATE TABLE Subjects (
                subjectId INTEGER NOT NULL,
                subjectName VARCHAR NOT NULL,
                CONSTRAINT subjectid PRIMARY KEY (subjectId)
);


CREATE TABLE Books (
                bookId INTEGER NOT NULL,
                publisherId INTEGER NOT NULL,
                bookName VARCHAR NOT NULL,
                subjectId INTEGER NOT NULL,
                shelfNumber INTEGER NOT NULL,
                pubYear INTEGER NOT NULL,
                price NUMERIC NOT NULL,
                countInStock INTEGER NOT NULL,
                CONSTRAINT bookid PRIMARY KEY (bookId)
);


CREATE TABLE Purchase (
                bookId INTEGER NOT NULL,
                readerId INTEGER NOT NULL,
                purDate DATE NOT NULL,
                price NUMERIC NOT NULL,
                CONSTRAINT purchaseid PRIMARY KEY (bookId, readerId)
);


CREATE TABLE Delivery (
                readerId INTEGER NOT NULL,
                bookId INTEGER NOT NULL,
                receiptDate DATE NOT NULL,
                returnDate DATE NOT NULL,
                CONSTRAINT deliveryid PRIMARY KEY (readerId, bookId, receiptDate)
);


CREATE TABLE BooksAuthors (
                authorId INTEGER NOT NULL,
                bookId INTEGER NOT NULL,
                CONSTRAINT baid PRIMARY KEY (authorId, bookId)
);


ALTER TABLE Delivery ADD CONSTRAINT readers_delivery_fk
FOREIGN KEY (readerId)
REFERENCES Readers (readerId)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Purchase ADD CONSTRAINT readers_purchase_fk
FOREIGN KEY (readerId)
REFERENCES Readers (readerId)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Publishers ADD CONSTRAINT cities_publishers_fk
FOREIGN KEY (cityId)
REFERENCES Cities (cityId)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Books ADD CONSTRAINT publishers_books_fk
FOREIGN KEY (publisherId)
REFERENCES Publishers (publisherId)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE BooksAuthors ADD CONSTRAINT authors_booksauthors_fk
FOREIGN KEY (authorId)
REFERENCES Authors (authorId)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Books ADD CONSTRAINT subjects_books_fk
FOREIGN KEY (subjectId)
REFERENCES Subjects (subjectId)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE BooksAuthors ADD CONSTRAINT books_booksauthors_fk
FOREIGN KEY (bookId)
REFERENCES Books (bookId)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Delivery ADD CONSTRAINT books_delivery_fk
FOREIGN KEY (bookId)
REFERENCES Books (bookId)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Purchase ADD CONSTRAINT books_purchase_fk
FOREIGN KEY (bookId)
REFERENCES Books (bookId)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

--------------------------------------------------------------------------------

insert into Cities (cityId, cityName)
values (1, 'Город1'),
       (2, 'Город2'),
       (3, 'Город3');

select * from Cities;
delete from Cities;

-------

insert into Publishers (publisherId, publisherName, cityId)
values (1, 'Издательство1', 1),
       (2, 'Издательство2', 2),
       (3, 'Издательство3', 3);

select * from Publishers;
delete from Publishers;

-------

insert into Subjects (subjectId, subjectName)
values (1, 'Тематика1'),
       (2, 'Тематика2'),
       (3, 'Тематика3');

select * from Subjects;
delete from Subjects;

-------

insert into Authors (authorId, authorName)
values (1, 'Автор1'),
       (2, 'Автор2'),
       (3, 'Автор3');

select * from Authors;
delete from Authors;

-------

insert into Books (bookId, bookName, publisherId, subjectId, shelfNumber, pubYear, price, countInStock)
values (1, 'Книга1', 1, 1, 1, 2011, 111.00, 1),
       (2, 'Книга2', 2, 2, 2, 2012, 222.00, 2),
       (3, 'Книга3', 3, 3, 3, 2013, 333.00, 3);

select * from Books;
delete from Books;

-------

insert into BooksAuthors (bookId, authorId)
values (1, 1),
       (2, 2),
       (3, 3);

select * from BooksAuthors;
delete from BooksAuthors;

-------

insert into Books (bookId, bookName, publisherId, subjectId, shelfNumber, pubYear, price, countInStock)
values (4, 'Книга4', 1, 1, 1, 2014, 444.00, 0);

--------------------------------------------------------------------------------

select publisherId, publisherName
from Publishers;

-------

select subjectId, subjectName
from Subjects;

-------

select authorId, authorName
from Authors;

-------

select b.bookId, b.bookName, b.pubYear, b.shelfNumber, b.price, b.countInStock
from (Books b left join Publishers p on b.publisherId = p.publisherId) left join BooksAuthors ba on b.bookId = ba.bookId
order by b.bookName;

-------

select distinct b.bookId, b.bookName, b.pubYear, b.shelfNumber, b.price, b.countInStock
from (Books b left join Publishers p on b.publisherId = p.publisherId) left join BooksAuthors ba on b.bookId = ba.bookId
order by b.bookName;
