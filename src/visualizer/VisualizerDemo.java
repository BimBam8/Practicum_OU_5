package visualizer;

import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * Klasse die de werking van de visualizer demomstreert
 * 
 * @author Medewerker OU
 *
 */
public class VisualizerDemo {

  public static void main(String[] args) {
    // aanmaken m=van de map waarmee de bars worden geplaatst
    Map<String, Integer> map = new TreeMap<>();
    map.put("aap", 100);// dit wordt een bar met naam "aap" en relatieve hoogt 100
    map.put("noot", 200);
    map.put("roos", 300);
    map.put("mies", 0);
    VisualizerControllerInterface contr = new VisualizerController();
    Visualizer vis = new Visualizer(map, contr);
    vis.setVisible(true);

  }

}
