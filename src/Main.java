import domein.PrikToGo;
import presentatie.View;
import visualizer.VisualizerController;
import visualizer.Visualizer;


public class Main {
	public static void main(String[] args) {
		try {
			PrikToGo prikToGo = new PrikToGo();

			
			

			VisualizerController vc = new VisualizerController(prikToGo);
			Visualizer visualizer = new Visualizer(vc.getBarInfo(), vc);
			visualizer.setVisible(true);
			prikToGo.attach(visualizer);

			View view = new View(prikToGo);
			prikToGo.attach(view);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}