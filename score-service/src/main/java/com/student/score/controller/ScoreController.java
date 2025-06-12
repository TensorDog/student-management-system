package com.student.score.controller;

import com.student.common.entity.Score;
import com.student.score.service.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Score Controller
 * 成绩管理控制器
 */
@RestController
@RequestMapping("/api/scores")
@Slf4j
public class ScoreController {
    
    @Autowired
    private ScoreService scoreService;
    
    /**
     * 根据学生ID查询成绩
     * @param studentId 学生ID
     * @return 成绩列表
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Score>> getScoresByStudentId(@PathVariable Long studentId) {
        log.info("查询学生成绩，学生ID: {}", studentId);
        List<Score> scores = scoreService.getScoresByStudentId(studentId);
        return ResponseEntity.ok(scores);
    }
    
    /**
     * 根据学生ID和科目查询成绩
     * @param studentId 学生ID
     * @param subject 科目名称
     * @return 成绩列表
     */
    @GetMapping("/student/{studentId}/subject/{subject}")
    public ResponseEntity<List<Score>> getScoresByStudentIdAndSubject(
            @PathVariable Long studentId, 
            @PathVariable String subject) {
        log.info("查询学生科目成绩，学生ID: {}, 科目: {}", studentId, subject);
        List<Score> scores = scoreService.getScoresByStudentIdAndSubject(studentId, subject);
        return ResponseEntity.ok(scores);
    }
    
    /**
     * 根据班级ID查询成绩
     * @param classId 班级ID
     * @return 成绩列表
     */
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Score>> getScoresByClassId(@PathVariable Long classId) {
        log.info("查询班级成绩，班级ID: {}", classId);
        List<Score> scores = scoreService.getScoresByClassId(classId);
        return ResponseEntity.ok(scores);
    }
    
    /**
     * 根据科目查询成绩
     * @param subject 科目名称
     * @return 成绩列表
     */
    @GetMapping("/subject/{subject}")
    public ResponseEntity<List<Score>> getScoresBySubject(@PathVariable String subject) {
        log.info("查询科目成绩，科目: {}", subject);
        List<Score> scores = scoreService.getScoresBySubject(subject);
        return ResponseEntity.ok(scores);
    }
    
    /**
     * 根据学期查询成绩
     * @param semester 学期
     * @return 成绩列表
     */
    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<Score>> getScoresBySemester(@PathVariable String semester) {
        log.info("查询学期成绩，学期: {}", semester);
        List<Score> scores = scoreService.getScoresBySemester(semester);
        return ResponseEntity.ok(scores);
    }
    
    /**
     * 根据ID查询单个成绩
     * @param id 成绩ID
     * @return 成绩信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Score> getScoreById(@PathVariable Long id) {
        log.info("查询成绩详情，成绩ID: {}", id);
        Score score = scoreService.getScoreById(id);
        return ResponseEntity.ok(score);
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
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getScoresPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String semester) {
        log.info("分页查询成绩，页码: {}, 大小: {}, 学生ID: {}, 班级ID: {}, 科目: {}, 学期: {}", 
                page, size, studentId, classId, subject, semester);
        Map<String, Object> result = scoreService.getScoresPage(page, size, studentId, classId, subject, semester);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 添加成绩
     * @param score 成绩信息
     * @return 操作结果
     */
    @PostMapping
    public ResponseEntity<String> addScore(@Valid @RequestBody Score score) {
        log.info("添加成绩: {}", score);
        scoreService.addScore(score);
        return ResponseEntity.ok("成绩添加成功");
    }
    
    /**
     * 更新成绩
     * @param id 成绩ID
     * @param score 成绩信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateScore(@PathVariable Long id, @Valid @RequestBody Score score) {
        log.info("更新成绩，ID: {}, 信息: {}", id, score);
        score.setId(id);
        scoreService.updateScore(score);
        return ResponseEntity.ok("成绩更新成功");
    }
    
    /**
     * 删除成绩
     * @param id 成绩ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteScore(@PathVariable Long id) {
        log.info("删除成绩，ID: {}", id);
        scoreService.deleteScore(id);
        return ResponseEntity.ok("成绩删除成功");
    }
    
    /**
     * 获取学生成绩统计
     * @param studentId 学生ID
     * @return 成绩统计信息
     */
    @GetMapping("/student/{studentId}/statistics")
    public ResponseEntity<Map<String, Object>> getStudentScoreStatistics(@PathVariable Long studentId) {
        log.info("获取学生成绩统计，学生ID: {}", studentId);
        Map<String, Object> statistics = scoreService.getStudentScoreStatistics(studentId);
        return ResponseEntity.ok(statistics);
    }
    
}