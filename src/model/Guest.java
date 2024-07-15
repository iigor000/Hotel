package model;
import java.time.LocalDate;

public class Guest extends User{

    public Guest( String name, String lastName, boolean gender, LocalDate birthDate, String phone, String address,
                 String username, String password) {
        super(name, lastName, gender, birthDate, phone, address, username, password);
    }

}
