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
@Document(collection = "teacher")
public class Teacher {
    // entity class for teacher in mongoDB.
    // the name of the collection is "teacher".
    private String tid;
    private String name;
    private String sex;
    private Integer age;
    private String dname;
}
