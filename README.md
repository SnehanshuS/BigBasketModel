###Big Basket Model
```
A NextGen scalable solution model to provide an effective and fast search results for large customer base of an Ecommerce website using Java/J2EE, Redis, Cassandra & Elasticsearch.
```

###DML, DDL Statements
```
CREATE KEYSPACE ecommodel WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor' : 3}; create table products (
pid int primary key,
pname varchar, 
pdesc varchar, 
pcost float, 
imageurl varchar );

use ecommodel;

INSERT INTO products (pid, pname, pdesc, pcost, imageurl) VALUES (1001,'Laptop', 'Laptop Dell Intel core i5 processor', 450,'http://res.cloudinary.com/dzh2erb9v/image/upload/v1461651436/dell_laptop_tqdfed.jpg');
INSERT INTO products (pid, pname, pdesc, pcost, imageurl) VALUES (1002,'Laptop', 'Laptop Lenevo Intel core i5 processor', 550, 'http://res.cloudinary.com/dzh2erb9v/image/upload/v1461651431/lenovo_laptop_bimnvz.jpg');
INSERT INTO products (pid, pname, pdesc, pcost, imageurl) VALUES (1003,'Camera', 'Camera Nikon, 21 MP, SLR', 1000, 'http://res.cloudinary.com/dzh2erb9v/image/upload/v1461651436/nikon_slr_tnmjoz.jpg');
INSERT INTO products (pid, pname, pdesc, pcost, imageurl) VALUES (1004,'Mobile', 'Mobile Apple Phone 6S, 16GB', 750, 'http://res.cloudinary.com/dzh2erb9v/image/upload/v1461651435/iphone6s_t3boud.jpg');
INSERT INTO products (pid, pname, pdesc, pcost, imageurl) VALUES (1005,'Laptop', 'Laptop Dell , 15 inches touch screen, intel core i7 processor', 750, 'http://res.cloudinary.com/dzh2erb9v/image/upload/v1461651436/dell_laptop_tqdfed.jpg');
INSERT INTO products (pid, pname, pdesc, pcost, imageurl) VALUES (1006,'Laptop', 'Laptop Sony , 14 inches touch screen, intel core i7 processor', 650, 'http://res.cloudinary.com/dzh2erb9v/image/upload/v1461651431/sony_laptop_ow23hk.jpg');
INSERT INTO products (pid, pname, pdesc, pcost, imageurl) VALUES (1007,'Tablet', 'Samsung Galaxy Tablet, 10.1 inch displaym MicroSD, 64GB', 250, 'http://res.cloudinary.com/dzh2erb9v/image/upload/v1461732855/samsung_tablet_eqp0sw.jpg');
INSERT INTO products (pid, pname, pdesc, pcost, imageurl) VALUES (1008,'Tablet', 'Microsoft Surface3 Tablet, 10.8 inch, windows 10, 128GB', 350, 'http://res.cloudinary.com/dzh2erb9v/image/upload/v1461732858/microsoft_tablet_ixadf4.png');
INSERT INTO products (pid, pname, pdesc, pcost, imageurl) VALUES (1010,'Camera', 'Camera Canon, 18 MP, SLR, CMOS sensor', 650, 'http://res.cloudinary.com/dzh2erb9v/image/upload/v1461732848/canon_slr_vjbye6.jpg');
```
