package com.echo.mongohello;

import com.echo.mongohello.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MongoHelloApplication {

    private StudentDao studentDao;

    public static void main(String[] args) {
        SpringApplication.run(MongoHelloApplication.class, args);
        System.out.println("Hello, MongoDB!");

    }

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }
}
