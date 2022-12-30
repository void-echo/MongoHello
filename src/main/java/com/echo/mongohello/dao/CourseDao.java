package com.echo.mongohello.dao;

import com.echo.mongohello.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CourseDao {
    private MongoTemplate mongoTemplate;

    // find one by cid.
    public Course findCourseByCid(String cid) {
        Query query = new Query(Criteria.where("cid").is(cid));
        return mongoTemplate.findOne(query, Course.class);
    }

    // find course by fcid.
    public List<Course> findCourseByFcid(String fcid) {
        Query query = new Query(Criteria.where("fcid").is(fcid));
        return mongoTemplate.find(query, Course.class);
    }

    // find all courses
    public List<Course> findAllCourses() {
        return mongoTemplate.findAll(Course.class);
    }

    // insert one course. if the course already exists, return false. otherwise, return true.
    public boolean insertCourse(Course course) {
        if (findCourseByCid(course.getCid()) != null) {
            return false;
        }
        mongoTemplate.insert(course);
        return true;
    }

    public void insertMany(List<Map<String, Object>> list) {
        mongoTemplate.insert(list, "course");
    }

    /**
     * @param map Json format: {cid: "xxx", name: "xxx", credit: 3, fcid: "xxx"}
     * Note that this method will change the `_id` of the course.
     */
    public void updateOne(Map<String, Object> map) {
        Query query = new Query(Criteria.where("cid").is(map.get("cid")));
        Update update = new Update();
        for (var entry : map.entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }
        mongoTemplate.updateFirst(query, update, Course.class);
    }

    public void updateMany(List<Map<String, Object>> list) {
        list.forEach(this::updateOne);
    }
    // insert one course.

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
