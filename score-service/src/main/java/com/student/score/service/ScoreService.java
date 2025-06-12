package com.student.score.service;

import com.student.common.entity.Score;
import com.student.score.mapper.ScoreMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Score Service
 * 成绩业务逻辑服务
 */
@Service
@Slf4j
public class ScoreService {
    
    @Autowired
    private ScoreMapper scoreMapper;
    
    /**
     * 根据学生ID查询成绩
     * @param studentId 学生ID
     * @return 成绩列表
     */
    public List<Score> getScoresByStudentId(Long studentId) {
        log.info("查询学生成绩，学生ID: {}", studentId);
        return scoreMapper.selectByStudentId(studentId);
    }
    
    /**
     * 根据学生ID和科目查询成绩
     * @param studentId 学生ID
     * @param subject 科目名称
     * @return 成绩列表
     */
    public List<Score> getScoresByStudentIdAndSubject(Long studentId, String subject) {
        log.info("查询学生科目成绩，学生ID: {}, 科目: {}", studentId, subject);
        return scoreMapper.selectByStudentIdAndSubject(studentId, subject);
    }
    
    /**
     * 根据班级ID查询成绩
     * @param classId 班级ID
     * @return 成绩列表
     */
    public List<Score> getScoresByClassId(Long classId) {
        log.info("查询班级成绩，班级ID: {}", classId);
        return scoreMapper.selectByClassId(classId);
    }
    
    /**
     * 根据科目查询成绩
     * @param subject 科目名称
     * @return 成绩列表
     */
    public List<Score> getScoresBySubject(String subject) {
        log.info("查询科目成绩，科目: {}", subject);
        return scoreMapper.selectBySubject(subject);
    }
    
    /**
     * 根据学期查询成绩
     * @param semester 学期
     * @return 成绩列表
     */
    public List<Score> getScoresBySemester(String semester) {
        log.info("查询学期成绩，学期: {}", semester);
        return scoreMapper.selectBySemester(semester);
    }
    
    /**
     * 根据ID查询单个成绩
     * @param id 成绩ID
     * @return 成绩信息
     */
    public Score getScoreById(Long id) {
        log.info("查询成绩详情，成绩ID: {}", id);
        return scoreMapper.selectById(id);
    }
    
    /**
     * 分页查询成绩
     * @param page 页码
     * @param size 每页大小
     * @param studentId 学生ID（可选）
     * @param classId 班级ID（可选）
     * @param subject 科目（可选）
     * @param semester 学期（可选）
     * @return 成绩分页数据
     */
    public Map<String, Object> getScoresPage(Integer page, Integer size, Long studentId, 
                                           Long classId, String subject, String semester) {
        log.info("分页查询成绩，页码: {}, 大小: {}, 学生ID: {}, 班级ID: {}, 科目: {}, 学期: {}", 
                page, size, studentId, classId, subject, semester);
        
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 查询数据
        List<Score> scores = scoreMapper.selectByPage(offset, size, studentId, classId, subject, semester);
        
        // 查询总数
        int total = scoreMapper.countByCondition(studentId, classId, subject, semester);
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("data", scores);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", (int) Math.ceil((double) total / size));
        
        return result;
    }
    
    /**
     * 添加成绩
     * @param score 成绩信息
     */
    @Transactional
    public void addScore(Score score) {
        log.info("添加成绩: {}", score);
        
        // 设置创建时间
        score.setCreateTime(LocalDateTime.now());
        score.setUpdateTime(LocalDateTime.now());
        
        // 计算等级和是否及格
        score.calculateGrade();
        
        scoreMapper.insert(score);
    }
    
    /**
     * 更新成绩
     * @param score 成绩信息
     */
    @Transactional
    public void updateScore(Score score) {
        log.info("更新成绩: {}", score);
        
        // 设置更新时间
        score.setUpdateTime(LocalDateTime.now());
        
        // 计算等级和是否及格
        score.calculateGrade();
        
        scoreMapper.update(score);
    }
    
    /**
     * 删除成绩
     * @param id 成绩ID
     */
    @Transactional
    public void deleteScore(Long id) {
        log.info("删除成绩，ID: {}", id);
        scoreMapper.deleteById(id);
    }
    
    /**
     * 获取学生成绩统计
     * @param studentId 学生ID
     * @return 成绩统计信息
     */
    public Map<String, Object> getStudentScoreStatistics(Long studentId) {
        log.info("获取学生成绩统计，学生ID: {}", studentId);
        
        List<Score> scores = scoreMapper.selectByStudentId(studentId);
        
        if (scores.isEmpty()) {
            Map<String, Object> emptyResult = new HashMap<>();
            emptyResult.put("totalSubjects", 0);
            emptyResult.put("averageScore", 0);
            emptyResult.put("maxScore", 0);
            emptyResult.put("minScore", 0);
            emptyResult.put("passedSubjects", 0);
            emptyResult.put("passRate", 0);
            return emptyResult;
        }
        
        // 计算统计信息
        int totalSubjects = scores.size();
        BigDecimal totalScore = scores.stream()
                .map(Score::getScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal averageScore = totalScore.divide(new BigDecimal(totalSubjects), 2, RoundingMode.HALF_UP);
        
        BigDecimal maxScore = scores.stream()
                .map(Score::getScore)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        
        BigDecimal minScore = scores.stream()
                .map(Score::getScore)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        
        long passedSubjects = scores.stream()
                .filter(score -> score.getIsPassed() != null && score.getIsPassed())
                .count();
        
        double passRate = (double) passedSubjects / totalSubjects * 100;
        
        // 构建返回结果
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalSubjects", totalSubjects);
        statistics.put("averageScore", averageScore);
        statistics.put("maxScore", maxScore);
        statistics.put("minScore", minScore);
        statistics.put("passedSubjects", passedSubjects);
        statistics.put("passRate", Math.round(passRate * 100.0) / 100.0);
        
        return statistics;
    }
    
}