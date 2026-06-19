package observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ounl
 */
public abstract class Subject {

	private List<Observer> observers = new ArrayList<>();
	
	public void attach(Observer observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}
	
	public void detach(Observer observer) {
		int index = observers.indexOf(observer);
		if (index != -1) {
			observers.remove(index);
		}
	}

	public void notifyObservers() {
	  for (Observer o: observers) {        		
	    o.update(this, null);
	  };
	}
			
	public void notifyObservers(Object arg) {
	  for (Observer o: observers) {        		
	    o.update(this, arg);
	  };
	}

}
