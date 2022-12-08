CREATE TABLE users
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO users (first_name, last_name, email, password)
VALUES ('John', 'Doe', 'john.doe@example.com', '123456'),
       ('Jane', 'Doe', 'jane.doe@example.com', '123456'),
       ('Bob', 'Smith', 'bob.smith@example.com', '123456'),
       ('Alice', 'Smith', 'alice.smith@example.com', '123456'),
       ('James', 'Johnson', 'james.johnson@example.com', '123456'),
       ('Emily', 'Johnson', 'emily.johnson@example.com', '123456'),
       ('William', 'Williams', 'william.williams@example.com', '123456'),
       ('Samantha', 'Williams', 'samantha.williams@example.com', '123456'),
       ('David', 'Brown', 'david.brown@example.com', '123456'),
       ('Ashley', 'Brown', 'ashley.brown@example.com', '123456'),
       ('Christopher', 'Jones', 'christopher.jones@example.com', '123456'),
       ('Amanda', 'Jones', 'amanda.jones@example.com', '123456'),
       ('Michael', 'Miller', 'michael.miller@example.com', '123456'),
       ('Sara', 'Miller', 'sara.miller@example.com', '123456'),
       ('Robert', 'Davis', 'robert.davis@example.com', '123456'),
       ('Megan', 'Davis', 'megan.davis@example.com', '123456'),
       ('Richard', 'Garcia', 'richard.garcia@example.com', '123456'),
       ('Stephanie', 'Garcia', 'stephanie.garcia@example.com', '123456'),
       ('Daniel', 'Martinez', 'daniel.martinez@example.com', '123456'),
       ('Melissa', 'Martinez', 'melissa.martinez@example.com', '123456');

