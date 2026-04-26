package com.pao.proiect.audidealership.model;

public class Employee extends Person {
    private String department;
    private double salary;

    public Employee(String id, String firstName, String lastName, String email, String department, double salary) {
        super(id, firstName, lastName, email);
        this.department = requireText(department, "Departamentul nu poate fi gol.");
        setSalary(salary);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = requireText(department, "Departamentul nu poate fi gol.");
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salariul nu poate fi negativ.");
        }
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{id='%s', name='%s', department='%s', salary=%.2f}"
                .formatted(getId(), getFullName(), department, salary);
    }
}
