package tableModels;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import manage.ManagerFactory;
import manage.UserManager;
import model.Employee;

public class EmployeesModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -7745345714270100039L;
	private String[] columnNames = {"Korisnicko ime", "Ime", "Prezime", "Uloga", "Rod", "Datum rodjenja", "Adresa", "Telefon", "Plata", "Radni staz", "Kvalifikacije"};
	private UserManager userManager;

	public EmployeesModel() {
		this.userManager = ManagerFactory.getInstance().getUserManager();
	}

	@Override
	public int getRowCount() {
		return userManager.getEmployees().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Employee employee = userManager.getEmployees().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return employee.getUsername();
		case 1:
			return employee.getName();
		case 2:
			return employee.getLastName();
		case 3:
			return employee.getEmployeeType();
		case 4:
			if (employee.getGender() == true)
                return "Muski";
            else {
                return "Zenski";
            }
		case 5:
			return employee.getBirthDate();
		case 6:
			return employee.getAdress();
		case 7:
			return employee.getPhone();
		case 8:
			return employee.getSalary();
		case 9:
			return employee.getYearsWorking();
		case 10:
			return employee.getQualification();
		default:
			return null;
		}
	}

	@Override
	public String getColumnName(int column) {
		return this.columnNames[column];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return this.getValueAt(0, columnIndex).getClass();
	}

	public Employee getEmployeeAt(int rowIndex) {
		return userManager.getEmployees().get(rowIndex);
	}


	public List<Employee> getEmployees() {
		return userManager.getEmployees();
	}

}
