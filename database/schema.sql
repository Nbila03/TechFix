CREATE DATABASE IF NOT EXISTS techfix_db;
USE techfix_db;


-- =====================================================
-- 1. USERS
-- =====================================================

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


-- =====================================================
-- 2. DEVICE CATEGORIES
-- =====================================================

CREATE TABLE device_categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);


-- =====================================================
-- 3. DEVICES
-- =====================================================

CREATE TABLE devices (
    device_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    device_name VARCHAR(100) NOT NULL,
    brand VARCHAR(50),
    model VARCHAR(100),
    serial_number VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    FOREIGN KEY (category_id)
        REFERENCES device_categories(category_id)
);


-- =====================================================
-- 4. BRANCHES
-- =====================================================

CREATE TABLE branches (
    branch_id INT AUTO_INCREMENT PRIMARY KEY,
    branch_name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(50) NOT NULL,
    latitude DECIMAL(10,7) NOT NULL,
    longitude DECIMAL(10,7) NOT NULL,
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE
);


-- =====================================================
-- 5. TECHNICIANS
-- =====================================================

CREATE TABLE technicians (
    technician_id INT AUTO_INCREMENT PRIMARY KEY,
    branch_id INT NOT NULL,
    technician_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(20),
    is_available BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (branch_id)
        REFERENCES branches(branch_id)
);


-- =====================================================
-- 6. REPAIR SERVICES
-- =====================================================

CREATE TABLE repair_services (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    service_name VARCHAR(100) NOT NULL,
    description TEXT,
    base_price DECIMAL(10,2) NOT NULL,
    estimated_days INT DEFAULT 1,
    is_active BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (category_id)
        REFERENCES device_categories(category_id)
);


-- =====================================================
-- 7. SERVICE SAMPLE IMAGES
-- =====================================================

CREATE TABLE service_sample_images (
    image_id INT AUTO_INCREMENT PRIMARY KEY,
    service_id INT NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    caption VARCHAR(255),

    FOREIGN KEY (service_id)
        REFERENCES repair_services(service_id)
);


-- =====================================================
-- 8. SPARE PARTS
-- =====================================================

CREATE TABLE spare_parts (
    part_id INT AUTO_INCREMENT PRIMARY KEY,
    branch_id INT NOT NULL,
    part_name VARCHAR(100) NOT NULL,
    compatible_device VARCHAR(100),
    quantity INT DEFAULT 0,
    unit_price DECIMAL(10,2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (branch_id)
        REFERENCES branches(branch_id)
);


-- =====================================================
-- 9. REPAIR REQUESTS
-- =====================================================

CREATE TABLE repair_requests (
    repair_id INT AUTO_INCREMENT PRIMARY KEY,

    user_id INT NOT NULL,
    device_id INT NOT NULL,
    service_id INT NOT NULL,

    branch_id INT,
    technician_id INT,

    problem_description TEXT,

    appointment_date DATE,
    appointment_time TIME,

    estimated_cost DECIMAL(10,2),
    final_cost DECIMAL(10,2),

    status VARCHAR(30) DEFAULT 'SUBMITTED',

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    FOREIGN KEY (device_id)
        REFERENCES devices(device_id),

    FOREIGN KEY (service_id)
        REFERENCES repair_services(service_id),

    FOREIGN KEY (branch_id)
        REFERENCES branches(branch_id),

    FOREIGN KEY (technician_id)
        REFERENCES technicians(technician_id)
);


-- =====================================================
-- 10. REPAIR IMAGES
-- =====================================================

CREATE TABLE repair_images (
    repair_image_id INT AUTO_INCREMENT PRIMARY KEY,
    repair_id INT NOT NULL UNIQUE,
    image_path VARCHAR(255) NOT NULL,
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (repair_id)
        REFERENCES repair_requests(repair_id)
);


-- =====================================================
-- 11. REPAIR STATUS HISTORY
-- =====================================================

CREATE TABLE repair_status_history (
    status_history_id INT AUTO_INCREMENT PRIMARY KEY,
    repair_id INT NOT NULL,
    status VARCHAR(30) NOT NULL,
    remarks TEXT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (repair_id)
        REFERENCES repair_requests(repair_id)
);


-- =====================================================
-- 12. REPAIR REQUEST PARTS
-- =====================================================

CREATE TABLE repair_request_parts (
    repair_part_id INT AUTO_INCREMENT PRIMARY KEY,
    repair_id INT NOT NULL,
    part_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,

    FOREIGN KEY (repair_id)
        REFERENCES repair_requests(repair_id),

    FOREIGN KEY (part_id)
        REFERENCES spare_parts(part_id)
);


-- =====================================================
-- 13. REPAIR PART USAGE
-- =====================================================

CREATE TABLE repair_part_usage (
    usage_id INT AUTO_INCREMENT PRIMARY KEY,
    repair_id INT NOT NULL,
    part_id INT NOT NULL,
    quantity_used INT NOT NULL DEFAULT 1,
    used_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (repair_id)
        REFERENCES repair_requests(repair_id),

    FOREIGN KEY (part_id)
        REFERENCES spare_parts(part_id)
);


-- =====================================================
-- 14. PAYMENTS
-- =====================================================

CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    repair_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(30),
    transaction_id VARCHAR(100),
    payment_status VARCHAR(30) DEFAULT 'PENDING',
    paid_at DATETIME,

    FOREIGN KEY (repair_id)
        REFERENCES repair_requests(repair_id)
);