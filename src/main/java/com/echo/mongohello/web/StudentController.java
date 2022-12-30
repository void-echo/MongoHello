package com.echo.mongohello.web;

import com.echo.mongohello.dao.StudentDao;
import com.echo.mongohello.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RestController
@CrossOrigin
@RequestMapping("/student")
public class StudentController {
    private StudentDao studentDao;

    // get one student by sid.
    @RequestMapping("/getStudentBySid")
    public Student getStudentBySid(String sid) {
        return studentDao.findStudentBySid(sid);
    }

    // get all students having age less than 20.
    @RequestMapping("/getStudentsByAgeLessThan")
    public List<Student> getStudentsByAgeLessThan(Optional<Integer> age_) {
        int age = age_.orElse(20);
        return studentDao.findStudentsByAgeLessThan(age);
    }

    // get all students.
    @RequestMapping("/getAllStudents")
    public List<Student> getAllStudents() {
        return studentDao.findAllStudents();
    }

    // get all the names and ages of student.
    @RequestMapping("/getNamesAndAges")
    public List<Map<String, String>> getNamesAndAges() {
        return studentDao.findNamesAndAges();
    }

    @RequestMapping("/findSoftwareAndAgeLessThan")

    public List<Student> findSoftwareAndAgeLessThan(Optional<Integer> age_) {
        int age = age_.orElse(20);
        return studentDao.findSoftwareAndAgeLessThan(age);
    }

    @RequestMapping("/findNameAndSexOfStudent")
    public List<Map<String, String>> findNameAndSexOfStudentAgeLt(Optional<Integer> age_) {
        var age = age_.orElse(10000);
        return studentDao.findNameAndSexOfStudentAgeLt(age);
    }

    @RequestMapping(value = "/insert-many", method = RequestMethod.POST)
    public void insertMany(@RequestBody List<Map<String, Object>> list) {
        studentDao.insertMany(list);
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

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateOne(@RequestBody List<Map<String, Object>> list) {
        System.out.println("GOT: ");
        System.out.println(list);
        studentDao.updateMany(list);
    }

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }
}
