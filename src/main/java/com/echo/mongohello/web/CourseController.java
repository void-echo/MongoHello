package com.echo.mongohello.web;

import com.echo.mongohello.dao.CourseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@CrossOrigin
@RequestMapping("/course")
public class CourseController {
    private CourseDao courseDao;

    // get one course by cid.
    @RequestMapping("/getCourseByCid")
    public String getCourseByCid(String cid) {
        return courseDao.findCourseByCid(cid).toString();
    }

    // get all courses having fcid.
    @RequestMapping("/getCoursesByFcid")
    public String getCoursesByFcid(String fcid) {
        return courseDao.findCourseByFcid(fcid).toString();
    }

    // get all courses.
    @RequestMapping("/getAllCourses")
    public String getAllCourses() {
        return courseDao.findAllCourses().toString();
    }

    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }
}
