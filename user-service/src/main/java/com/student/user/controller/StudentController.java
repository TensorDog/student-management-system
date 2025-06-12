package com.student.user.controller;

import com.student.common.entity.Student;
import com.student.common.dto.StudentDTO;
import com.student.common.result.Result;
import com.student.user.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Student Controller
 * 学生管理控制器
 */
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
@Validated
public class StudentController {
    
    private final StudentService studentService;
    
    /**
     * 获取所有学生列表
     */
    @GetMapping
    public Result<List<Student>> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return Result.success(students);
        } catch (Exception e) {
            log.error("获取学生列表失败", e);
            return Result.error("获取学生列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取学生信息
     */
    @GetMapping("/{id}")
    public Result<Student> getStudentById(@PathVariable("id") @NotNull Long id) {
        try {
            Student student = studentService.getStudentById(id);
            if (student == null) {
                return Result.notFound("学生不存在");
            }
            return Result.success(student);
        } catch (Exception e) {
            log.error("获取学生信息失败，ID: {}", id, e);
            return Result.error("获取学生信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据班级ID获取学生列表
     */
    @GetMapping("/class/{classId}")
    public Result<List<Student>> getStudentsByClassId(@PathVariable("classId") @NotNull Long classId) {
        try {
            List<Student> students = studentService.getStudentsByClassId(classId);
            return Result.success(students);
        } catch (Exception e) {
            log.error("根据班级ID获取学生列表失败，班级ID: {}", classId, e);
            return Result.error("获取班级学生列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据学号获取学生信息
     */
    @GetMapping("/number/{studentNumber}")
    public Result<Student> getStudentByNumber(@PathVariable("studentNumber") @NotNull String studentNumber) {
        try {
            Student student = studentService.getStudentByNumber(studentNumber);
            if (student == null) {
                return Result.notFound("学号不存在");
            }
            return Result.success(student);
        } catch (Exception e) {
            log.error("根据学号获取学生信息失败，学号: {}", studentNumber, e);
            return Result.error("获取学生信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建新学生
     */
    @PostMapping
    public Result<Student> createStudent(@RequestBody @Valid StudentDTO studentDTO) {
        try {
            Student student = studentService.createStudent(studentDTO);
            return Result.success("学生创建成功", student);
        } catch (Exception e) {
            log.error("创建学生失败", e);
            return Result.error("创建学生失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新学生信息
     */
    @PutMapping("/{id}")
    public Result<Student> updateStudent(@PathVariable("id") @NotNull Long id, 
                                       @RequestBody @Valid StudentDTO studentDTO) {
        try {
            Student student = studentService.updateStudent(id, studentDTO);
            if (student == null) {
                return Result.notFound("学生不存在");
            }
            return Result.success("学生信息更新成功", student);
        } catch (Exception e) {
            log.error("更新学生信息失败，ID: {}", id, e);
            return Result.error("更新学生信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除学生
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteStudent(@PathVariable("id") @NotNull Long id) {
        try {
            boolean deleted = studentService.deleteStudent(id);
            if (!deleted) {
                return Result.notFound("学生不存在");
            }
            return Result.success("学生删除成功", null);
        } catch (Exception e) {
            log.error("删除学生失败，ID: {}", id, e);
            return Result.error("删除学生失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除学生
     */
    @DeleteMapping("/batch")
    public Result<Void> deleteStudents(@RequestBody List<Long> ids) {
        try {
            int deletedCount = studentService.deleteStudentsBatch(ids);
            return Result.success("成功删除 " + deletedCount + " 个学生", null);
        } catch (Exception e) {
            log.error("批量删除学生失败", e);
            return Result.error("批量删除学生失败: " + e.getMessage());
        }
    }
    
    /**
     * 搜索学生 (根据姓名或学号)
     */
    @GetMapping("/search")
    public Result<List<Student>> searchStudents(@RequestParam String keyword) {
        try {
            List<Student> students = studentService.searchStudents(keyword);
            return Result.success(students);
        } catch (Exception e) {
            log.error("搜索学生失败，关键词: {}", keyword, e);
            return Result.error("搜索学生失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取学生统计信息
     */
    @GetMapping("/statistics")
    public Result<Object> getStudentStatistics() {
        try {
            Object statistics = studentService.getStudentStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取学生统计信息失败", e);
            return Result.error("获取统计信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试接口
     */
    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("User service is working!");
    }
    
} 