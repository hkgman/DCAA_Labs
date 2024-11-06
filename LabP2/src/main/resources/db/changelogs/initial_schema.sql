CREATE TABLE Prisoner (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(64) NOT NULL,
    birthday DATE NOT NULL,
    receipt_date DATE NOT NULL,
    term INT NOT NULL CHECK (term >= 1),
    article VARCHAR(64) NOT NULL
);