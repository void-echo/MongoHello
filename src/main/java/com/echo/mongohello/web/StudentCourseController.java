package com.echo.mongohello.web;

import com.echo.mongohello.dao.StudentCourseDao;
import com.echo.mongohello.entity.StudentCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Component
@RestController
@CrossOrigin
@RequestMapping("/studentCourse")
public class StudentCourseController {
    private StudentCourseDao studentCourseDao;

    // get one studentCourse by sid.
    @RequestMapping("/getStudentCourseBySid")
    public List<StudentCourse> getStudentCourseBySid(String sid) {
        return studentCourseDao.findAllStudentCoursesBySid(sid);
    }

    // get all studentCourses.
    @RequestMapping("/getAllStudentCourses")
    public List<StudentCourse> getAllStudentCourses() {
        return studentCourseDao.findAllStudentCourses();
    }

    // find all student_courses having cid equals given cid.
    @RequestMapping("/findStudentCoursesByCid")
    public List<StudentCourse> findStudentCoursesByCid(String cid) {
        return studentCourseDao.findAllStudentCoursesByCid(cid);
    }

    // findStudentCourseBySidAndCid
    @RequestMapping("/findStudentCourseBySidAndCid")
    public String findStudentCourseBySidAndCid(String sid, String cid) {
        return studentCourseDao.findStudentCourseBySidAndCid(sid, cid).toString();
    }

    @RequestMapping("/insert-many")
    public boolean insertMany(List<StudentCourse> data) {
        return studentCourseDao.insertMany(data);
    }

    @Autowired
    public void setStudentCourseDao(StudentCourseDao studentCourseDao) {
        this.studentCourseDao = studentCourseDao;
    }
}
