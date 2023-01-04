CREATE TABLE executions (
    id CHAR(36) NOT NULL,
    instructions VARCHAR(255) NOT NULL,
    createdAt DATETIME NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;