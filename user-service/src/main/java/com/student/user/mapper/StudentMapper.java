package com.student.user.mapper;

import com.student.common.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Student Mapper Interface
 * 学生数据访问层接口
 */
@Mapper
public interface StudentMapper {
    
    /**
     * 查询所有学生
     */
    List<Student> findAll();
    
    /**
     * 根据ID查询学生
     */
    Student findById(@Param("id") Long id);
    
    /**
     * 根据学号查询学生
     */
    Student findByStudentNumber(@Param("studentNumber") String studentNumber);
    
    /**
     * 根据班级ID查询学生列表
     */
    List<Student> findByClassId(@Param("classId") Long classId);
    
    /**
     * 根据状态查询学生列表
     */
    List<Student> findByStatus(@Param("status") String status);
    
    /**
     * 插入新学生
     */
    int insert(Student student);
    
    /**
     * 更新学生信息
     */
    int update(Student student);
    
    /**
     * 根据ID删除学生
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 批量删除学生
     */
    int deleteByIds(@Param("ids") List<Long> ids);
    
    /**
     * 根据关键词搜索学生 (姓名或学号)
     */
    List<Student> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 获取学生总数
     */
    int getTotalCount();
    
    /**
     * 根据性别统计学生数量
     */
    int getCountByGender(@Param("gender") String gender);
    
    /**
     * 根据状态统计学生数量
     */
    int getCountByStatus(@Param("status") String status);
    
    /**
     * 统计各班级学生数量
     */
    List<Map<String, Object>> getCountByClass();
    
    /**
     * 根据年级统计学生数量
     */
    List<Map<String, Object>> getCountByGrade();
    
    /**
     * 根据入学年份统计学生数量
     */
    List<Map<String, Object>> getCountByEnrollmentYear();
    
} 