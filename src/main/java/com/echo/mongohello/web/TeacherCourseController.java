package com.echo.mongohello.web;

import com.echo.mongohello.dao.TeacherCourseDao;
import com.echo.mongohello.entity.TeacherCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Component
@RestController
@CrossOrigin
@RequestMapping("/teacherCourse")
public class TeacherCourseController {
    private TeacherCourseDao teacherCourseDao;

    // get teacherCourses by tid.
    @RequestMapping("/getTeacherCourseByTid")
    public List<TeacherCourse> getTeacherCourseByTid(String tid) {
        return teacherCourseDao.findAllTeacherCoursesByTid(tid);
    }

    // findTeacherCourseByTidAndCid
    @RequestMapping("/findTeacherCourseByTidAndCid")
    public TeacherCourse findTeacherCourseByTidAndCid(String tid, String cid) {
        return teacherCourseDao.findTeacherCourseByTidAndCid(tid, cid);
    }

    // get all teacherCourses.
    @RequestMapping("/getAllTeacherCourses")
    public List<TeacherCourse> getAllTeacherCourses() {
        return teacherCourseDao.findAllTeacherCourses();
    }

    // find all teacher_courses having cid equals given cid.
    @RequestMapping("/findTeacherCoursesByCid")
    public List<TeacherCourse> findTeacherCoursesByCid(String cid) {
        return teacherCourseDao.findAllTeacherCoursesByCid(cid);
    }

    @Autowired
    public void setTeacherCourseDao(TeacherCourseDao teacherCourseDao) {
        this.teacherCourseDao = teacherCourseDao;
    }
}
