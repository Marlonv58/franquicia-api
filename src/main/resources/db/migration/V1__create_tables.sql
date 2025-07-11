CREATE TABLE IF NOT EXISTS franquicia (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sucursal (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    franchise_id BIGINT NOT NULL,
    CONSTRAINT fk_sucursal_franquicia FOREIGN KEY (franchise_id)
    REFERENCES franquicia(id)
    ON DELETE CASCADE
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS producto (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    branch_id BIGINT NOT NULL,
    CONSTRAINT fk_producto_sucursal FOREIGN KEY (branch_id)
    REFERENCES sucursal(id)
    ON DELETE CASCADE
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;