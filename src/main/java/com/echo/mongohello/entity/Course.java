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
@Document(collection = "course")
public class Course {
    // entity class for course in mongoDB.
    // the name of the collection is "course".
    private String cid;
    private String name;
    private String fcid;
    private Double credit;
}
