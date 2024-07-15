package model;

public enum RoomType {
    SINGLE(1),
    DOUBLE1_1(2),
    DOUBLE2(3),
    TRIPLE1_1_1(4),
    TRIPLE1_2(5);
    int room;

    private RoomType() {
    }
    private RoomType(int i) {
        this.room = i;
    }

    public String toStringFile() {
        switch (room) {
            case 1:
                return "SINGLE";
            case 2:
                return "DOUBLE1_1";
            case 3:
                return "DOUBLE2";
            case 4:
                return "TRIPLE1_1_1";
            case 5:
                return "TRIPLE1_2";
            default:
                return "Invalid";
        }
    }

    @Override
    public String toString(){
        switch (room) {
            case 1:
                return "Jednokrevetna";
            case 2:
                return "Dvokrevetna (2)";
            case 3:
                return "Dvokrevetna (1+1)";
            case 4:
                return "Trokrevetna (1+1+1)";
            case 5:
                return "Trokrevetna (1+2)";
            default:
                return "Invalid";
        }
    }
}
