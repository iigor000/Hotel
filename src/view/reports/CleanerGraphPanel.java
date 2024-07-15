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

import manage.CleanersManager;
import manage.ManagerFactory;
import model.CleanerRooms;
import net.miginfocom.swing.MigLayout;

public class CleanerGraphPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public CleanerGraphPanel() {
		setLayout(new MigLayout("", "[grow]", "[][][]"));
		
		CleanersManager cleanersManager = ManagerFactory.getInstance().getCleanersManager();
		HashMap<String, Integer> cleanersMap = new HashMap<>();
		
		for (CleanerRooms cleaner : cleanersManager.getCleaners()) {
			cleanersMap.put(cleaner.getCleanerUsername(), cleaner.getCleanedRoomsByDate(LocalDate.now().minusDays(30), LocalDate.now().plusDays(1)));
		}
		
		PieChart pieChart = new PieChart(750, 400);
		
		for (Map.Entry<String, Integer> entry : cleanersMap.entrySet()) {
			pieChart.addSeries(entry.getKey(), entry.getValue());
		}
		
		BufferedImage chartImage = BitmapEncoder.getBufferedImage(pieChart);

		JLabel chartLabel = new JLabel();
		chartLabel.setIcon(new javax.swing.ImageIcon(chartImage.getScaledInstance(750, 400, Image.SCALE_DEFAULT)));

		add(chartLabel, "cell 0 1,grow,alignx center");
		
		JLabel titlelbl = new JLabel("Raspodela ociscenih soba u poslednjih 30 dana");
		titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
		add(titlelbl, "cell 0 0,alignx center");
	}

}
