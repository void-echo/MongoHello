package com.echo.mongohello.util;

import com.echo.mongohello.dao.*;
import com.echo.mongohello.entity.Course;
import com.echo.mongohello.entity.Student;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


/**
 * DataInitializer: Init data from xlsx file.
 */
@Component
@RestController
@CrossOrigin
@RequestMapping("/data-init")
public class DataInitializer {
    public final static String xlsxDirPath = "D:\\Onedrive\\桌面\\大三上\\NoSQL\\__tmp__data\\";
    public final static String[] xlsxFileNames = {"student.xlsx", "course.xlsx", "student_course.xlsx", "teacher.xlsx", "teacher_course.xlsx"};

    @RequestMapping("/init")
    public boolean init() throws IOException {

        for (var xlsxFileName : xlsxFileNames) {
            var table_name = xlsxFileName.split("\\.")[0];
            Workbook workbook = new XSSFWorkbook(xlsxDirPath + xlsxFileName);
            // print sheet 0 header
            var sheet = workbook.getSheetAt(0);
            var header = sheet.getRow(0);
            switch (table_name) {
                case "course" -> {
                    return init_course(workbook);
                }
                case "student" -> {
                    return init_student(workbook);
                }
                default -> {
//                    throw new IllegalStateException("Unexpected value: " + table_name);
                }
            }
        }
        return true;

    }

    public boolean init_course(Workbook workbook) {
        // Note that the first row is the header
        var sheet = workbook.getSheetAt(0);
        for (int i = 1; i < sheet.getPhysicalNumberOfRows() + 1; i++) {
            var row = sheet.getRow(i);
            if (row == null) {
                System.out.println("Operate end on a null row " + i + " of sheet " + sheet.getSheetName() + " of workbook " + workbook);
                break;
            }
            var course = new Course();
            var cid = row.getCell(0) == null ? null : row.getCell(0).toString();
            if (cid == null) {
                System.out.println("cid is null");
                continue;
            }
            var name = row.getCell(1) == null ? null : row.getCell(1).toString();
            var fcid = row.getCell(2) == null ? null : row.getCell(2).toString();
            Double credit = row.getCell(3) == null ? null : row.getCell(3).getNumericCellValue();
            course.setCid(cid);
            course.setName(name);
            course.setFcid(fcid);
            course.setCredit(credit);
            courseDao.insertCourse(course);
        }
        return true;
    }


    public boolean init_student(Workbook workbook) {
        var sheet = workbook.getSheetAt(0);
        for (int i = 1; i < sheet.getPhysicalNumberOfRows() + 1; i++) {
            var row = sheet.getRow(i);
            if (row == null) {
                System.out.println("Operate end on a null row " + i + " of sheet " + sheet.getSheetName() + " of workbook " + workbook);
                break;
            }
            // sid, name, sex, age, birthday, dname, class
            var sid = row.getCell(0) == null ? null : row.getCell(0).toString();
            if (sid == null) {
                System.out.println("sid is null");
                continue;
            }
            var name = row.getCell(1) == null ? null : row.getCell(1).toString();
            var sex = row.getCell(2) == null ? null : row.getCell(2).toString();
            Integer age = row.getCell(3) == null ? null : (int) row.getCell(3).getNumericCellValue();
            var birthday = row.getCell(4) == null ? null : row.getCell(4).toString();
            var dname = row.getCell(5) == null ? null : row.getCell(5).toString();
            var class_ = row.getCell(6) == null ? null : row.getCell(6).toString();

            var student = new Student(sid, name, sex, age, birthday, dname, class_);
            studentDao.insertStudent(student);
        }
        return true;
    }

    // every dao as a private member
    private CourseDao courseDao;
    private StudentDao studentDao;
    private TeacherDao teacherDao;
    private StudentCourseDao studentCourseDao;
    private TeacherCourseDao teacherCourseDao;

    // autowire dao
    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Autowired
    public void setStudentCourseDao(StudentCourseDao studentCourseDao) {
        this.studentCourseDao = studentCourseDao;
    }

    @Autowired
    public void setTeacherCourseDao(TeacherCourseDao teacherCourseDao) {
        this.teacherCourseDao = teacherCourseDao;
    }
}
