package visualizer;

import java.util.Map;

import javax.swing.JOptionPane;

import domein.PrikToGo;
import observer.Observer;

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
    // hier try catch om de exception te vangen die wordt gegooid als de laatste
    // vestiging gesloten wordt.
    try {
      ptg.toggleVestiging(naam);
    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(null, e.getMessage(),
          "Fout", JOptionPane.WARNING_MESSAGE);
    }
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

  @Override
  public void beeindig(Observer observer) {
    ptg.detach(observer);
  }
}
