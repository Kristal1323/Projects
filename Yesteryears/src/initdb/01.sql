CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_description VARCHAR(50),
    price DECIMAL(6,2) NOT NULL
);

INSERT INTO rooms (room_description, price)
VALUES ("Prehistoric Era", 177.00);

INSERT INTO rooms (room_description, price)
VALUES ("Classical Era", 377.00);

INSERT INTO rooms (room_description, price)
VALUES ("The Middle Ages", 277.00);

INSERT INTO rooms (room_description, price)
VALUES ("Early Modern Era", 277.00);

INSERT INTO rooms (room_description, price)
VALUES ("Modern Era", 377.00);

CREATE TABLE tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_description VARCHAR(50),
    price DECIMAL(6,2) NOT NULL
);

INSERT INTO tickets (ticket_description, price)
VALUES ("Single Day Pass", 77.00);

INSERT INTO tickets (ticket_description, price)
VALUES ("3 Days Pass", 157.00);

INSERT INTO tickets (ticket_description, price)
VALUES ("Annual Pass", 177.00);

CREATE TABLE creditcards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    card_description VARCHAR(50)
);

INSERT INTO creditcards (card_description)
VALUES ("Visa");

INSERT INTO creditcards (card_description)
VALUE ("Master Card");

CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    emailaddress VARCHAR(100),
    phone VARCHAR(10)
);

CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    room_id INT,
    ticket_id INT,
    creditcard_id INT,
    cardnumber VARCHAR(16),
    cvv VARCHAR(3),
     FOREIGN KEY (customer_id) 
        REFERENCES customers(id)
        ON DELETE RESTRICT,
    FOREIGN KEY (room_id) 
        REFERENCES rooms(id)
        ON DELETE RESTRICT,
    FOREIGN KEY (ticket_id) 
        REFERENCES tickets(id)
        ON DELETE RESTRICT,
    FOREIGN KEY (creditcard_id) 
        REFERENCES creditcards(id)
        ON DELETE RESTRICT
);

CREATE TABLE emails (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    enquiry VARCHAR(50),
    email_description VARCHAR(255),
    FOREIGN KEY (customer_id) 
        REFERENCES customers(id)
        ON DELETE RESTRICT

);


