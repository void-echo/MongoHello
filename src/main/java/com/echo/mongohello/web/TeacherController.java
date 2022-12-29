package com.echo.mongohello.web;


import com.echo.mongohello.dao.TeacherDao;
import com.echo.mongohello.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Component
@RestController
@CrossOrigin
@RequestMapping("/teacher")
public class TeacherController {
    private TeacherDao teacherDao;

    // get one teacher by tid.
    @RequestMapping("/getTeacherByTid")
    public Teacher getTeacherByTid(String tid) {
        return teacherDao.findTeacherByTid(tid);
    }

    // get all teachers.
    @RequestMapping("/getAllTeachers")
    public List<Teacher> getAllTeachers() {
        return teacherDao.findAllTeachers();
    }

    // get all teachers having age more than given age.
    @RequestMapping("/getTeachersByAgeMoreThan")
    public List<Teacher> getTeachersByAgeLessThan(Integer age) {
        return teacherDao.findTeachersByAgeMoreThan(age);
    }

    // find all male teachers
    @RequestMapping("/getAllMaleTeachers")
    public List<Teacher> getAllMaleTeachers() {
        return teacherDao.findAllMaleTeachers();
    }

    // find all female teachers
    @RequestMapping("/getAllFemaleTeachers")
    public List<Teacher> getAllFemaleTeachers() {
        return teacherDao.findAllFemaleTeachers();
    }

    @RequestMapping("/findMaleTeachersOlderThan")
    public List<Teacher> findMaleTeachersOlderThan(Optional<Integer> age_) {
        var age = age_.orElse(50);
        return teacherDao.findMaleTeachersOlderThan(age);
    }

    // find teachers having dname equals given dname.
    @RequestMapping("/getAllTeachersOfDname")
    public List<Teacher> getTeachersByDname(Optional<String> dname_) {
        var dname = dname_.orElse("计算机科学与技术学院");
        return teacherDao.findAllTeachersOfDname(dname);
    }

    // insert one teacher. if the teacher already exists, return false. otherwise, return true.

    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }
}
