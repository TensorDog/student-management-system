package com.student.score.mapper;

import com.student.common.entity.Score;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Score Mapper
 * 成绩数据访问层
 */
@Mapper
public interface ScoreMapper {
    
    /**
     * 根据学生ID查询成绩
     * @param studentId 学生ID
     * @return 成绩列表
     */
    @Select("SELECT * FROM scores WHERE student_id = #{studentId} ORDER BY exam_date DESC")
    List<Score> selectByStudentId(@Param("studentId") Long studentId);
    
    /**
     * 根据学生ID和科目查询成绩
     * @param studentId 学生ID
     * @param subject 科目名称
     * @return 成绩列表
     */
    @Select("SELECT * FROM scores WHERE student_id = #{studentId} AND subject = #{subject} ORDER BY exam_date DESC")
    List<Score> selectByStudentIdAndSubject(@Param("studentId") Long studentId, @Param("subject") String subject);
    
    /**
     * 根据班级ID查询成绩
     * @param classId 班级ID
     * @return 成绩列表
     */
    @Select("SELECT * FROM scores WHERE class_id = #{classId} ORDER BY exam_date DESC")
    List<Score> selectByClassId(@Param("classId") Long classId);
    
    /**
     * 根据科目查询成绩
     * @param subject 科目名称
     * @return 成绩列表
     */
    @Select("SELECT * FROM scores WHERE subject = #{subject} ORDER BY exam_date DESC")
    List<Score> selectBySubject(@Param("subject") String subject);
    
    /**
     * 根据学期查询成绩
     * @param semester 学期
     * @return 成绩列表
     */
    @Select("SELECT * FROM scores WHERE semester = #{semester} ORDER BY exam_date DESC")
    List<Score> selectBySemester(@Param("semester") String semester);
    
    /**
     * 根据ID查询成绩
     * @param id 成绩ID
     * @return 成绩信息
     */
    @Select("SELECT * FROM scores WHERE id = #{id}")
    Score selectById(@Param("id") Long id);
    
    /**
     * 分页查询成绩
     * @param offset 偏移量
     * @param size 每页大小
     * @param studentId 学生ID（可选）
     * @param classId 班级ID（可选）
     * @param subject 科目（可选）
     * @param semester 学期（可选）
     * @return 成绩列表
     */
    @Select("<script>" +
            "SELECT * FROM scores " +
            "WHERE 1=1 " +
            "<if test='studentId != null'> AND student_id = #{studentId} </if>" +
            "<if test='classId != null'> AND class_id = #{classId} </if>" +
            "<if test='subject != null and subject != \"\"'> AND subject = #{subject} </if>" +
            "<if test='semester != null and semester != \"\"'> AND semester = #{semester} </if>" +
            "ORDER BY exam_date DESC " +
            "LIMIT #{offset}, #{size}" +
            "</script>")
    List<Score> selectByPage(@Param("offset") int offset, @Param("size") int size,
                           @Param("studentId") Long studentId, @Param("classId") Long classId,
                           @Param("subject") String subject, @Param("semester") String semester);
    
    /**
     * 查询符合条件的成绩总数
     * @param studentId 学生ID（可选）
     * @param classId 班级ID（可选）
     * @param subject 科目（可选）
     * @param semester 学期（可选）
     * @return 总数
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM scores " +
            "WHERE 1=1 " +
            "<if test='studentId != null'> AND student_id = #{studentId} </if>" +
            "<if test='classId != null'> AND class_id = #{classId} </if>" +
            "<if test='subject != null and subject != \"\"'> AND subject = #{subject} </if>" +
            "<if test='semester != null and semester != \"\"'> AND semester = #{semester} </if>" +
            "</script>")
    int countByCondition(@Param("studentId") Long studentId, @Param("classId") Long classId,
                        @Param("subject") String subject, @Param("semester") String semester);
    
    /**
     * 插入成绩
     * @param score 成绩信息
     */
    @Insert("INSERT INTO scores (student_id, student_name, student_number, class_id, class_name, " +
            "subject, subject_code, exam_type, score, total_score, grade, is_passed, " +
            "semester, academic_year, exam_date, create_time, update_time, teacher, remarks) " +
            "VALUES (#{studentId}, #{studentName}, #{studentNumber}, #{classId}, #{className}, " +
            "#{subject}, #{subjectCode}, #{examType}, #{score}, #{totalScore}, #{grade}, #{isPassed}, " +
            "#{semester}, #{academicYear}, #{examDate}, #{createTime}, #{updateTime}, #{teacher}, #{remarks})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Score score);
    
    /**
     * 更新成绩
     * @param score 成绩信息
     */
    @Update("UPDATE scores SET " +
            "student_id = #{studentId}, student_name = #{studentName}, student_number = #{studentNumber}, " +
            "class_id = #{classId}, class_name = #{className}, subject = #{subject}, subject_code = #{subjectCode}, " +
            "exam_type = #{examType}, score = #{score}, total_score = #{totalScore}, grade = #{grade}, " +
            "is_passed = #{isPassed}, semester = #{semester}, academic_year = #{academicYear}, " +
            "exam_date = #{examDate}, update_time = #{updateTime}, teacher = #{teacher}, remarks = #{remarks} " +
            "WHERE id = #{id}")
    void update(Score score);
    
    /**
     * 删除成绩
     * @param id 成绩ID
     */
    @Delete("DELETE FROM scores WHERE id = #{id}")
    void deleteById(@Param("id") Long id);
    
}