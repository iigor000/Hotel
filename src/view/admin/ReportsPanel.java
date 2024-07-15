package view.admin;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import manage.ManagerFactory;
import manage.ReservationManager;
import model.RoomType;
import net.miginfocom.swing.MigLayout;
import view.reports.ReportsView;

public class ReportsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public ReportsPanel() {
		setLayout(new MigLayout("", "[grow]", "[][]"));
		
		LocalDate start = LocalDate.now().withDayOfMonth(1).minusMonths(12);
		List<Double> single = new ArrayList<Double>();
		List<Double> double1_1 = new ArrayList<Double>();
		List<Double> double2 = new ArrayList<Double>();
		List<Double> triple1_1_1 = new ArrayList<Double>();
		List<Double> triple1_2 = new ArrayList<Double>();
		List<Double> total = new ArrayList<Double>();
		List<String> months = new ArrayList<String>();
		ReservationManager reservationManager = ManagerFactory.getInstance().getReservationManager();
		
		for (int i = 0; i < 12; i++) {
			single.add(reservationManager.getEarningsRoomType(start, start.plusMonths(1), RoomType.SINGLE));
			double1_1.add(reservationManager.getEarningsRoomType(start, start.plusMonths(1), RoomType.DOUBLE1_1));
			double2.add(reservationManager.getEarningsRoomType(start, start.plusMonths(1), RoomType.DOUBLE2));
			triple1_1_1.add(reservationManager.getEarningsRoomType(start, start.plusMonths(1), RoomType.TRIPLE1_1_1));
			triple1_2.add(reservationManager.getEarningsRoomType(start, start.plusMonths(1), RoomType.TRIPLE1_2));
			total.add(reservationManager.getEarnings(start, start.plusMonths(1)));
			start = start.plusMonths(1);
			months.add(start.getMonth().toString().substring(0, 3));
		}
		
		CategoryChart chart = new CategoryChart(950, 500);
		
		CategorySeries singleSeries = chart.addSeries("Jednokrevetna", months, single);
		CategorySeries double1_1Series = chart.addSeries("Dvokrevetna (1+1)", months, double1_1);
		CategorySeries double2Series = chart.addSeries("Dvokrevetna (2)", months, double2);
		CategorySeries triple1_1_1Series = chart.addSeries("Trokrevetna (1+1+1)", months, triple1_1_1);
		CategorySeries triple1_2Series = chart.addSeries("Trokrevetna (1+2)", months, triple1_2);
		CategorySeries totalSeries = chart.addSeries("Ukupno", months, total);
		
		singleSeries.setMarker(SeriesMarkers.NONE);
		double1_1Series.setMarker(SeriesMarkers.NONE);
		double2Series.setMarker(SeriesMarkers.NONE);
		triple1_1_1Series.setMarker(SeriesMarkers.NONE);
		triple1_2Series.setMarker(SeriesMarkers.NONE);
		totalSeries.setMarker(SeriesMarkers.NONE);
		
		chart.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);
		chart.getStyler().setPlotGridLinesVisible(false);
		
		chart.setTitle("Prihodi po mesecima");
		chart.setXAxisTitle("Meseci");
		chart.setYAxisTitle("Prihod");
		
		BufferedImage chartImage = BitmapEncoder.getBufferedImage(chart);

		JLabel chartLabel = new JLabel();
		chartLabel.setIcon(new javax.swing.ImageIcon(chartImage.getScaledInstance(950, 500, Image.SCALE_DEFAULT)));

		add(chartLabel, "cell 0 0,grow,alignx center");
		
		JButton moreReportsbtn = new JButton("Vidite jos izvestaja");
		
		moreReportsbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ReportsView();
			}
		});
		
		add(moreReportsbtn, "cell 0 1,alignx center,hmin 50");
	}

}
