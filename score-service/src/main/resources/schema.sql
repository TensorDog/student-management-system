-- 创建成绩数据库
CREATE DATABASE IF NOT EXISTS score_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE score_db;

-- 创建成绩表
CREATE TABLE IF NOT EXISTS scores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '成绩ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    student_name VARCHAR(50) COMMENT '学生姓名',
    student_number VARCHAR(20) COMMENT '学号',
    class_id BIGINT NOT NULL COMMENT '班级ID',
    class_name VARCHAR(50) COMMENT '班级名称',
    subject VARCHAR(50) NOT NULL COMMENT '科目名称',
    subject_code VARCHAR(20) NOT NULL COMMENT '科目代码',
    exam_type VARCHAR(20) NOT NULL COMMENT '考试类型(MIDTERM-期中, FINAL-期末, QUIZ-小测验, ASSIGNMENT-作业)',
    score DECIMAL(5,2) NOT NULL COMMENT '成绩分数',
    total_score DECIMAL(5,2) DEFAULT 100.00 COMMENT '总分',
    grade VARCHAR(2) COMMENT '等级(A, B, C, D, F)',
    is_passed BOOLEAN COMMENT '是否及格',
    semester VARCHAR(10) NOT NULL COMMENT '学期(如: 2024-1, 2024-2)',
    academic_year VARCHAR(20) NOT NULL COMMENT '学年(如: 2023-2024)',
    exam_date DATETIME NOT NULL COMMENT '考试日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    teacher VARCHAR(50) COMMENT '任课教师',
    remarks TEXT COMMENT '备注',
    
    INDEX idx_student_id (student_id),
    INDEX idx_class_id (class_id),
    INDEX idx_subject (subject),
    INDEX idx_semester (semester),
    INDEX idx_exam_date (exam_date),
    INDEX idx_student_subject (student_id, subject),
    INDEX idx_class_subject (class_id, subject)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成绩表';

-- 插入示例数据
INSERT INTO scores (student_id, student_name, student_number, class_id, class_name, subject, subject_code, exam_type, score, total_score, grade, is_passed, semester, academic_year, exam_date, teacher, remarks) VALUES
(1, '张三', '2024001', 1, '计算机科学与技术1班', '数据结构', 'CS101', 'MIDTERM', 85.5, 100.00, 'B', TRUE, '2024-1', '2023-2024', '2024-03-15 09:00:00', '李老师', '期中考试'),
(1, '张三', '2024001', 1, '计算机科学与技术1班', '高等数学', 'MATH101', 'MIDTERM', 92.0, 100.00, 'A', TRUE, '2024-1', '2023-2024', '2024-03-20 14:00:00', '王老师', '期中考试'),
(1, '张三', '2024001', 1, '计算机科学与技术1班', '英语', 'ENG101', 'MIDTERM', 78.5, 100.00, 'C', TRUE, '2024-1', '2023-2024', '2024-03-25 10:00:00', '刘老师', '期中考试'),
(2, '李四', '2024002', 1, '计算机科学与技术1班', '数据结构', 'CS101', 'MIDTERM', 76.0, 100.00, 'C', TRUE, '2024-1', '2023-2024', '2024-03-15 09:00:00', '李老师', '期中考试'),
(2, '李四', '2024002', 1, '计算机科学与技术1班', '高等数学', 'MATH101', 'MIDTERM', 88.0, 100.00, 'B', TRUE, '2024-1', '2023-2024', '2024-03-20 14:00:00', '王老师', '期中考试'),
(3, '王五', '2024003', 2, '软件工程1班', '数据库原理', 'CS201', 'MIDTERM', 90.5, 100.00, 'A', TRUE, '2024-1', '2023-2024', '2024-03-18 09:00:00', '陈老师', '期中考试'),
(3, '王五', '2024003', 2, '软件工程1班', '软件工程', 'SE101', 'MIDTERM', 87.0, 100.00, 'B', TRUE, '2024-1', '2023-2024', '2024-03-22 14:00:00', '张老师', '期中考试');