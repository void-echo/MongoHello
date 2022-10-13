package com.echo.mongohello.dao;


import com.echo.mongohello.entity.StudentCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

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

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
