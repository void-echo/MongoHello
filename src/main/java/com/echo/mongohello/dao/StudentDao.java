package com.echo.mongohello.dao;

import com.echo.mongohello.entity.Course;
import com.echo.mongohello.entity.Student;
import com.echo.mongohello.entity.StudentCourse;
import com.echo.mongohello.entity.advanced.StudentAndGPA;
import com.echo.mongohello.entity.advanced.StudentAndSelectedCourseNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;


@Component
public class StudentDao {
    // StudentDao is an interface for Student entity.
    // the name of the collection is "student".


    private MongoTemplate mongoTemplate;


    // find one by sid.
    public Student findStudentBySid(String sid) {
        Query query = new Query(Criteria.where("sid").is(sid));
        return mongoTemplate.findOne(query, Student.class);
    }

    // find all students having age less than given age.
    public Iterable<Student> findStudentsByAgeLessThan(Integer age) {
        Query query = new Query(Criteria.where("age").lt(age));
        return mongoTemplate.find(query, Student.class);
    }

    // find all students
    public Iterable<Student> findAllStudents() {
        return mongoTemplate.findAll(Student.class);
    }

    // find all the names and ages of student
    public List<String> findNamesAndAges() {
        List<String> li = new ArrayList<>();
        Query query = new Query(Criteria.where("name").exists(true).and("age").exists(true));
        // return in this format:
        // [{"name":"Tom","age":18},{"name":"Jerry","age":19}]
        List<Student> students = mongoTemplate.find(query, Student.class);
        for (Student student : students) {
            li.add("{name: " + student.getName() + ", age: " + student.getAge() + "}");
        }
        return li;
    }

    // insert one student. if the student already exists, return false. otherwise, return true.
    public boolean insertStudent(Student student) {
        if (findStudentBySid(student.getSid()) != null) {
            return false;
        }
        mongoTemplate.insert(student);
        return true;
    }

    // get all student that have a certain name, and return a list of student
    public List<Student> findStudentByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.find(query, Student.class);
    }

    public List<StudentAndGPA> findTop10ByOrderByGpaDesc() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("sid").avg("score").as("gpa"),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "gpa")),
                Aggregation.limit(10)
        );
        // results is a list of {_id=200900132747, gpa=94.4}-like objects.
        var results = mongoTemplate.aggregate(aggregation, "student_course", Map.class);
        List<StudentAndGPA> studentAndGPAList = new ArrayList<>();
        results.forEach((tinySet) -> {
            System.out.println(tinySet);
            Query query = new Query(Criteria.where("sid").is(tinySet.get("_id")));
            Student student = mongoTemplate.findOne(query, Student.class, "student");
            if (student == null || student.getName() == null) {
                return;
            }
            StudentAndGPA studentAndGPA = new StudentAndGPA(student, (Double) tinySet.get("gpa"));
            studentAndGPAList.add(studentAndGPA);
        });

        return studentAndGPAList;
    }

    /**
     * In fact there may be more than 10 students have the same class number 10.
     */
    public List<StudentAndSelectedCourseNum> findBusyStudentTop10() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("sid").count().as("selectedCourseNum"),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "selectedCourseNum")),
                Aggregation.limit(10)
        );
        // results is a list of {_id=200900132747, selectedCourseNum=123}-like objects.
        var results = mongoTemplate.aggregate(aggregation, "student_course", Map.class);
        List<StudentAndSelectedCourseNum> studentAndSelectedCourseNumList = new ArrayList<>();
        results.forEach((tinySet) -> {
            System.out.println(tinySet);
            Query query = new Query(Criteria.where("sid").is(tinySet.get("_id")));
            Student student = mongoTemplate.findOne(query, Student.class, "student");
            if (student == null || student.getName() == null) {
                return;
            }
            StudentAndSelectedCourseNum studentAndSelectedCourseNum = new StudentAndSelectedCourseNum(student, (Integer) tinySet.get("selectedCourseNum"));
            studentAndSelectedCourseNumList.add(studentAndSelectedCourseNum);
        });

        return studentAndSelectedCourseNumList;
    }

    public List<Map<String, Object>> findEachBestCourseScoreAndName() {
        // items: { sid, student_name, course_name, score }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("sid").max("score").as("bestScore"),
                Aggregation.limit(100));
        var results = mongoTemplate.aggregate(aggregation, "student_course", Map.class);
        List<Map<String, Object>> list = new ArrayList<>();
        results.forEach((tinySet) -> {
            System.out.println(tinySet);
            Query query = new Query(Criteria.where("sid").is(tinySet.get("_id")));
            Student student = mongoTemplate.findOne(query, Student.class, "student");
            Query q1 = new Query(Criteria.where("score").is(tinySet.get("bestScore")).and("sid").is(tinySet.get("_id")));
            StudentCourse studentCourse = mongoTemplate.findOne(q1, StudentCourse.class, "student_course");
            Query q2 = new Query(Criteria.where("cid").is(Objects.requireNonNull(studentCourse).getCid()));

            Course course = mongoTemplate.findOne(q2, Course.class, "course");
            Map<String, Object> map = new HashMap<>();
            if (student == null || student.getName() == null || course == null) {
                return;
            }
            map.put("sid", student.getSid());
            map.put("student_name", student.getName());
            map.put("course_name", course.getName());
            map.put("score", tinySet.get("bestScore"));
            list.add(map);
        });
        return list;
    }

    public List<Map<String, Object>> eachCourseEnrollNumAndAvgScore() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("cid").count().as("count").avg("score").as("avgScore"));
        var results = mongoTemplate.aggregate(aggregation, "student_course", Map.class);
        List<Map<String, Object>> list = new ArrayList<>();
        results.forEach((tinySet) -> {
            System.out.println(tinySet);
            Query query = new Query(Criteria.where("cid").is(tinySet.get("_id")));
            Course course = mongoTemplate.findOne(query, Course.class, "course");
            Map<String, Object> map = new HashMap<>();
            if (course == null) {
                return;
            }
            map.put("cid", tinySet.get("_id"));
            map.put("course_name", course.getName());
            map.put("count", tinySet.get("count"));
            map.put("avgScore", tinySet.get("avgScore"));
            list.add(map);
        });
        return list;
    }


    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
