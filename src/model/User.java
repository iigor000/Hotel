package model;

import java.time.LocalDate;

import manage.UserManager;

public class User {
    protected String name;
    protected String lastName;
    protected boolean gender;
    protected LocalDate birthDate;
    protected String phone;
    protected String adress;
    protected String username;
    protected String password;

    public User( String name, String lastName, boolean gender, LocalDate birthDate, String phone, String adress,
                  String username, String password) {
        this.name = name;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
        this.adress = adress;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return  name + "," + lastName + "," + gender + ","
                + birthDate.toString() + "," + phone + "," + adress + "," + username + ","
                + password;
    }

    public String toReadableString() {
        return "Name: " + name + "\n" +
                "Last name: " + lastName + "\n";
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getName() {
        return this.name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public boolean getGender() {
        return this.gender;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getAdress() {
        return this.adress;
    }

    public void setPassword(String password) {
        this.password = UserManager.hashPassword(password);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(boolean gender){
        this.gender = gender;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
