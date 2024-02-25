package P4;

import java.util.ArrayList;
import java.util.List;

import edu.metrostate.ics372.p2.Event;

public class Ambulance {
	private final int ambulanceID;
	private int callTimeRemaining;
	private List<Event> callsAssigned;
	
	public Ambulance(int ambulanceID) {
		this.ambulanceID = ambulanceID;
		callsAssigned = new ArrayList<>();
	}
	
	public void decrementCallTimeRemaining() {
		if (callTimeRemaining > 0) {
			callTimeRemaining--;
		}
	}
	
	public boolean isAvailable() {
		return callTimeRemaining == 0;
	}

	public int getAmbulanceID() {
		return ambulanceID;
	}
	
	public void setTimeRemaining(int duration) {
		this.callTimeRemaining = duration;
	}

	public void assignCall(Event call) {
		callsAssigned.add(call);
	}
	
	public List<Event> getCallsAssigned(){
		return callsAssigned;
	}
}
