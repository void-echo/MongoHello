package com.echo.mongohello.entity.advanced;

import com.echo.mongohello.entity.Student;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class StudentAndGPA extends Student {
    // entity class for student and GPA.
    private Double gpa;

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public StudentAndGPA(Student student, Double gpa) {
        super();
        this.setSid(student.getSid());
        this.setName(student.getName());
        this.setSex(student.getSex());
        this.setAge(student.getAge());
        this.setBirthday(student.getBirthday());
        this.setDname(student.getDname());
        this.setClassId(student.getClassId());
        this.gpa = gpa;
    }
}
