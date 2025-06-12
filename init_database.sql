-- Create database
CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- Create student table
CREATE TABLE IF NOT EXISTS student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_number VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    age INT,
    class_id BIGINT,
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create class table
CREATE TABLE IF NOT EXISTS student_class (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    class_name VARCHAR(50) NOT NULL,
    grade VARCHAR(20) NOT NULL,
    capacity INT NOT NULL,
    current_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create score table
CREATE TABLE IF NOT EXISTS score (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    subject VARCHAR(50) NOT NULL,
    score DECIMAL(5,2) NOT NULL,
    semester VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(id)
);

-- Add some test data
INSERT INTO student_class (class_name, grade, capacity) VALUES
('Class 1A', '2024', 30),
('Class 1B', '2024', 30),
('Class 2A', '2023', 35),
('Class 2B', '2023', 35);

INSERT INTO student (student_number, name, gender, age, class_id, phone, email) VALUES
('2024001', 'John Doe', 'Male', 20, 1, '13800138001', 'john@example.com'),
('2024002', 'Jane Smith', 'Female', 19, 1, '13800138002', 'jane@example.com'),
('2024003', 'Mike Johnson', 'Male', 21, 2, '13800138003', 'mike@example.com'); 