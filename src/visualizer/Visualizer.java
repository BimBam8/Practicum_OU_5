package visualizer;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Set;

import observer.Subject;
import observer.Observer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * Klasse die klikbare bars (staafjes) op een frame toont. Elke bar wordt
 * gekarakteriseerd door een key en een value.
 * 
 * @author Medewerker OU
 *
 */
public class Visualizer extends JFrame implements Observer {

	// intermediair tussen vizualizer (gui) en domein klassen
	private VisualizerControllerInterface contr;

	private static final long serialVersionUID = 1L;
	private static final int WIDTH_FRAME = 1200; // px was 820 // 1000
	private static final int HEIGHT_FRAME = 520; // px was 520
	private static final int MARGIN = 10; // px
	private static final String TITLE = "Data visualizer";
	private int WIDTH_PANE = WIDTH_FRAME - 2 * MARGIN;
	private int HEIGHT_PANE = HEIGHT_FRAME - 3 * MARGIN;
	private static final int HGAP = 10; // Horizontale ruimte tussen de bars
	private Container pane = null;

	/**
	 * Creeert een visualizer (staafdiagram) op grond van een map
	 * 
	 * @param map
	 *              de map
	 * @param contr
	 *              de controller
	 */
	public Visualizer(Map<String, Integer> map, VisualizerControllerInterface contr) {
		super();
		this.contr = contr;
		initialize();
		drawBars(map);
	}

	private void initialize() {
		pane = this.getContentPane();
		pane.setBounds(MARGIN, MARGIN, WIDTH_PANE, HEIGHT_PANE);
		pane.setBackground(Color.white);
		setTitle(TITLE);
		this.setSize(WIDTH_FRAME, HEIGHT_FRAME);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) Math.round((dim.width - WIDTH_FRAME) / 2),
				(int) Math.round((dim.height - HEIGHT_FRAME) / 2));
		pane.setLayout(null);
		// this.setFocusableWindowState(false);
	}

	/**
	 * Tekent de bars op basis van de map met (key, value) paren.
	 * Bars met hoogte 0 worden getoond als bars met maximale hoogte in de
	 * achtergrondkleur.
	 * 
	 */
	private void drawBars(Map<String, Integer> map) {
		pane.removeAll();
		int size = map.size();
		int total_hgap = (size + 1) * HGAP;
		int width_bar = (WIDTH_PANE - total_hgap) / size;
		int maxValue = maxValue(map);
		double verticalScaleFactor = (double) HEIGHT_PANE / (double) maxValue;
		Set<String> keys = map.keySet();
		int x_pos = 0;
		for (String key : keys) {
			x_pos = x_pos + HGAP;
			int value = map.get(key);
			createBar(key, value, x_pos, verticalScaleFactor, width_bar);
			x_pos = x_pos + width_bar;
		}
		this.repaint();
		this.revalidate();
	}

	private void createBar(String key, int value, int x_pos, double verticalScaleFactor, int width_bar) {
		Bar bar;
		if (value == 0) {
			// bar in achtergrondkleur met maximale hoogte om label bovenaan te krijgen.
			int height_bar = HEIGHT_PANE;
			int y_pos = 0;
			bar = new Bar(key, 0, x_pos, y_pos, width_bar, height_bar, new Color(178, 34, 34)); // achtergrondkleur
																								// groen voor open
		} else {
			// // bar in kleur geel met hoogte: schaalfactor * aantal klanten.
			int height_bar = (int) (verticalScaleFactor * value);
			int y_pos = HEIGHT_PANE - height_bar;
			bar = new Bar(key, value, x_pos, y_pos, width_bar, height_bar, new Color(34, 139, 34)); // achtergrondkleur
																									// rood voor
																									// gesloten
		}
		bar.addMouseListener(new BarLuisteraar());
		pane.add(bar);
	}

	/**
	 * Geeft de grootste value in de map
	 * 
	 * @return
	 */
	private int maxValue(Map<String, Integer> map) {
		int max = 0;
		Set<String> keys = map.keySet();
		for (String key : keys) {
			if (map.get(key) > max) {
				max = map.get(key);
			}
		}
		return max;
	}

	class BarLuisteraar extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			Bar bar = (Bar) e.getSource();
			contr.barClicked(bar.getName(), bar.getLabelValue());
		}
	}

	private void beeindigOverzicht() {
		int confirm = JOptionPane.showConfirmDialog(this, "Weet je zeker dat je het overzicht wilt beeindigen?",
				"Bevestiging", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			dispose();
		}
	}

	@Override
	public void update(Subject o, Object arg) {
		drawBars(contr.getBarInfo());
	}

}
