package visualizer;

import java.util.Map;

/**
 * 
 * Intermediair tussen de userinterface (klasse Visualizer met daarop bars (staafjes)) 
 * en de domeinklassen 
 * @author Mederwerker OU
 *
 */
public interface VisualizerControllerInterface {
  
  public void barClicked(String naam, Integer aantal);
  
  public Map<String, Integer> getBarInfo();
  
}

