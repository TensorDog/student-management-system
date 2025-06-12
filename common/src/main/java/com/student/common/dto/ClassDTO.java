package com.student.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

/**
 * Class Data Transfer Object
 * 班级数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassDTO {
    /** 班级ID */
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
} 