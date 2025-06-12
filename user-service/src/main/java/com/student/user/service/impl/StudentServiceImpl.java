package com.student.user.service.impl;

import com.student.common.entity.Student;
import com.student.common.dto.StudentDTO;
import com.student.common.utils.DateUtils;
import com.student.user.service.StudentService;
import com.student.user.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Student Service Implementation
 * 学生服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class StudentServiceImpl implements StudentService {
    
    private final StudentMapper studentMapper;
    
    @Override
    public List<Student> getAllStudents() {
        log.info("获取所有学生列表");
        return studentMapper.findAll();
    }
    
    @Override
    public Student getStudentById(Long id) {
        log.info("根据ID获取学生信息: {}", id);
        return studentMapper.findById(id);
    }
    
    @Override
    public Student getStudentByNumber(String studentNumber) {
        log.info("根据学号获取学生信息: {}", studentNumber);
        return studentMapper.findByStudentNumber(studentNumber);
    }
    
    @Override
    public List<Student> getStudentsByClassId(Long classId) {
        log.info("根据班级ID获取学生列表: {}", classId);
        return studentMapper.findByClassId(classId);
    }
    
    @Override
    public Student createStudent(StudentDTO studentDTO) {
        log.info("创建新学生: {}", studentDTO.getName());
        
        // 检查学号是否已存在
        if (isStudentNumberExists(studentDTO.getStudentNumber())) {
            throw new RuntimeException("学号已存在: " + studentDTO.getStudentNumber());
        }
        
        // 转换DTO为Entity
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO, student);
        
        // 设置创建时间和更新时间
        LocalDateTime now = DateUtils.getCurrentDateTime();
        student.setCreateTime(now);
        student.setUpdateTime(now);
        
        // 如果状态为空，设置默认状态
        if (student.getStatus() == null || student.getStatus().trim().isEmpty()) {
            student.setStatus("ACTIVE");
        }
        
        // 插入数据库
        studentMapper.insert(student);
        
        log.info("学生创建成功，ID: {}", student.getId());
        return student;
    }
    
    @Override
    public Student updateStudent(Long id, StudentDTO studentDTO) {
        log.info("更新学生信息，ID: {}", id);
        
        // 检查学生是否存在
        Student existingStudent = studentMapper.findById(id);
        if (existingStudent == null) {
            return null;
        }
        
        // 如果学号有变更，检查新学号是否已存在
        if (!existingStudent.getStudentNumber().equals(studentDTO.getStudentNumber()) 
            && isStudentNumberExists(studentDTO.getStudentNumber())) {
            throw new RuntimeException("学号已存在: " + studentDTO.getStudentNumber());
        }
        
        // 更新字段
        BeanUtils.copyProperties(studentDTO, existingStudent, "id", "createTime");
        existingStudent.setUpdateTime(DateUtils.getCurrentDateTime());
        
        // 更新数据库
        studentMapper.update(existingStudent);
        
        log.info("学生信息更新成功，ID: {}", id);
        return existingStudent;
    }
    
    @Override
    public boolean deleteStudent(Long id) {
        log.info("删除学生，ID: {}", id);
        
        // 检查学生是否存在
        Student student = studentMapper.findById(id);
        if (student == null) {
            return false;
        }
        
        // 删除学生
        int deletedRows = studentMapper.deleteById(id);
        
        log.info("学生删除成功，ID: {}", id);
        return deletedRows > 0;
    }
    
    @Override
    public int deleteStudentsBatch(List<Long> ids) {
        log.info("批量删除学生，数量: {}", ids.size());
        
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        
        int deletedCount = studentMapper.deleteByIds(ids);
        log.info("批量删除学生成功，删除数量: {}", deletedCount);
        return deletedCount;
    }
    
    @Override
    public List<Student> searchStudents(String keyword) {
        log.info("搜索学生，关键词: {}", keyword);
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents();
        }
        
        return studentMapper.searchByKeyword(keyword.trim());
    }
    
    @Override
    public Map<String, Object> getStudentStatistics() {
        log.info("获取学生统计信息");
        
        Map<String, Object> statistics = new HashMap<>();
        
        // 总学生数
        int totalCount = studentMapper.getTotalCount();
        statistics.put("totalCount", totalCount);
        
        // 男女学生数量
        int maleCount = studentMapper.getCountByGender("M");
        int femaleCount = studentMapper.getCountByGender("F");
        statistics.put("maleCount", maleCount);
        statistics.put("femaleCount", femaleCount);
        
        // 各状态学生数量
        int activeCount = studentMapper.getCountByStatus("ACTIVE");
        int graduatedCount = studentMapper.getCountByStatus("GRADUATED");
        int suspendedCount = studentMapper.getCountByStatus("SUSPENDED");
        statistics.put("activeCount", activeCount);
        statistics.put("graduatedCount", graduatedCount);
        statistics.put("suspendedCount", suspendedCount);
        
        // 各班级学生数量
        List<Map<String, Object>> classStats = studentMapper.getCountByClass();
        statistics.put("classStatistics", classStats);
        
        return statistics;
    }
    
    @Override
    public boolean isStudentNumberExists(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            return false;
        }
        
        Student existingStudent = studentMapper.findByStudentNumber(studentNumber);
        return existingStudent != null;
    }
    
    @Override
    public List<Student> getStudentsByStatus(String status) {
        log.info("根据状态获取学生列表: {}", status);
        return studentMapper.findByStatus(status);
    }
    
} 