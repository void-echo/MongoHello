package com.echo.mongohello.dao;

import com.echo.mongohello.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class TeacherDao {
    // dao class for teacher in mongoDB.
    // the name of the collection is "teacher".
    // TODO: add more methods.
    private MongoTemplate mongoTemplate;

    // find one by tid.
    public Teacher findTeacherByTid(String tid) {
        Query query = new Query(Criteria.where("tid").is(tid));
        return mongoTemplate.findOne(query, Teacher.class);
    }

    // find all teachers having age more than given age.
    public Iterable<Teacher> findTeachersByAgeMoreThan(Integer age) {
        Query query = new Query(Criteria.where("age").gt(age));
        return mongoTemplate.find(query, Teacher.class);
    }

    // find all teachers whose sex equals "M"
    public Iterable<Teacher> findAllMaleTeachers() {
        Query query = new Query(Criteria.where("sex").is("M"));
        return mongoTemplate.find(query, Teacher.class);
    }

    // find all teachers whose sex equals "F"
    public Iterable<Teacher> findAllFemaleTeachers() {
        Query query = new Query(Criteria.where("sex").is("F"));
        return mongoTemplate.find(query, Teacher.class);
    }

    // find teachers having dname equals given dname.
    public Iterable<Teacher> findAllTeachersOfDname(String dname) {
        Query query = new Query(Criteria.where("dname").is(dname));
        return mongoTemplate.find(query, Teacher.class);
    }

    // find all teachers
    public Iterable<Teacher> findAllTeachers() {
        return mongoTemplate.findAll(Teacher.class);
    }

    // insert one teacher. if the teacher already exists, return false. otherwise, return true.
    public boolean insertTeacher(Teacher teacher) {
        if (findTeacherByTid(teacher.getTid()) != null) {
            return false;
        }
        mongoTemplate.insert(teacher);
        return true;
    }
    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


}
