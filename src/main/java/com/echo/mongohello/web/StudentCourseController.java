package com.echo.mongohello.web;

import com.echo.mongohello.dao.StudentCourseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@CrossOrigin
@RequestMapping("/studentCourse")
public class StudentCourseController {
    private StudentCourseDao studentCourseDao;

    // get one studentCourse by sid.
    @RequestMapping("/getStudentCourseBySid")
    public String getStudentCourseBySid(String sid) {
        return studentCourseDao.findAllStudentCoursesBySid(sid).toString();
    }

    // get all studentCourses.
    @RequestMapping("/getAllStudentCourses")
    public String getAllStudentCourses() {
        return studentCourseDao.findAllStudentCourses().toString();
    }

    // find all student_courses having cid equals given cid.
    @RequestMapping("/findStudentCoursesByCid")
    public String findStudentCoursesByCid(String cid) {
        return studentCourseDao.findAllStudentCoursesByCid(cid).toString();
    }

    @Autowired
    public void setStudentCourseDao(StudentCourseDao studentCourseDao) {
        this.studentCourseDao = studentCourseDao;
    }
}
