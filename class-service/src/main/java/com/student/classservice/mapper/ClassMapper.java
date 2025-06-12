package com.student.classservice.mapper;

import com.student.common.entity.StudentClass;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ClassMapper {
    int insert(StudentClass studentClass);
    
    int update(StudentClass studentClass);
    
    int deleteById(Long id);
    
    StudentClass selectById(Long id);
    
    List<StudentClass> selectAll();
    
    List<StudentClass> selectByGrade(String grade);
    
    List<StudentClass> selectAvailableClasses();
} 