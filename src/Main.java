import domein.PrikToGo;
import presentatie.View;

public class Main {
	public static void main(String[] args) {
		try {
			PrikToGo prikToGo = new PrikToGo();

			View view = new View(prikToGo);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}