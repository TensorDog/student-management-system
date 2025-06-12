package com.student.classservice.controller;

import com.student.classservice.service.ClassService;
import com.student.common.entity.StudentClass;
import com.student.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping
    public Result<StudentClass> createClass(@RequestBody StudentClass studentClass) {
        // 设置默认值
        if (studentClass.getCurrentCount() == null) {
            studentClass.setCurrentCount(0);
        }
        return Result.success(classService.createClass(studentClass));
    }

    @PutMapping("/{id}")
    public Result<StudentClass> updateClass(@PathVariable Long id, @RequestBody StudentClass studentClass) {
        studentClass.setId(id);
        return Result.success(classService.updateClass(studentClass));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteClass(@PathVariable Long id) {
        return Result.success(classService.deleteClass(id));
    }

    @GetMapping("/{id}")
    public Result<StudentClass> getClassById(@PathVariable Long id) {
        try {
            StudentClass result = classService.getClassById(id);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("Error getting class by id: " + e.getMessage());
        }
    }

    @GetMapping("/test/{id}")
    public Result<String> testWithId(@PathVariable Long id) {
        return Result.success("Test with ID: " + id);
    }

    @GetMapping
    public Result<List<StudentClass>> getAllClasses() {
        return Result.success(classService.getAllClasses());
    }

    @GetMapping("/grade/{grade}")
    public Result<List<StudentClass>> getClassesByGrade(@PathVariable String grade) {
        return Result.success(classService.getClassesByGrade(grade));
    }

    @GetMapping("/{id}/full")
    public Result<Boolean> isClassFull(@PathVariable Long id) {
        return Result.success(classService.isClassFull(id));
    }

    @GetMapping("/available")
    public Result<List<StudentClass>> getAvailableClasses() {
        return Result.success(classService.getAvailableClasses());
    }
    
    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("Class service is working!");
    }
    
    @GetMapping("/health-check")
    public Result<String> healthCheck() {
        try {
            // 简化测试，不使用 StudentClass
            String testMessage = "Basic service layer working";
            return Result.success(testMessage);
        } catch (Exception e) {
            return Result.error("Service error: " + e.getMessage());
        }
    }
} 