import javax.swing.WindowConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import domein.PrikToGo;
import presentatie.View;
import visualizer.VisualizerController;
import visualizer.Visualizer;

public class Main {
	public static void main(String[] args) {
		try {
			PrikToGo prikToGo = new PrikToGo();

			VisualizerController vc = new VisualizerController(prikToGo);
			Visualizer visualizer = new Visualizer(prikToGo.getKlantenAantalPerVestiging(), vc);
			visualizer.setVisible(true);
			visualizer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			visualizer.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					prikToGo.detach(visualizer);
				}
			});
			prikToGo.attach(visualizer);

			View view = new View(prikToGo);
			prikToGo.attach(view);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}