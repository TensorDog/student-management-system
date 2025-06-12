-- 创建班级表
CREATE TABLE IF NOT EXISTS student_class (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(100) NOT NULL COMMENT '班级名称',
    grade VARCHAR(20) NOT NULL COMMENT '年级',
    capacity INT NOT NULL DEFAULT 30 COMMENT '容量',
    current_count INT NOT NULL DEFAULT 0 COMMENT '当前人数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 插入测试数据
INSERT INTO student_class (class_name, grade, capacity, current_count) VALUES 
('计算机科学1班', '2024', 30, 25),
('计算机科学2班', '2024', 32, 28),
('软件工程1班', '2023', 35, 30);