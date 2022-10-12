package com.echo.mongohello.web;

import com.echo.mongohello.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@CrossOrigin
@RequestMapping("/student")
public class StudentController {
    private StudentDao studentDao;

    // get one student by sid.
    @RequestMapping("/getStudentBySid")
    public String getStudentBySid(String sid) {
        return studentDao.findStudentBySid(sid).toString();
    }

    // get all students having age less than 20.
    @RequestMapping("/getStudentsByAgeLessThan")
    public String getStudentsByAgeLessThan(Integer age) {
        return studentDao.findStudentsByAgeLessThan(age).toString();
    }

    // get all students.
    @RequestMapping("/getAllStudents")
    public String getAllStudents() {
        return studentDao.findAllStudents().toString();
    }

    // get all the names and ages of student.
    @RequestMapping("/getNamesAndAges")
    public String getNamesAndAges() {
        return studentDao.findNamesAndAges().toString();
    }


    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }
}
