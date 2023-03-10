package com.echo.mongohello.dao;

import com.echo.mongohello.entity.TeacherCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeacherCourseDao {
    private MongoTemplate mongoTemplate;

    // find one by tid and cid.
    public TeacherCourse findTeacherCourseByTidAndCid(String tid, String cid) {
        Query query = new Query(Criteria.where("tid").is(tid).and("cid").is(cid));
        return mongoTemplate.findOne(query, TeacherCourse.class);
    }

    // find all teacher_courses having tid equals given tid.
    public List<TeacherCourse> findAllTeacherCoursesByTid(String tid) {
        Query query = new Query(Criteria.where("tid").is(tid));
        return mongoTemplate.find(query, TeacherCourse.class);
    }

    // find all teacher_courses having cid equals given cid.
    public List<TeacherCourse> findAllTeacherCoursesByCid(String cid) {
        Query query = new Query(Criteria.where("cid").is(cid));
        return mongoTemplate.find(query, TeacherCourse.class);
    }

    public boolean insertTeacherCourse(TeacherCourse teacherCourse) {
        if (findTeacherCourseByTidAndCid(teacherCourse.getTid(), teacherCourse.getCid()) != null) {
            return false;
        }
        mongoTemplate.insert(teacherCourse);
        return true;
    }

    // find all teacher_courses
    public List<TeacherCourse> findAllTeacherCourses() {
        return mongoTemplate.findAll(TeacherCourse.class);
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
