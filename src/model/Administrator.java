package model;

import java.time.LocalDate;

public class Administrator extends Employee{

    public Administrator(String name, String lastName, boolean gender, LocalDate birthDate, String phone, String adress, String username, String password, double salary, int qualification, int yearsWorking, EmployeeType employeeType){
        super(name, lastName, gender, birthDate, phone, adress, username, password, salary, qualification, yearsWorking, employeeType);
    }
}
