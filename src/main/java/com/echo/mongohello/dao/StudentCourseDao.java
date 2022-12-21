package com.echo.mongohello.dao;


import com.echo.mongohello.entity.Course;
import com.echo.mongohello.entity.StudentCourse;
import com.echo.mongohello.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.awt.desktop.QuitEvent;
import java.util.*;

@Component
public class StudentCourseDao {
    private MongoTemplate mongoTemplate;

    // find all by sid and cid.
    public Iterable<StudentCourse> findStudentCourseBySidAndCid(String sid, String cid) {
        Query query = new Query(Criteria.where("sid").is(sid).and("cid").is(cid));
        return mongoTemplate.find(query, StudentCourse.class);
    }

    // find all student_courses having sid equals given sid.
    public List<StudentCourse> findAllStudentCoursesBySid(String sid) {
        Query query = new Query(Criteria.where("sid").is(sid));
        return mongoTemplate.find(query, StudentCourse.class);
    }

    // find all student_courses having cid equals given cid.
    public List<StudentCourse> findAllStudentCoursesByCid(String cid) {
        Query query = new Query(Criteria.where("cid").is(cid));
        return mongoTemplate.find(query, StudentCourse.class);
    }

    // find all student_courses
    public List<StudentCourse> findAllStudentCourses() {
        return mongoTemplate.findAll(StudentCourse.class);
    }

    // insert a student_course.
    public void insertStudentCourse(StudentCourse studentCourse) {
        mongoTemplate.insert(studentCourse);
    }

    public List<String> findDistinctAllCourseNames() {
        List<String> cids = mongoTemplate.findDistinct(new Query(), "cid", "student_course", String.class);
        Set<String> cidSet = new HashSet<>(cids);
        List<String> courseNames = new ArrayList<>();
        cidSet.forEach((cid) -> {
            Query query = new Query(Criteria.where("cid").is(cid));
            Course course = mongoTemplate.findOne(query, Course.class);
            String one = course == null ? null : course.getName();
            if (one != null) {
                courseNames.add(one);
            }
        });
        return courseNames;
    }

    public List<Map<String, String>> findSCbySidWithTName(String sid) {
        // format: cid, tid, teacher_name, score, course_name
        Query query = new Query(Criteria.where("sid").is(sid));
        List<StudentCourse> studentCourses = mongoTemplate.find(query, StudentCourse.class);
        List<Map<String, String>> result = new ArrayList<>();
        // find teacher name using tid
        studentCourses.forEach((sc) -> {
            var tid = sc.getTid();
            Query query1 = new Query(Criteria.where("tid").is(tid));
            Query query2 = new Query(Criteria.where("cid").is(sc.getCid()));
            Optional<Teacher> teacher = Optional.ofNullable(mongoTemplate.findOne(query1, Teacher.class));
            Optional<Course> course = Optional.ofNullable(mongoTemplate.findOne(query2, Course.class));
            result.add(Map.of(
                    "cid", sc.getCid(),
                    "tid", tid,
                    "teacher_name", teacher.map(Teacher::getName).orElse(""),
                    "score", sc.getScore().toString(),
                    "course_name", course.map(Course::getName).orElse("")));
        });
        return result;
    }

    public List<Course> getAvailableCourse(String sid) {
        var li = findSCbySidWithTName(sid); // Already selected courses
        Set<String> gainedCids = new HashSet<>();
        li.forEach((map) -> {
            gainedCids.add(map.get("cid"));
        });
        List<Course> result = new ArrayList<>();
        // here, we used `findAll`. This action is time-consuming, but it's ok.
        // because the number of courses is not so large, and most of them are not yet selected by the student.
        mongoTemplate.findAll(Course.class).forEach((course) -> {
            if (!gainedCids.contains(course.getCid()) && (course.getFcid() == null || gainedCids.contains(course.getFcid()))) {
                result.add(course);
            }
        });
        return result;
    }

    public boolean insertMany(List<StudentCourse> data) {
        try {
            mongoTemplate.insertAll(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
