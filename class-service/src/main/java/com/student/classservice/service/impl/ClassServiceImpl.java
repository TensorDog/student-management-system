package com.student.classservice.service.impl;

import com.student.classservice.service.ClassService;
import com.student.common.entity.StudentClass;
import com.student.classservice.mapper.ClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Override
    @Transactional
    public StudentClass createClass(StudentClass studentClass) {
        classMapper.insert(studentClass);
        return studentClass;
    }

    @Override
    @Transactional
    public StudentClass updateClass(StudentClass studentClass) {
        classMapper.update(studentClass);
        return studentClass;
    }

    @Override
    @Transactional
    public boolean deleteClass(Long id) {
        return classMapper.deleteById(id) > 0;
    }

    @Override
    public StudentClass getClassById(Long id) {
        return classMapper.selectById(id);
    }

    @Override
    public List<StudentClass> getAllClasses() {
        return classMapper.selectAll();
    }

    @Override
    public List<StudentClass> getClassesByGrade(String grade) {
        return classMapper.selectByGrade(grade);
    }

    @Override
    public boolean isClassFull(Long classId) {
        // 临时返回false，等StudentClass类修复后再实现
        return false;
    }

    @Override
    public List<StudentClass> getAvailableClasses() {
        return classMapper.selectAvailableClasses();
    }
} 