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
@Document(collection = "teacher_course")
public class TeacherCourse {
    // entity class for teacher_course in mongoDB.
    // the name of the collection is "teacher_course".
    private String tid;
    private String cid;
}
