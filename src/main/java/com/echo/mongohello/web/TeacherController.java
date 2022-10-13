package com.echo.mongohello.web;


import com.echo.mongohello.dao.TeacherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@CrossOrigin
@RequestMapping("/teacher")
public class TeacherController {
    private TeacherDao teacherDao;

    // get one teacher by tid.
    @RequestMapping("/getTeacherByTid")
    public String getTeacherByTid(String tid) {
        return teacherDao.findTeacherByTid(tid).toString();
    }

    // get all teachers.
    @RequestMapping("/getAllTeachers")
    public String getAllTeachers() {
        return teacherDao.findAllTeachers().toString();
    }

    // get all teachers having age more than given age.
    @RequestMapping("/getTeachersByAgeMoreThan")
    public String getTeachersByAgeLessThan(Integer age) {
        return teacherDao.findTeachersByAgeMoreThan(age).toString();
    }

    // find all male teachers
    @RequestMapping("/getAllMaleTeachers")
    public String getAllMaleTeachers() {
        return teacherDao.findAllMaleTeachers().toString();
    }

    // find all female teachers
    @RequestMapping("/getAllFemaleTeachers")
    public String getAllFemaleTeachers() {
        return teacherDao.findAllFemaleTeachers().toString();
    }

    // find teachers having dname equals given dname.
    @RequestMapping("/getAllTeachersOfDname")
    public String getTeachersByDname(String dname) {
        return teacherDao.findAllTeachersOfDname(dname).toString();
    }

    // insert one teacher. if the teacher already exists, return false. otherwise, return true.

    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }
}
