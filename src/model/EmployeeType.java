package model;

public enum EmployeeType {
    ADMINISTRATOR(1), CLEANER(2), RECEPTIONIST(3);
    int type;

    private EmployeeType() {
    }

    private EmployeeType(int i) {
        this.type = i;
    }

    @Override
    public String toString() {
        switch (type) {
            case 1:
                return "Administrator";
            case 2:
                return "Cistac";
            case 3:
                return "Recepcioner";
            default:
                return "Invalid";
        }
    }
    
    public String toFileString() {
    	        switch (type) {
            case 1:
                return "ADMINISTRATOR";
            case 2:
                return "CLEANER";
            case 3:
                return "RECEPTIONIST";
            default:
                return "Invalid";
        }
    }
}
