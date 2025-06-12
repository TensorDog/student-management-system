package com.student.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * Student Data Transfer Object
 * 学生数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    
    /**
     * 学生ID
     */
    private Long id;
    
    /**
     * 学生姓名
     */
    @NotBlank(message = "学生姓名不能为空")
    private String name;
    
    /**
     * 学号
     */
    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^[0-9]{8,12}$", message = "学号必须是8-12位数字")
    private String studentNumber;
    
    /**
     * 性别 (M-男, F-女)
     */
    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^[MF]$", message = "性别只能是M或F")
    private String gender;
    
    /**
     * 出生日期
     */
    @NotNull(message = "出生日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    
    /**
     * 手机号码
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;
    
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 家庭住址
     */
    private String address;
    
    /**
     * 班级ID
     */
    @NotNull(message = "班级不能为空")
    private Long classId;
    
    /**
     * 入学日期
     */
    @NotNull(message = "入学日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate enrollmentDate;
    
} 