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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

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
    public List<Student> findStudentsByAgeLessThan(Integer age) {
        Query query = new Query(Criteria.where("age").lt(age));
        return mongoTemplate.find(query, Student.class);
    }

    // find all students
    public List<Student> findAllStudents() {
        return mongoTemplate.findAll(Student.class);
    }

    // find all the names and ages of student
    public List<Map<String, String>> findNamesAndAges() {
        List<Map<String, String>> li = new ArrayList<>();
        Query query = new Query(Criteria.where("name").exists(true).and("age").exists(true));
        // return in this format:
        // [{"name": "Tom","age": 18},{"name": "Jerry","age": 19}]
        List<Student> students = mongoTemplate.find(query, Student.class);
        for (Student student : students) {
            li.add(Map.of("name", student.getName(), "age", student.getAge().toString()));
        }
        return li;
    }

    public List<Student> findSoftwareAndAgeLessThan(Integer age) {
        Query query = new Query(Criteria.where("age").lt(age).and("dname").is("软件学院"));
        return mongoTemplate.find(query, Student.class);
    }

    public List<Map<String, String>> findNameAndSexOfStudentAgeLt(Integer age) {
        Query query = new Query(Criteria.where("age").lt(age));
        List<Map<String, String>> li = new ArrayList<>();
        List<Student> students = mongoTemplate.find(query, Student.class);
        for (Student student : students) {
            Map<String, String> map = new HashMap<>();
            map.put("name", student.getName());
            map.put("sex", student.getSex());
            li.add(map);
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

    /**
     * Note that this method costs a lot of time, and we added `limit 100` for saving time.
     */
    public List<Map<String, Object>> findEachBestCourseScoreAndName() {
        // items: { sid, student_name, course_name, score }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("sid").max("score").as("bestScore"),
                Aggregation.limit(100));             // limit 100 for saving time.
        var results = mongoTemplate.aggregate(aggregation, "student_course", Map.class);
        List<Map<String, Object>> list = new ArrayList<>();
        results.forEach((tinySet) -> {
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

    public List<Map<String, Object>> findMaxScoreOfEveryCourse() {
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("cid").max("score").as("max_score"));
        var results = mongoTemplate.aggregate(aggregation, "student_course", Map.class);
        List<Map<String, Object>> list = new ArrayList<>();
        for (var map : results) {
            Query query = new Query(Criteria.where("cid").is(map.get("_id")));
            Course course = mongoTemplate.findOne(query, Course.class, "course");
            if (course == null) {
                continue;
            }
            Query query1 = new Query(Criteria.where("score").is(map.get("max_score")).and("cid").is(map.get("_id")));
            StudentCourse sc = mongoTemplate.findOne(query1, StudentCourse.class, "student_course");
            assert sc != null;
            Query query2 = new Query(Criteria.where("sid").is(sc.getSid()));
            Student student = mongoTemplate.findOne(query2, Student.class, "student");
            Map<String, Object> record = new HashMap<>();

            record.put("cid", map.get("_id"));
            record.put("cname", course.getName());
            record.put("max_score", map.get("max_score"));
            assert student != null;
            record.put("student_name", student.getName());
            list.add(record);
        }
        return list;
    }

    public List<Map<String, Object>> eachCourseEnrollNumAndAvgScore() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("cid").count().as("count").avg("score").as("avgScore"));
        var results = mongoTemplate.aggregate(aggregation, "student_course", Map.class);
        List<Map<String, Object>> list = new ArrayList<>();
        results.forEach((tinySet) -> {
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

    /**
     * @return [
     * {
     * "A": 1,
     * "B": 1,
     * "C": 2,
     * "D": 1,
     * "name": "韩娟娟"
     * }, {...}, ..., {...}]
     */
    public List<Map<String, Object>> findScoreDistribution() {
        Aggregation aggregationA = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("score").gte(85)),
                Aggregation.group("sid").count().as("A"));
        var resultsA = mongoTemplate.aggregate(aggregationA, "student_course", Map.class);
        var aggregationB = Aggregation.newAggregation(Aggregation.match(Criteria.where("score").lt(85).gte(75)),
                Aggregation.group("sid").count().as("B"));
        var resultsB = mongoTemplate.aggregate(aggregationB, "student_course", Map.class);
        var aggregationC = Aggregation.newAggregation(Aggregation.match(Criteria.where("score").lt(75).gte(60)),
                Aggregation.group("sid").count().as("C"));
        var resultsC = mongoTemplate.aggregate(aggregationC, "student_course", Map.class);
        var aggregationD = Aggregation.newAggregation(Aggregation.match(Criteria.where("score").lt(60)),
                Aggregation.group("sid").count().as("D"));
        var resultsD = mongoTemplate.aggregate(aggregationD, "student_course", Map.class);
        Map<String, Map<String, Object>> tem = new HashMap<>();
        List<Student> students = mongoTemplate.findAll(Student.class, "student");
        for (var student : students) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", student.getName());
            tem.put(student.getSid(), hashMap);
        }
        resultsA.forEach((tinySet) -> {
            String id = (String) tinySet.get("_id");
            if (tem.get(id) != null) {
                tem.get(id).put("A", tinySet.get("A"));
            }
        });
        resultsB.forEach((tinySet) -> {
            String id = (String) tinySet.get("_id");
            if (tem.get(id) != null) {
                tem.get(id).put("B", tinySet.get("B"));
            }
        });
        resultsC.forEach((tinySet) -> {
            String id = (String) tinySet.get("_id");
            if (tem.get(id) != null) {
                tem.get(id).put("C", tinySet.get("C"));
            }
        });
        resultsD.forEach((tinySet) -> {
            String id = (String) tinySet.get("_id");
            if (tem.get(id) != null) {
                tem.get(id).put("D", tinySet.get("D"));
            }
        });
        List<Map<String, Object>> list = new ArrayList<>();
        for (var entry : tem.entrySet()) {
            list.add(entry.getValue());
        }
        for (var stringObjectMap : list) {
            stringObjectMap.putIfAbsent("A", 0);
            stringObjectMap.putIfAbsent("B", 0);
            stringObjectMap.putIfAbsent("C", 0);
            stringObjectMap.putIfAbsent("D", 0);
        }
        return list;
    }

    public List<Map<String, Object>> findTopTenAvgScoreCourses() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("cid").avg("score").as("avgScore"),
                Aggregation.sort(Sort.Direction.DESC, "avgScore"), Aggregation.limit(10));
        var results = mongoTemplate.aggregate(aggregation, "student_course", Map.class);
        List<Map<String, Object>> list = new ArrayList<>();
        for (var map : results) {
            Query query = new Query(Criteria.where("cid").is(map.get("_id")));
            Course course = mongoTemplate.findOne(query, Course.class, "course");
            Map<String, Object> newMap = new HashMap<>();
            if (course == null) {
                continue;
            }
            newMap.put("cid", map.get("_id"));
            newMap.put("cname", course.getName());
            newMap.put("avgScore", map.get("avgScore"));
            list.add(newMap);
        }
        return list;
    }

    public void insertMany(List<Map<String, Object>> list) {
        mongoTemplate.insert(list, "student");
    }

    public void updateOne(Map<String, Object> map) {
        Query query = new Query(Criteria.where("sid").is(map.get("sid")));
        Update update = new Update();
        for (var entry : map.entrySet()) {
            var key = entry.getKey();
            if (key.equals("classId")) {
                key = "class";
            }
            update.set(key, entry.getValue());
        }
        mongoTemplate.updateFirst(query, update, "student");
    }

    public void updateMany(List<Map<String, Object>> list) {
        list.forEach(this::updateOne);
    }

    public List<Map<String, Object>> findTopTenEnrollCourses() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("cid").count().as("count"),
                Aggregation.sort(Sort.Direction.DESC, "count"), Aggregation.limit(10));
        var results = mongoTemplate.aggregate(aggregation, "student_course", Map.class);
        List<Map<String, Object>> list = new ArrayList<>();
        for (var map : results) {
            Query query = new Query(Criteria.where("cid").is(map.get("_id")));
            var course = mongoTemplate.findOne(query, Course.class, "course");
            Map<String, Object> newMap = new HashMap<>();
            if (course == null) {
                continue;
            }
            newMap.put("cid", map.get("_id"));
            newMap.put("cname", course.getName());
            newMap.put("count", map.get("count"));
            list.add(newMap);
        }
        return list;
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
