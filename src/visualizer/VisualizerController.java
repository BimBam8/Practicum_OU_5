package visualizer;

import java.util.Map;
import java.util.TreeMap;

import domein.PrikToGo;

/**
 * Voorbeeld van een implementatie klasse
 * @author Medewerker OU 
 * 
 *
 */
public class VisualizerController implements VisualizerControllerInterface {

  

  @Override
  public void barClicked(String naam, Integer aantal) {
   System.out.println(naam + "  " + aantal);
    
  }

  @Override
  public Map<String, Integer> getBarInfo() {  
    return null;
  }

}
