package com.echo.mongohello.entity.advanced;

import com.echo.mongohello.entity.Student;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class StudentAndSelectedCourseNum extends Student {
    // entity class for student and selected course number.
    private Integer selectedCourseNum;

    public Integer getSelectedCourseNum() {
        return selectedCourseNum;
    }

    public void setSelectedCourseNum(Integer selectedCourseNum) {
        this.selectedCourseNum = selectedCourseNum;
    }

    public StudentAndSelectedCourseNum(Student student, Integer selectedCourseNum) {
        super();
        this.setSid(student.getSid());
        this.setName(student.getName());
        this.setSex(student.getSex());
        this.setAge(student.getAge());
        this.setBirthday(student.getBirthday());
        this.setDname(student.getDname());
        this.setClassId(student.getClassId());
        this.selectedCourseNum = selectedCourseNum;
    }
}
