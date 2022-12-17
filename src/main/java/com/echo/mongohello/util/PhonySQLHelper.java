package com.echo.mongohello.util;

import com.echo.mongohello.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@CrossOrigin
@RequestMapping("/sql")
public class PhonySQLHelper {
    MongoTemplate mongoTemplate;

    @RequestMapping("/delete-table")
    public boolean deleteAllRowsOfTable(String table_name) {
        // delete content of table, but not the table itself.
        switch (table_name) {
            case "course" -> mongoTemplate.dropCollection(Course.class);
            case "student" -> mongoTemplate.dropCollection(Student.class);
            case "student_course" -> mongoTemplate.dropCollection(StudentCourse.class);
            case "teacher" -> mongoTemplate.dropCollection(Teacher.class);
            case "teacher_course" -> mongoTemplate.dropCollection(TeacherCourse.class);
            default -> {
                return false;
            }
        }
        return true;
    }

    @RequestMapping("/create-table")
    public boolean createTable(String table_name) {
        // create table.
        switch (table_name) {
            case "course" -> mongoTemplate.createCollection(Course.class);
            case "student" -> mongoTemplate.createCollection(Student.class);
            case "student_course" -> mongoTemplate.createCollection(StudentCourse.class);
            case "teacher" -> mongoTemplate.createCollection(Teacher.class);
            case "teacher_course" -> mongoTemplate.createCollection(TeacherCourse.class);
            default -> {
                return false;
            }
        }
        return true;
    }

    @RequestMapping("clear-table")
    // drop table and create it again.
    public boolean clearTable(String table_name) {
        boolean b = deleteAllRowsOfTable(table_name);
        if (!b) {
            return false;
        }
        return createTable(table_name);
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
