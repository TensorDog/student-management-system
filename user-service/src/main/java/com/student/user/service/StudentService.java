package com.student.user.service;

import com.student.common.entity.Student;
import com.student.common.dto.StudentDTO;
import java.util.List;
import java.util.Map;

/**
 * Student Service Interface
 * 学生服务接口
 */
public interface StudentService {
    
    /**
     * 获取所有学生列表
     */
    List<Student> getAllStudents();
    
    /**
     * 根据ID获取学生信息
     */
    Student getStudentById(Long id);
    
    /**
     * 根据学号获取学生信息
     */
    Student getStudentByNumber(String studentNumber);
    
    /**
     * 根据班级ID获取学生列表
     */
    List<Student> getStudentsByClassId(Long classId);
    
    /**
     * 创建新学生
     */
    Student createStudent(StudentDTO studentDTO);
    
    /**
     * 更新学生信息
     */
    Student updateStudent(Long id, StudentDTO studentDTO);
    
    /**
     * 删除学生
     */
    boolean deleteStudent(Long id);
    
    /**
     * 批量删除学生
     */
    int deleteStudentsBatch(List<Long> ids);
    
    /**
     * 搜索学生 (根据姓名或学号)
     */
    List<Student> searchStudents(String keyword);
    
    /**
     * 获取学生统计信息
     */
    Map<String, Object> getStudentStatistics();
    
    /**
     * 检查学号是否已存在
     */
    boolean isStudentNumberExists(String studentNumber);
    
    /**
     * 根据状态获取学生列表
     */
    List<Student> getStudentsByStatus(String status);
    
} 