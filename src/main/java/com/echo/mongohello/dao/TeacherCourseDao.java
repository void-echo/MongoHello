package com.echo.mongohello.dao;

import com.echo.mongohello.entity.TeacherCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class TeacherCourseDao {
    private MongoTemplate mongoTemplate;

    // find one by tid and cid.
    public TeacherCourse findTeacherCourseByTidAndCid(String tid, String cid) {
        Query query = new Query(Criteria.where("tid").is(tid).and("cid").is(cid));
        return mongoTemplate.findOne(query, TeacherCourse.class);
    }

    // find all teacher_courses having tid equals given tid.
    public Iterable<TeacherCourse> findAllTeacherCoursesByTid(String tid) {
        Query query = new Query(Criteria.where("tid").is(tid));
        return mongoTemplate.find(query, TeacherCourse.class);
    }

    // find all teacher_courses having cid equals given cid.
    public Iterable<TeacherCourse> findAllTeacherCoursesByCid(String cid) {
        Query query = new Query(Criteria.where("cid").is(cid));
        return mongoTemplate.find(query, TeacherCourse.class);
    }

    // find all teacher_courses
    public Iterable<TeacherCourse> findAllTeacherCourses() {
        return mongoTemplate.findAll(TeacherCourse.class);
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
