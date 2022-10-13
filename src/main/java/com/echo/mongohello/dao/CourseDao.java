package com.echo.mongohello.dao;

import com.echo.mongohello.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CourseDao {
    private MongoTemplate mongoTemplate;

    // find one by cid.
    public Course findCourseByCid(String cid) {
        Query query = new Query(Criteria.where("cid").is(cid));
        return mongoTemplate.findOne(query, Course.class);
    }

    // find course by fcid.
    public Iterable<Course> findCourseByFcid(String fcid) {
        Query query = new Query(Criteria.where("fcid").is(fcid));
        return mongoTemplate.find(query, Course.class);
    }

    // find all courses
    public Iterable<Course> findAllCourses() {
        return mongoTemplate.findAll(Course.class);
    }

    // insert one course.

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
