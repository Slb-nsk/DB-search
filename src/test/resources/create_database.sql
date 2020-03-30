CREATE TABLE customers(
  customerId SERIAL PRIMARY KEY,
  firstName TEXT NOT NULL,
  lastName TEXT NOT NULL
);


CREATE TABLE products(
  productId SERIAL PRIMARY KEY,
  productName TEXT NOT NULL,
  price INT
);


CREATE TABLE purchases(
  purchaseId SERIAL PRIMARY KEY,
  customerId INT NOT NULL REFERENCES customers(customerId),
  productId INT NOT NULL REFERENCES products(productId),
  purchaseDate DATE
);


INSERT INTO customers(firstName, lastName) VALUES ('Антон', 'Иванов');
INSERT INTO customers(firstName, lastName) VALUES ('Николай', 'Иванов');
INSERT INTO customers(firstName, lastName) VALUES ('Валентин', 'Петров');
INSERT INTO products(productName, price) VALUES ('Хлеб', '100');
INSERT INTO products(productName, price) VALUES ('Сметана', '200');
INSERT INTO products(productName, price) VALUES ('Колбаса', '300');
INSERT INTO products(productName, price) VALUES ('Сыр', '400');
INSERT INTO products(productName, price) VALUES ('Минеральная вода', '500');
INSERT INTO purchases(customerId, productId, purchaseDate) VALUES ('1','1','2020-04-01');
INSERT INTO purchases(customerId, productId, purchaseDate) VALUES ('1','1','2020-04-02');
INSERT INTO purchases(customerId, productId, purchaseDate) VALUES ('1','1','2020-04-03');
INSERT INTO purchases(customerId, productId, purchaseDate) VALUES ('1','1','2020-04-04');
INSERT INTO purchases(customerId, productId, purchaseDate) VALUES ('1','2','2020-04-04');
INSERT INTO purchases(customerId, productId, purchaseDate) VALUES ('2','1','2020-04-04');