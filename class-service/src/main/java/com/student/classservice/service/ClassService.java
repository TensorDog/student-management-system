package com.student.classservice.service;

import com.student.common.entity.StudentClass;
import java.util.List;

public interface ClassService {
    // Create a new class
    StudentClass createClass(StudentClass studentClass);
    
    // Update an existing class
    StudentClass updateClass(StudentClass studentClass);
    
    // Delete a class by ID
    boolean deleteClass(Long id);
    
    // Get a class by ID
    StudentClass getClassById(Long id);
    
    // Get all classes
    List<StudentClass> getAllClasses();
    
    // Get classes by grade
    List<StudentClass> getClassesByGrade(String grade);
    
    // Check if a class is full
    boolean isClassFull(Long classId);
    
    // Get available classes (not full)
    List<StudentClass> getAvailableClasses();
} 