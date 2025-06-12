-- 创建学生表
CREATE TABLE IF NOT EXISTS student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_number VARCHAR(20) NOT NULL UNIQUE COMMENT '学号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) NOT NULL COMMENT '性别',
    age INT NOT NULL COMMENT '年龄',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    class_id BIGINT COMMENT '班级ID',
    enrollment_date DATE COMMENT '入学日期',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 创建班级表（与class-service共享）
CREATE TABLE IF NOT EXISTS student_class (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(100) NOT NULL COMMENT '班级名称',
    grade VARCHAR(20) NOT NULL COMMENT '年级',
    capacity INT NOT NULL DEFAULT 30 COMMENT '容量',
    current_count INT NOT NULL DEFAULT 0 COMMENT '当前人数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 插入班级测试数据
INSERT INTO student_class (class_name, grade, capacity, current_count) VALUES 
('计算机科学1班', '2024', 30, 2),
('计算机科学2班', '2024', 32, 1),
('软件工程1班', '2023', 35, 0);

-- 插入学生测试数据
INSERT INTO student (student_number, name, gender, age, phone, email, class_id, enrollment_date) VALUES 
('2024001', '张三', '男', 20, '13800138001', 'zhangsan@example.com', 1, '2024-09-01'),
('2024002', '李四', '女', 19, '13800138002', 'lisi@example.com', 1, '2024-09-01'),
('2024003', '王五', '男', 21, '13800138003', 'wangwu@example.com', 2, '2024-09-01');