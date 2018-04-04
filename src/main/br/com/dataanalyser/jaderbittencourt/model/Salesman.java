package br.com.dataanalyser.jaderbittencourt.model;

import lombok.Data;

@Data
public class Salesman {

    private String CPF;
    private String name;
    private Double salary;

    public Salesman() {
    }

    public Salesman(String CPF, String name, Double salary) {
        this.CPF = CPF;
        this.name = name;
        this.salary = salary;
    }
}
