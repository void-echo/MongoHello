package com.echo.mongohello.util;

import com.echo.mongohello.dao.*;
import com.echo.mongohello.entity.*;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * DataInitializer: Init data from xlsx file.
 */
@SuppressWarnings({"DuplicatedCode", "BooleanMethodIsAlwaysInverted", "FieldCanBeLocal", "unused"})
@Component
@RestController
@CrossOrigin
@RequestMapping("/data-init")
public class DataInitializer {
    private CourseDao courseDao;
    private StudentDao studentDao;
    private TeacherDao teacherDao;
    private StudentCourseDao studentCourseDao;
    private TeacherCourseDao teacherCourseDao;

    private DataFormatter dataFormatter;
    public final static String xlsxDirPath = "src/main/resources/data/";
    public final static String[] xlsxFileNames = {"student.xlsx", "course.xlsx", "student_course.xlsx", "teacher.xlsx", "teacher_course.xlsx"};

    /**
     * @return true if all tables are initialized successfully.
     * DO NOT run this method if data is already put into database.
     */
    @RequestMapping("/init")
    public boolean init(Optional<String> table) {
        AtomicBoolean flag = new AtomicBoolean(true);
        table.ifPresentOrElse((table_name) -> {
            var xlsxFileName = table_name + ".xlsx";
            Workbook workbook;
            try {
                workbook = new XSSFWorkbook(xlsxDirPath + xlsxFileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // print sheet 0 header

            switch (table_name) {
                case "course" -> {
                    if (!init_course(workbook)) flag.set(false);
                }
                case "student" -> {
                    if (!init_student(workbook)) flag.set(false);
                }
                case "student_course" -> {
                    if (!init_student_course(workbook)) flag.set(false);
                }
                case "teacher" -> {
                    if (!init_teacher(workbook)) flag.set(false);
                }
                case "teacher_course" -> {
                    if (!init_teacher_course(workbook)) flag.set(false);
                }
            }
        }, () -> {
            for (var xlsxFileName : xlsxFileNames) {
                var table_name = xlsxFileName.split("\\.")[0];
                Workbook workbook;
                try {
                    workbook = new XSSFWorkbook(xlsxDirPath + xlsxFileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // print sheet 0 header

                switch (table_name) {
                    case "course" -> {
                        if (!init_course(workbook)) flag.set(false);
                    }
                    case "student" -> {
                        if (!init_student(workbook)) flag.set(false);
                    }
                    case "student_course" -> {
                        if (!init_student_course(workbook)) flag.set(false);
                    }
                    case "teacher" -> {
                        if (!init_teacher(workbook)) flag.set(false);
                    }
                    case "teacher_course" -> {
                        if (!init_teacher_course(workbook)) flag.set(false);
                    }
                }
            }
        });
        return flag.get();
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

    public boolean init_student_course(Workbook workbook) {
        var sheet = workbook.getSheetAt(0);
        for (int i = 1; i < sheet.getPhysicalNumberOfRows() + 1; i++) {
            var row = sheet.getRow(i);
            if (row == null) {
                System.out.println("Operate end on a null row " + i + " of sheet " + sheet.getSheetName() + " of workbook " + workbook);
                break;
            }
            // sid, cid, score, tid
            var sid = row.getCell(0) == null ? null : row.getCell(0).toString();
            if (sid == null) {
                System.out.println("sid is null");
                continue;
            }
            var cid = row.getCell(1) == null ? null : row.getCell(1).toString();
            Double score = row.getCell(2) == null ? null : Double.parseDouble(row.getCell(2).toString());
            var tid = row.getCell(3) == null ? null : row.getCell(3).toString();

            var studentCourse = new StudentCourse(sid, cid, score, tid);
            studentCourseDao.insertStudentCourse(studentCourse);
        }
        return true;
    }

    public boolean init_teacher(Workbook workbook) {
        var sheet = workbook.getSheetAt(0);
        for (int i = 1; i < sheet.getPhysicalNumberOfRows() + 1; i++) {
            var row = sheet.getRow(i);
            if (row == null) {
                System.out.println("Operate end on a null row " + i + " of sheet " + sheet.getSheetName() + " of workbook " + workbook);
                break;
            }
            // tid, name, sex, age, dname
            var tid = row.getCell(0) == null ? null : row.getCell(0).toString();
            if (tid == null) {
                System.out.println("tid is null");
                continue;
            }
            var name = row.getCell(1) == null ? null : row.getCell(1).toString();
            var sex = row.getCell(2) == null ? null : row.getCell(2).toString();
            Integer age = row.getCell(3) == null ? null : (int) row.getCell(3).getNumericCellValue();
            var dname = row.getCell(4) == null ? null : row.getCell(4).toString();
            Teacher teacher = new Teacher(tid, name, sex, age, dname);
            teacherDao.insertTeacher(teacher);
        }
        return true;
    }

    public boolean init_teacher_course(Workbook workbook) {
        var sheet = workbook.getSheetAt(0);
        for (int i = 1; i < sheet.getPhysicalNumberOfRows() + 1; i++) {
            var row = sheet.getRow(i);
            if (row == null) {
                System.out.println("Operate end on a null row " + i + " of sheet " + sheet.getSheetName() + " of workbook " + workbook);
                break;
            }
            // cid, tid
            var tid = row.getCell(1) == null ? null : row.getCell(1).toString();
            if (tid == null) {
                System.out.println("tid is null");
                continue;
            }
            var cid = row.getCell(0) == null ? null : row.getCell(0).toString();
            TeacherCourse teacherCourse = new TeacherCourse(tid, cid);
            teacherCourseDao.insertTeacherCourse(teacherCourse);
        }
        return true;
    }
    // every dao as a private member


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

    @Autowired
    public void setDataFormatter(DataFormatter dataFormatter) {
        this.dataFormatter = dataFormatter;
    }
}
