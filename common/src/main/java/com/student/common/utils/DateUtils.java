package com.student.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Date Utility Class
 * 日期工具类
 */
public class DateUtils {
    
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    
    /**
     * 格式化日期为字符串
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return null;
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * 格式化日期时间为字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DATETIME_FORMATTER);
    }
    
    /**
     * 解析日期字符串
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }
    
    /**
     * 解析日期时间字符串
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) return null;
        return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
    }
    
    /**
     * 获取当前日期
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
    
    /**
     * 获取当前日期时间
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
    
    /**
     * 计算年龄
     */
    public static int calculateAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }
    
    /**
     * 计算两个日期之间的天数
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return 0;
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    
    /**
     * 判断是否为同一年
     */
    public static boolean isSameYear(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) return false;
        return date1.getYear() == date2.getYear();
    }
    
    /**
     * 获取学年字符串 (如: 2023-2024)
     */
    public static String getAcademicYear(LocalDate date) {
        if (date == null) return null;
        
        int year = date.getYear();
        int month = date.getMonthValue();
        
        // 如果是9月之前，认为是上一学年
        if (month < 9) {
            return (year - 1) + "-" + year;
        } else {
            return year + "-" + (year + 1);
        }
    }
    
    /**
     * 获取学期字符串 (如: 2024-1, 2024-2)
     */
    public static String getSemester(LocalDate date) {
        if (date == null) return null;
        
        int year = date.getYear();
        int month = date.getMonthValue();
        
        // 9月-1月为第一学期，2月-8月为第二学期
        if (month >= 9 || month <= 1) {
            return year + "-1";
        } else {
            return year + "-2";
        }
    }
    
} 