package com.echo.mongohello.web;

import com.echo.mongohello.dao.CourseDao;
import com.echo.mongohello.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RestController
@CrossOrigin
@RequestMapping("/course")
public class CourseController {
    private CourseDao courseDao;

    // get one course by cid.
    @RequestMapping("/getCourseByCid")
    public Course getCourseByCid(String cid) {
        return courseDao.findCourseByCid(cid);
    }

    // get all courses having fcid.
    @RequestMapping("/getCoursesByFcid")
    public List<Course> getCoursesByFcid(Optional<String> fcid_) {
        var fcid = fcid_.orElse("300001");
        return courseDao.findCourseByFcid(fcid);
    }

    @RequestMapping(value = "/insert-many", method = RequestMethod.POST)
    public void insertMany(@RequestBody List<Map<String, Object>> list) {
        courseDao.insertMany(list);
    }

    // get all courses.
    @RequestMapping("/getAllCourses")
    public List<Course> getAllCourses() {
        return courseDao.findAllCourses();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateOne(@RequestBody List<Map<String, Object>> list) {
        courseDao.updateMany(list);
    }

    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }


}
