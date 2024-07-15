package view.reports;

import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.PieChart;

import manage.ManagerFactory;
import manage.ReservationManager;
import manage.ReservationRequestManager;
import model.ReservationStatus;
import net.miginfocom.swing.MigLayout;

public class ReservationGraphPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public ReservationGraphPanel() {
		ReservationManager reservationManager = ManagerFactory.getInstance().getReservationManager();
		ReservationRequestManager reservationRequestManager = ManagerFactory.getInstance().getReservationRequestManager();
		setLayout(new MigLayout("", "[grow]", "[]"));
		
		JLabel titlelbl = new JLabel("Status rezervacija u poslednjih 30 dana");
		titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
		add(titlelbl, "cell 0 0,alignx center");
		
		HashMap<String, Integer> reservationsMap = new HashMap<>();
		reservationsMap.put("Odobreno", reservationManager.getNumberOfReservationsByStatus(LocalDate.now().minusDays(30), LocalDate.now().plusDays(1), ReservationStatus.CONFIRMED));
		reservationsMap.put("Odbijeno", reservationManager.getNumberOfReservationsByStatus(LocalDate.now().minusDays(30), LocalDate.now().plusDays(1), ReservationStatus.REJECTED));
		reservationsMap.put("Otkazano", reservationManager.getNumberOfReservationsByStatus(LocalDate.now().minusDays(30), LocalDate.now().plusDays(1), ReservationStatus.CANCELED));
		reservationsMap.put("Ceka se", reservationRequestManager.getNumberOfRequestsByDate(LocalDate.now().minusDays(30), LocalDate.now().plusDays(1)));
		
		PieChart pieChart1 = new PieChart(750, 400);
		
		for (Map.Entry<String, Integer> entry : reservationsMap.entrySet()) {
			pieChart1.addSeries(entry.getKey(), entry.getValue());
		}
		
		BufferedImage chartImage1 = BitmapEncoder.getBufferedImage(pieChart1);
		
		JLabel chartLabel = new JLabel();
		
		chartLabel.setIcon(new javax.swing.ImageIcon(chartImage1.getScaledInstance(750, 400, Image.SCALE_DEFAULT)));
		add(chartLabel, "cell 0 1,grow,alignx center");
	}

}
