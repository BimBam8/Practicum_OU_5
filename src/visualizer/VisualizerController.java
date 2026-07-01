package visualizer;

import java.util.Map;
import java.util.TreeMap;

import domein.PrikToGo;

/**
 * Voorbeeld van een implementatie klasse
 * 
 * @author Medewerker OU
 * 
 *
 */
public class VisualizerController implements VisualizerControllerInterface {

  private final PrikToGo ptg;

  /**
   * Creeert een controller voor de visualizer
   * 
   * @param ptg de domeinklasse PrikToGo
   */
  public VisualizerController(PrikToGo ptg) {
    this.ptg = ptg;
  }

  /**
   * Wordt aangeroepen als er op een bar geklikt wordt.
   * Opent of sluit de vestiging met de gegeven naam.
   * 
   * @param naam   naam van de vestiging
   * @param aantal huidig aantal klanten (niet gebruikt)
   */
  @Override
  public void barClicked(String naam, Integer aantal) {
    ptg.toggleVestiging(naam);
  }

  /**
   * Geeft de informatie over de bars (staafjes) terug in een map
   * 
   * @return map met key = naam van de bar en value = waarde van de bar
   */
  @Override
  public Map<String, Integer> getBarInfo() {
    return ptg.getKlantenAantalPerVestiging();
  }
}
