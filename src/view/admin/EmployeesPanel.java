package view.admin;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import manage.ManagerFactory;
import manage.UserManager;
import model.Employee;
import net.miginfocom.swing.MigLayout;
import tableModels.EmployeesModel;

public class EmployeesPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public EmployeesPanel(JTable table) {
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JLabel title = new JLabel("Zaposleni");
		title.setFont(new Font("Tahoma", Font.PLAIN, 30));
		add(title, "cell 0 0,alignx center");
		
		
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, "cell 0 1,grow");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		JButton firebtn= new JButton("Otpusti zaposlenog");
		Dimension buttonSize = new Dimension(150, 50);
		firebtn.setMinimumSize(buttonSize);
		firebtn.setMaximumSize(buttonSize);
		firebtn.setPreferredSize(buttonSize);
		JButton changebtn = new JButton("Promeni podatke");
		changebtn.setMinimumSize(buttonSize);
		changebtn.setMaximumSize(buttonSize);
		changebtn.setPreferredSize(buttonSize);
		
		firebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati zaposlenog", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				String username = (String) table.getValueAt(row, 0);
				UserManager userManager = ManagerFactory.getInstance().getUserManager();
				userManager.removeUser(username);
				((EmployeesModel) table.getModel()).fireTableDataChanged();
				
				JOptionPane.showMessageDialog(null, "Zaposleni je otpusten", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		changebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Morate selektovati zaposlenog", "Greska",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String username = (String) table.getValueAt(row, 0);
                UserManager userManager = ManagerFactory.getInstance().getUserManager();
                Employee employee = (Employee) userManager.getUser(username);
                new EditEmployeeDialog(employee, ((EmployeesModel) table.getModel()));
            }
        });
		
		buttonPanel.add(firebtn);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(changebtn);

		add(buttonPanel, "cell 0 2,growx");
	}

}
