package model;

import java.text.DecimalFormat;
import java.time.LocalDate;

public abstract class Employee extends User {
    protected double salary;
    protected int qualification;
    protected int yearsWorking;
    protected EmployeeType employeeType;

    public Employee(String name, String lastName, boolean gender, LocalDate birthDate, String phone,
                    String adress, String username, String password, double salary, int qualification, int yearsWorking, EmployeeType employeeType) {
        super(name, lastName, gender, birthDate, phone, adress, username, password);
        this.salary = salary;
        this.qualification = qualification;
        this.yearsWorking = yearsWorking;
        this.employeeType = employeeType;

        calculateSalary();
    }

    @Override
    public String toString() {
        return  name + "," + lastName + "," + gender
                + ","
                + birthDate.toString() + "," + phone + "," + adress + "," + username
                + ","
                + password + "," + salary + "," + qualification + ","
                + yearsWorking + "," + employeeType.toFileString();
    }

    @Override
    public String toReadableString() {
        switch (this.employeeType){
            case CLEANER:
                return "Ime: " + name + "\n" +
                        "Prezime: " + lastName + "\n" +
                        "Polozaj: Cistac" + "\n";
            case RECEPTIONIST:
                return "Ime: " + name + "\n" +
                        "Prezime: " + lastName + "\n" +
                        "Recetionar" + "\n";
            case ADMINISTRATOR:
                return "Ime: " + name + "\n" +
                        "Prezime: " + lastName + "\n" +
                        "Administrator" + "\n";
            default:
                return "Ime: " + name + "\n" +
                        "Prezime: " + lastName + "\n";
        }
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
        calculateSalary();
    }

    public int getQualification() {
        return qualification;
    }

    public void setQualification(int qualification) {
        this.qualification = qualification;
    }

    public int getYearsWorking() {
        return yearsWorking;
    }

    public void setYearsWorking(int yearsWorking) {
        this.yearsWorking = yearsWorking;
    }
    
	public EmployeeType getEmployeeType() {
		return employeeType;
	}
	
	public void setEmployeeType(EmployeeType employeeType) {
		this.employeeType = employeeType;
	}
	
	private void calculateSalary() {
		if(this.salary == 0){
            switch (employeeType) {
                case CLEANER:
                    this.salary = 1000 * (1 + Double.valueOf(this.qualification) / 10) * (1 + Double.valueOf(this.yearsWorking) / 10);
                    break;
                case RECEPTIONIST:
                    this.salary = 1500 * (1 + Double.valueOf(this.qualification) / 10) * (1 + Double.valueOf(this.yearsWorking) / 10);
                    break;
                case ADMINISTRATOR:
                    this.salary = 2000 * (1 + Double.valueOf(this.qualification) / 10) * (1 + Double.valueOf(this.yearsWorking) / 10);
                    break;
                default:
                    break;
            }
            DecimalFormat df = new DecimalFormat("#.##");
            this.salary = Double.valueOf(df.format(this.salary));
        }
	}
	
}
