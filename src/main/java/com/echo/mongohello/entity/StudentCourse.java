package com.echo.mongohello.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document(collection = "student_course")
public class StudentCourse {
    // entity class for student_course in mongoDB.
    // the name of the collection is "student_course".
    private String sid;
    private String cid;
    private Double score;
    private String tid;
}
