package com.echo.mongohello.dao;

import com.echo.mongohello.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public List<Teacher> findTeachersByAgeMoreThan(Integer age) {
        Query query = new Query(Criteria.where("age").gt(age));
        return mongoTemplate.find(query, Teacher.class);
    }

    // find all teachers whose sex equals "男"
    public List<Teacher> findAllMaleTeachers() {
        Query query = new Query(Criteria.where("sex").is("男"));
        return mongoTemplate.find(query, Teacher.class);
    }

    public List<Teacher> findMaleTeachersOlderThan(Integer age) {
        // age > 50 && sex = '男'
        Query query = new Query(Criteria.where("age").gt(age).and("sex").is("男"));
        return mongoTemplate.find(query, Teacher.class);
    }

    // find all teachers whose sex equals "女"
    public List<Teacher> findAllFemaleTeachers() {
        Query query = new Query(Criteria.where("sex").is("女"));
        return mongoTemplate.find(query, Teacher.class);
    }

    // find teachers having dname equals given dname.
    public List<Teacher> findAllTeachersOfDname(String dname) {
        Query query = new Query(Criteria.where("dname").is(dname));
        return mongoTemplate.find(query, Teacher.class);
    }

    // find all teachers
    public List<Teacher> findAllTeachers() {
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
