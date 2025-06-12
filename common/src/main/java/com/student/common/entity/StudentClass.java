package com.student.common.entity;

import java.sql.Timestamp;

/**
 * Student Class Entity
 * 班级实体类
 */
public class StudentClass {
    /** 班级ID (主键) */
    private Long id;
    /** 班级名称 */
    private String className;
    /** 年级 */
    private String grade;
    /** 容量 */
    private Integer capacity;
    /** 当前人数 */
    private Integer currentCount;
    /** 创建时间 */
    private Timestamp createdAt;
    /** 更新时间 */
    private Timestamp updatedAt;

    // 无参构造函数
    public StudentClass() {}

    // 全参构造函数
    public StudentClass(Long id, String className, String grade, Integer capacity, 
                       Integer currentCount, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.className = className;
        this.grade = grade;
        this.capacity = capacity;
        this.currentCount = currentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Integer currentCount) {
        this.currentCount = currentCount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "StudentClass{" +
                "id=" + id +
                ", className='" + className + '\'' +
                ", grade='" + grade + '\'' +
                ", capacity=" + capacity +
                ", currentCount=" + currentCount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
} 