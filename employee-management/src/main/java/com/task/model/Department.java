package com.task.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    private int id;
    private String departmentName;
    private String departmentPhone;
    private String departmentEmail;

    private String headName;
    private int employeesCount;
}
