CREATE TABLE Prisoner (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(64) NOT NULL,
    birthday DATE NOT NULL,
    receipt_date DATE NOT NULL,
    term INT NOT NULL CHECK (term >= 1),
    article VARCHAR(64) NOT NULL
);

CREATE TABLE users (
    id UUID PRIMARY KEY NOT NULL,
    email VARCHAR(64) NOT NULL,
    password TEXT NOT NULL,
    role INT NOT NULL
);