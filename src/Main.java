import manage.ManagerFactory;
import util.AppSettings;
import view.LoginView;

public class Main {

	public static void main(String[] args) {
		AppSettings appSettings = new AppSettings("database/Rooms.csv", "database/Reservations.csv", "database/Prices.csv", "database/Users.csv", "database/Cleaners.csv", "database/Requests.csv");
        ManagerFactory controllers = ManagerFactory.getInstance(appSettings);
        controllers.loadData();
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                controllers.saveData();
            }
        });
        
        new LoginView();   
	}
}
