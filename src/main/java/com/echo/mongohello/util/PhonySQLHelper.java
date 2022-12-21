package com.echo.mongohello.util;

import com.echo.mongohello.dao.*;
import com.echo.mongohello.entity.*;
import com.echo.mongohello.entity.advanced.StudentAndGPA;
import com.echo.mongohello.entity.advanced.StudentAndSelectedCourseNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author echo
 * This class is used to run some easy SQLs and for data analysis(lab 6).
 */
@Component
@RestController
@CrossOrigin
@RequestMapping("/sql")
public class PhonySQLHelper {
    MongoTemplate mongoTemplate;
    private CourseDao courseDao;
    private StudentDao studentDao;
    private TeacherDao teacherDao;
    private StudentCourseDao studentCourseDao;
    private TeacherCourseDao teacherCourseDao;

    @RequestMapping("/delete-table")
    public boolean deleteAllRowsOfTable(String table_name) {
        // delete content of table, but not the table itself.
        switch (table_name) {
            case "course" -> mongoTemplate.dropCollection(Course.class);
            case "student" -> mongoTemplate.dropCollection(Student.class);
            case "student_course" -> mongoTemplate.dropCollection(StudentCourse.class);
            case "teacher" -> mongoTemplate.dropCollection(Teacher.class);
            case "teacher_course" -> mongoTemplate.dropCollection(TeacherCourse.class);
            default -> {
                return false;
            }
        }
        return true;
    }

    @RequestMapping("/create-table")
    public boolean createTable(String table_name) {
        // create table.
        switch (table_name) {
            case "course" -> mongoTemplate.createCollection(Course.class);
            case "student" -> mongoTemplate.createCollection(Student.class);
            case "student_course" -> mongoTemplate.createCollection(StudentCourse.class);
            case "teacher" -> mongoTemplate.createCollection(Teacher.class);
            case "teacher_course" -> mongoTemplate.createCollection(TeacherCourse.class);
            default -> {
                return false;
            }
        }
        return true;
    }

    @RequestMapping("/clear-table")
    // drop table and create it again.
    public boolean clearTable(String table_name) {
        boolean b = deleteAllRowsOfTable(table_name);
        if (!b) {
            return false;
        }
        return createTable(table_name);
    }

    @RequestMapping("/find-distinct-all-course-names-in-student-course")
    public List<String> findDistinctAllCourseNamesInStudentCourse() {
        return studentCourseDao.findDistinctAllCourseNames();
    }

    @RequestMapping("/gpa-top-10-students")
    public List<StudentAndGPA> gpaTop10Students() {
        return studentDao.findTop10ByOrderByGpaDesc();
    }

    @RequestMapping("/busy-student-top-10")
    public List<StudentAndSelectedCourseNum> busyStudentTop10() {
        return studentDao.findBusyStudentTop10();
    }

    @RequestMapping("/find-each-best-course-score-and-name")
    public List<Map<String, Object>> findEachBestCourseScoreAndName() {
        return studentDao.findEachBestCourseScoreAndName();
    }

    @RequestMapping("/each-course-enroll-student-num-and-avg-score")
    public List<Map<String, Object>> eachCourseEnrollNumAndAvgScore() {
        return studentDao.eachCourseEnrollNumAndAvgScore();
    }

    @RequestMapping("/find-score-distribution")
    public List<Map<String, Object>> findScoreDistribution() {
        return studentDao.findScoreDistribution();
    }

    @RequestMapping("/find-top-10-avg-score-courses")
    public List<Map<String, Object>> findTopTenAvgScoreCourses() {
        return studentDao.findTopTenAvgScoreCourses();
    }

    @RequestMapping("/find-top-10-enroll-courses")
    public List<Map<String, Object>> findTopTenEnrollCourses() {
        return studentDao.findTopTenEnrollCourses();
    }

    @RequestMapping("/find-sc-by-sid-with-t-name")
    public List<Map<String, String>> findSCbySidWithTName(String sid) {
        return studentCourseDao.findSCbySidWithTName(sid);
    }

    @RequestMapping("/get-available-courses-by-sid")
    public List<Course> getAvailableCourse(String sid) {
        return studentCourseDao.getAvailableCourse(sid);
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // autowire dao
    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Autowired
    public void setStudentCourseDao(StudentCourseDao studentCourseDao) {
        this.studentCourseDao = studentCourseDao;
    }

    @Autowired
    public void setTeacherCourseDao(TeacherCourseDao teacherCourseDao) {
        this.teacherCourseDao = teacherCourseDao;
    }

}
