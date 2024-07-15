package view.cleaner;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import manage.ManagerFactory;
import manage.RoomManager;
import model.Cleaner;
import model.Room;
import net.miginfocom.swing.MigLayout;

public class RoomsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Cleaner cleaner;

	public RoomsPanel(Cleaner cleaner) {
		this.cleaner = cleaner;
		setLayout(new MigLayout("", "[grow]", ""));
        
        buildRoomsPanel(this);
	}

	private void buildRoomsPanel(JPanel roomsPanel) {
	    roomsPanel.removeAll();
	    
	    JLabel title = new JLabel("Sobe za ciscenje");
	    title.setFont(new Font("Tahoma", Font.PLAIN, 30));
	    title.setHorizontalAlignment(SwingConstants.CENTER);
	    roomsPanel.add(title, "cell 0 0, alignx center, wrap");

	    RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
	    List<Room> rooms = roomManager.getRoomsByCleaner(cleaner.getUsername());

	    for (int i = 0; i < rooms.size(); i++) {
	        Room room = rooms.get(i);
	        JLabel roomLabel = new JLabel("Broj sobe: " + room.getNumber());
	        JButton cleanedButton = new JButton("Ocisti sobu");
	        cleanedButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {

	                roomManager.cleanRoom(room.getNumber());
	                cleanedButton.setEnabled(false);

	                buildRoomsPanel(roomsPanel);
	            }
	        });
	        roomsPanel.add(roomLabel, "cell 0 " + (2*i+1));
	        roomsPanel.add(cleanedButton, "cell 0 " + (2*i+2));
	    }

	    roomsPanel.revalidate();
	    roomsPanel.repaint();
	}
}
