package model;

import java.time.LocalDate;

public class Receptionist extends Employee{
    public Receptionist(String name, String lastName, boolean gender, LocalDate birthDate, String phone, String adress, String username, String password, double salary, int qualification, int yearsWorking, EmployeeType employeeType){
        super(name, lastName, gender, birthDate, phone, adress, username, password, salary, qualification, yearsWorking, employeeType);
    }

}
