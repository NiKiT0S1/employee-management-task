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
public class Position {

    private int id;
    private String positionName;
    private BigDecimal positionSalary; // В БД значение DECIMAL
}
