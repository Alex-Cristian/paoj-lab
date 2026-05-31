DROP TABLE IF EXISTS test_drives;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS sales_agents;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id VARCHAR(30) PRIMARY KEY,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    email VARCHAR(120) NOT NULL,
    phone_number VARCHAR(30) NOT NULL,
    loyalty_tier VARCHAR(40) NOT NULL
);

CREATE TABLE vehicles (
    vin VARCHAR(17) PRIMARY KEY,
    model_name VARCHAR(100) NOT NULL,
    production_year INT NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    available BOOLEAN NOT NULL,
    color VARCHAR(60) NOT NULL,
    body_style VARCHAR(60) NOT NULL,
    fuel_type VARCHAR(40) NOT NULL,
    horsepower INT NOT NULL,
    quattro_system VARCHAR(80) NOT NULL,
    battery_capacity_kwh INT NULL,
    range_km INT NULL,
    fast_charging BOOLEAN NULL
);

CREATE TABLE sales_agents (
    id VARCHAR(30) PRIMARY KEY,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    email VARCHAR(120) NOT NULL,
    department VARCHAR(80) NOT NULL,
    salary DECIMAL(12, 2) NOT NULL,
    region VARCHAR(80) NOT NULL,
    years_of_experience INT NOT NULL
);

CREATE TABLE orders (
    id VARCHAR(30) PRIMARY KEY,
    customer_id VARCHAR(30) NOT NULL,
    vehicle_vin VARCHAR(17) NOT NULL,
    agent_id VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    completed BOOLEAN NOT NULL,
    completed_at TIMESTAMP NULL,
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_orders_vehicle FOREIGN KEY (vehicle_vin) REFERENCES vehicles(vin),
    CONSTRAINT fk_orders_agent FOREIGN KEY (agent_id) REFERENCES sales_agents(id)
);

CREATE TABLE test_drives (
    id VARCHAR(30) PRIMARY KEY,
    customer_id VARCHAR(30) NOT NULL,
    vehicle_vin VARCHAR(17) NOT NULL,
    scheduled_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_test_drives_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_test_drives_vehicle FOREIGN KEY (vehicle_vin) REFERENCES vehicles(vin)
);
