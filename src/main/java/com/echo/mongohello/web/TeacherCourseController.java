package com.echo.mongohello.web;

import com.echo.mongohello.dao.TeacherCourseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@CrossOrigin
@RequestMapping("/teacherCourse")
public class TeacherCourseController {
    private TeacherCourseDao teacherCourseDao;

    // get teacherCourses by tid.
    @RequestMapping("/getTeacherCourseByTid")
    public String getTeacherCourseByTid(String tid) {
        return teacherCourseDao.findAllTeacherCoursesByTid(tid).toString();
    }

    // findTeacherCourseByTidAndCid
    @RequestMapping("/findTeacherCourseByTidAndCid")
    public String findTeacherCourseByTidAndCid(String tid, String cid) {
        return teacherCourseDao.findTeacherCourseByTidAndCid(tid, cid).toString();
    }

    // get all teacherCourses.
    @RequestMapping("/getAllTeacherCourses")
    public String getAllTeacherCourses() {
        return teacherCourseDao.findAllTeacherCourses().toString();
    }

    // find all teacher_courses having cid equals given cid.
    @RequestMapping("/findTeacherCoursesByCid")
    public String findTeacherCoursesByCid(String cid) {
        return teacherCourseDao.findAllTeacherCoursesByCid(cid).toString();
    }

    @Autowired
    public void setTeacherCourseDao(TeacherCourseDao teacherCourseDao) {
        this.teacherCourseDao = teacherCourseDao;
    }
}
