package com.task.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private int id;
    private String fullName;
    private int departmentId;
    private int positionId;

    private String departmentName;
    private String positionName;
    private BigDecimal positionSalary;
    private boolean headOfDepartment;
}
