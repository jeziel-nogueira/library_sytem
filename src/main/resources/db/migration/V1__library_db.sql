
CREATE TABLE users (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    address VARCHAR(150),
    phone VARCHAR(20),
    password VARCHAR(255),
    status VARCHAR(50) NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE books (
    id UUID PRIMARY KEY NOT NULL,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    genre VARCHAR(50),
    isbn VARCHAR(13),
    status VARCHAR(50) NOT NULL CHECK (status IN ('AVAILABLE', 'UNAVAILABLE')),
    publisher VARCHAR(50),
    tags VARCHAR(50),
    coverUrl VARCHAR(100),
    reserve VARCHAR(100)
);

CREATE TABLE loans (
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID NOT NULL,
    book_id UUID NOT NULL,
    loan_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'RETURNED', 'OVERDUE')),

    CONSTRAINT fk_loans_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_loans_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

CREATE TABLE reservations (
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID NOT NULL,
    book_id UUID NOT NULL,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED')),

    CONSTRAINT fk_reservations_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_reservations_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);
