package com.student.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.DecimalMax;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Score Entity
 * 成绩实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    
    /**
     * 成绩ID (主键)
     */
    private Long id;
    
    /**
     * 学生ID (外键)
     */
    @NotNull(message = "学生ID不能为空")
    private Long studentId;
    
    /**
     * 学生姓名 (冗余字段，用于显示)
     */
    private String studentName;
    
    /**
     * 学号 (冗余字段，用于显示)
     */
    private String studentNumber;
    
    /**
     * 班级ID (外键)
     */
    @NotNull(message = "班级ID不能为空")
    private Long classId;
    
    /**
     * 班级名称 (冗余字段，用于显示)
     */
    private String className;
    
    /**
     * 科目名称
     */
    @NotBlank(message = "科目名称不能为空")
    private String subject;
    
    /**
     * 科目代码
     */
    @NotBlank(message = "科目代码不能为空")
    private String subjectCode;
    
    /**
     * 考试类型 (MIDTERM-期中, FINAL-期末, QUIZ-小测验, ASSIGNMENT-作业)
     */
    @NotBlank(message = "考试类型不能为空")
    private String examType;
    
    /**
     * 成绩分数
     */
    @NotNull(message = "成绩分数不能为空")
    @DecimalMin(value = "0.0", message = "成绩不能小于0分")
    @DecimalMax(value = "100.0", message = "成绩不能大于100分")
    private BigDecimal score;
    
    /**
     * 总分 (默认100分)
     */
    private BigDecimal totalScore = new BigDecimal("100.00");
    
    /**
     * 等级 (A, B, C, D, F)
     */
    private String grade;
    
    /**
     * 是否及格
     */
    private Boolean isPassed;
    
    /**
     * 学期 (如: 2024-1, 2024-2)
     */
    @NotBlank(message = "学期不能为空")
    private String semester;
    
    /**
     * 学年 (如: 2023-2024)
     */
    @NotBlank(message = "学年不能为空")
    private String academicYear;
    
    /**
     * 考试日期
     */
    @NotNull(message = "考试日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime examDate;
    
    /**
     * 录入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 任课教师
     */
    private String teacher;
    
    /**
     * 备注
     */
    private String remarks;
    
    /**
     * 计算成绩等级
     */
    public void calculateGrade() {
        if (score == null) return;
        
        double scoreValue = score.doubleValue();
        if (scoreValue >= 90) {
            this.grade = "A";
        } else if (scoreValue >= 80) {
            this.grade = "B";
        } else if (scoreValue >= 70) {
            this.grade = "C";
        } else if (scoreValue >= 60) {
            this.grade = "D";
        } else {
            this.grade = "F";
        }
        
        this.isPassed = scoreValue >= 60;
    }
    
} 