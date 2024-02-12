CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255),
    enabled    BOOLEAN,
    role_id    INT,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);


CREATE TABLE roles
(
    id          INT PRIMARY KEY,
    name        VARCHAR(50),
    description VARCHAR(255)
);


CREATE TABLE addresses
(
    id                BIGSERIAL PRIMARY KEY,
    street            VARCHAR(255),
    city_id           INT,
    state_province_id INT,
    country_id        INT,
    zip_code          VARCHAR(20),
    FOREIGN KEY (city_id) REFERENCES cities (id),
    FOREIGN KEY (state_province_id) REFERENCES states_provinces (id),
    FOREIGN KEY (country_id) REFERENCES countries (id)
);


CREATE TABLE products
(
    id                   BIGSERIAL PRIMARY KEY,
    name                 VARCHAR(255),
    sku                  VARCHAR(50),
    description_short    VARCHAR(255),
    description_full     TEXT,
    unit_price           DECIMAL(10, 2),
    image_url            VARCHAR(255),
    units_in_stock       INT,
    date_created         TIMESTAMP,
    last_updated         TIMESTAMP,
    category_id          BIGINT,
    brand_id             BIGINT,
    country_of_origin_id INT,
    FOREIGN KEY (category_id) REFERENCES products_categories (id),
    FOREIGN KEY (brand_id) REFERENCES brands (id),
    FOREIGN KEY (country_of_origin_id) REFERENCES countries (id)
);


CREATE TABLE products_categories
(
    id            BIGSERIAL PRIMARY KEY,
    category_name VARCHAR(255)
);


CREATE TABLE brands
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    logo VARCHAR(255)
);


CREATE TABLE currencies
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(50),
    symbol VARCHAR(10)
);


CREATE TABLE countries
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255),
    code VARCHAR(3)
);


CREATE TABLE states_provinces
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255),
    country_id INT,
    FOREIGN KEY (country_id) REFERENCES countries (id)
);


CREATE TABLE cities
(
    id                SERIAL PRIMARY KEY,
    name              VARCHAR(255),
    state_province_id INT,
    FOREIGN KEY (state_province_id) REFERENCES states_provinces (id)
);


CREATE TABLE order_items
(
    id         BIGSERIAL PRIMARY KEY,
    image_url  VARCHAR(255),
    quantity   INT,
    unit_price DECIMAL(10, 2),
    order_id   BIGINT,
    product_id BIGINT,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);


CREATE TABLE orders
(
    id                    BIGSERIAL PRIMARY KEY,
    order_tracking_number VARCHAR(255),
    total_price           DECIMAL(10, 2),
    total_quantity        INT,
    status                VARCHAR(50),
    date_created          TIMESTAMP,
    last_updated          TIMESTAMP,
    customer_id           BIGINT,
    shipping_address_id   BIGINT,
    billing_address_id    BIGINT,
    currency_id           INT,
    FOREIGN KEY (customer_id) REFERENCES users (id),
    FOREIGN KEY (shipping_address_id) REFERENCES addresses (id),
    FOREIGN KEY (billing_address_id) REFERENCES addresses (id),
    FOREIGN KEY (currency_id) REFERENCES currencies (id)
);

CREATE TABLE cart_items
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);


CREATE TABLE reviews
(
    id           SERIAL PRIMARY KEY,
    product_id   BIGINT,
    user_id      BIGINT,
    headline     VARCHAR(255),
    comment      TEXT,
    rating       INT,
    date_created TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE product_analogs
(
    id                BIGSERIAL PRIMARY KEY,
    product_id        BIGINT,
    analog_product_id BIGINT,
    FOREIGN KEY (product_id) REFERENCES products (id),
    FOREIGN KEY (analog_product_id) REFERENCES products (id)
);