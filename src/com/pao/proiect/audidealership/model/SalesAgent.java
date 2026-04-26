package com.pao.proiect.audidealership.model;

public class SalesAgent extends Employee {
    private String region;
    private int yearsOfExperience;

    public SalesAgent(String id, String firstName, String lastName, String email,
                      String department, double salary, String region, int yearsOfExperience) {
        super(id, firstName, lastName, email, department, salary);
        this.region = requireText(region, "Regiunea nu poate fi goala.");
        setYearsOfExperience(yearsOfExperience);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = requireText(region, "Regiunea nu poate fi goala.");
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        if (yearsOfExperience < 0) {
            throw new IllegalArgumentException("Experienta nu poate fi negativa.");
        }
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public String toString() {
        return "SalesAgent{id='%s', name='%s', region='%s', experience=%d years}"
                .formatted(getId(), getFullName(), region, yearsOfExperience);
    }
}
