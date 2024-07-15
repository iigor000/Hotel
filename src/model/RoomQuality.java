package model;

public enum RoomQuality {
	AC(1), BALCONY(2), TV(3), SMOKER(4), NONSMOKER(5);
	int value;
	
	RoomQuality(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		switch (value) {
		case 1:
			return "Klima";
		case 2:
			return "Balkon";
		case 3:
			return "TV";
		case 4:
			return "Pusacka";
		case 5:
			return "Nepusacka";
		default:
			return "";
		}
	}
	
	public static RoomQuality getFromReadableString(String s) {
		switch (s) {
		case "Klima":
			return AC;
		case "Balkon":
			return BALCONY;
		case "TV":
			return TV;
		case "Pusacka":
			return SMOKER;
		case "Nepusacka":
			return NONSMOKER;
		default:
			return null;
		}
	}
	
	public String toStringFile() {
		switch (value) {
        case 1:
            return "AC";
        case 2:
            return "BALCONY";
        case 3:
            return "TV";
        case 4:
            return "SMOKER";
        case 5:
            return "NONSMOKER";
        default:
            return "";
        }
    }
	
}
