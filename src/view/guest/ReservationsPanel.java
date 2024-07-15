package view.guest;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import manage.ManagerFactory;
import manage.ReservationManager;
import model.Guest;
import net.miginfocom.swing.MigLayout;
import tableModels.GuestRequestsModel;
import tableModels.ReservationGuestModel;

public class ReservationsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ReservationManager reservationManager;

	public ReservationsPanel(Guest guest, JTable table, JLabel total, JTable requests) {
		this.reservationManager = ManagerFactory.getInstance().getReservationManager();
		
		setLayout(new MigLayout("", "[grow]", "[][grow][][]"));
        
        JLabel titlelbl = new JLabel("Vase rezervacije");
        titlelbl.setHorizontalAlignment(SwingConstants.CENTER);
        titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
        add(titlelbl, "cell 0 0,alignx center");
        
        JScrollPane sc = new JScrollPane(table);
        add(sc, "cell 0 1,grow");
        
        JLabel title1lbl = new JLabel("Vasi zahtevi");
        title1lbl.setHorizontalAlignment(SwingConstants.CENTER);
        title1lbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
        add(title1lbl, "cell 0 2,alignx center");
        
        JScrollPane sc1 = new JScrollPane(requests);
        add(sc1, "cell 0 3,grow");
        
        Dimension btnSize = new Dimension(150, 50);
        JButton cancelButton = new JButton("Otkazi rezervaciju");
        cancelButton.setPreferredSize(btnSize);
        cancelButton.setMinimumSize(btnSize);
        cancelButton.setMaximumSize(btnSize);
        
		total.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(total, "cell 0 4,alignx center");
		
		cancelButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        int selectedRow = requests.getSelectedRow();
		        if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati zahtev", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
		        }
		   
	            int modelRow = table.convertRowIndexToModel(selectedRow);
	            GuestRequestsModel model = (GuestRequestsModel) requests.getModel();
	            boolean canceled = model.cancelReservation(modelRow);
	            if (canceled) {
                	JOptionPane.showMessageDialog(null, "Rezervacija je otkazana", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                	total.setText("Ukupna cena: " + reservationManager.getTotalCostByGuest(guest.getUsername()) + " Eur");
                } else {
                	JOptionPane.showMessageDialog(null, "Rezervacija ne moze biti otkazana", "Greksa", JOptionPane.ERROR_MESSAGE);
                }
	            
	            ReservationGuestModel tableModel = (ReservationGuestModel) table.getModel();
	            tableModel.fireTableDataChanged();
		    }
		});
		
		
		add(cancelButton, "flowx,cell 0 5,alignx center");
		
		total.setText("Ukupna cena: " + reservationManager.getTotalCostByGuest(guest.getUsername()) + " Eur");
	}

}
