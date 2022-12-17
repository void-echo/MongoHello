package com.echo.mongohello.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document(collection = "student")
public class Student {
    // entity class for student in mongoDB.
    // the name of the collection is "student".
    private String sid;
    private String name;
    private String sex;
    private Integer age;
    private String birthday;
    private String dname;
    @Field("class")
    private String classId;
}
