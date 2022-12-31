package com.echo.mongohello.web;

import com.echo.mongohello.dao.*;
import com.echo.mongohello.entity.Course;
import com.echo.mongohello.entity.StudentCourse;
import com.echo.mongohello.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CommentedOutCode")
@Component
@RestController
@CrossOrigin
@RequestMapping("/course-selection")
public class CourseSelection {
    MongoTemplate mongoTemplate;
    StudentDao studentDao;
    StudentCourseDao studentCourseDao;
    TeacherDao teacherDao;
    CourseDao courseDao;
    TeacherCourseDao teacherCourseDao;

    @RequestMapping(value = "/get-by-sid", method = RequestMethod.GET)
    public List<Map<String, Object>> getCourseBySid(@RequestParam String sid) {
        // cid cname tid tname score
        var sc_list = studentCourseDao.findAllStudentCoursesBySid(sid);
        List<Map<String, Object>> result = new ArrayList<>();
        for (StudentCourse sc : sc_list) {
            Map<String, Object> map = new HashMap<>();
            map.put("cid", sc.getCid());
            map.put("tid", sc.getTid());
            map.put("cname", courseDao.findCourseByCid(sc.getCid()).getName());
            Teacher teacher = teacherDao.findTeacherByTid(sc.getTid());
            if (teacher != null) {
                map.put("tname", teacher.getName());
            }
            map.put("score", sc.getScore());
            result.add(map);
        }
        return result;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public void insertStudent_Course(@RequestBody List<Map<String, Object>> request) {
        System.out.println(request);
        studentCourseDao.insertMany(request);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateStudent_Course(@RequestBody List<Map<String, Object>> request) {
        System.out.println(request);
        studentCourseDao.updateMany(request);
    }

    @RequestMapping(value = "/get-available-courses", method = RequestMethod.GET)
    public List<Course> getOptionalCourse(@RequestParam String sid) {
        // cid cname credit
        var sc_list = studentCourseDao.findAllStudentCoursesBySid(sid);
        List<Course> result = new ArrayList<>();
        for (var sc : sc_list) {
            result.addAll(courseDao.findCourseByFcid(sc.getCid()));
        }
        result.addAll(courseDao.findCourseByFcid(null));
        //        for (var course : result) {
        //            List<TeacherCourse> map = teacherCourseDao.findAllTeacherCoursesByCid(course.getCid());
        //            if (map != null) {
        //                course.put("tid", map.get("tid"));
        //            }
        //        }
        return result;
    }


    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Autowired
    public void setStudentCourseDao(StudentCourseDao studentCourseDao) {
        this.studentCourseDao = studentCourseDao;
    }

    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Autowired
    public void setTeacherCourseDao(TeacherCourseDao teacherCourseDao) {
        this.teacherCourseDao = teacherCourseDao;
    }

}
