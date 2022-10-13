package com.echo.mongohello.web;

import com.echo.mongohello.dao.StudentDao;
import com.echo.mongohello.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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

    // insert a student.
    // student has age, class_, dname, name, sid, sex, birthday, sid.
    // sid is necessary. other fields are optional.
    @RequestMapping("/insertStudent")
    public Boolean insertStudent(String sid, Optional<String> class_, Optional<String> name, Optional<String> sex, Optional<Integer> age, Optional<String> birthday, Optional<String> dname) {
        Student student = new Student();
        student.setSid(sid);
        // set values for optional fields.
        class_.ifPresent(student::setClassId);
        name.ifPresent(student::setName);
        age.ifPresent(student::setAge);
        sex.ifPresent(student::setSex);
        birthday.ifPresent(student::setBirthday);
        dname.ifPresent(student::setDname);
        // insert student.
        return studentDao.insertStudent(student);
    }

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }
}
