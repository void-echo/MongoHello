package com.echo.mongohello.dao;


import com.echo.mongohello.entity.Course;
import com.echo.mongohello.entity.StudentCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class StudentCourseDao {
    private MongoTemplate mongoTemplate;

    // find all by sid and cid.
    public Iterable<StudentCourse> findStudentCourseBySidAndCid(String sid, String cid) {
        Query query = new Query(Criteria.where("sid").is(sid).and("cid").is(cid));
        return mongoTemplate.find(query, StudentCourse.class);
    }

    // find all student_courses having sid equals given sid.
    public Iterable<StudentCourse> findAllStudentCoursesBySid(String sid) {
        Query query = new Query(Criteria.where("sid").is(sid));
        return mongoTemplate.find(query, StudentCourse.class);
    }

    // find all student_courses having cid equals given cid.
    public Iterable<StudentCourse> findAllStudentCoursesByCid(String cid) {
        Query query = new Query(Criteria.where("cid").is(cid));
        return mongoTemplate.find(query, StudentCourse.class);
    }

    // find all student_courses
    public Iterable<StudentCourse> findAllStudentCourses() {
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

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
