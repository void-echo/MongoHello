package com.echo.mongohello.dao;

import com.echo.mongohello.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@Component
public class StudentDao {
    // StudentDao is an interface for Student entity.
    // the name of the collection is "student".


    private MongoTemplate mongoTemplate;


    // find one by sid.
    public Student findStudentBySid(String sid) {
        Query query = new Query(Criteria.where("sid").is(sid));
        return mongoTemplate.findOne(query, Student.class);
    }

    // find all students having age less than given age.
    public Iterable<Student> findStudentsByAgeLessThan(Integer age) {
        Query query = new Query(Criteria.where("age").lt(age));
        return mongoTemplate.find(query, Student.class);
    }

    // find all students
    public Iterable<Student> findAllStudents() {
        return mongoTemplate.findAll(Student.class);
    }

    // find all the names and ages of student
    public List<String> findNamesAndAges() {
        List<String> li = new ArrayList<>();
        Query query = new Query(Criteria.where("name").exists(true).and("age").exists(true));
        // return in this format:
        // [{"name":"Tom","age":18},{"name":"Jerry","age":19}]
        List<Student> students = mongoTemplate.find(query, Student.class);
        for (Student student : students) {
            li.add("{name: " + student.getName() + ", age: " + student.getAge() + "}");
        }
        return li;
    }

    // insert one student. if the student already exists, return false. otherwise, return true.
    public boolean insertStudent(Student student) {
        if (findStudentBySid(student.getSid()) != null) {
            return false;
        }
        mongoTemplate.insert(student);
        return true;
    }

    // get all student that have a certain name, and return a list of student
    public List<Student> findStudentByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.find(query, Student.class);
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
