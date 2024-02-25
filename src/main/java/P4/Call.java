package P4;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import edu.metrostate.ics372.p2.Event;

public class Call implements Event {

	private final String callID;
	private final LocalDateTime timeRequested;
	private LocalDateTime dispatchTime;
	private final Priority priority;
	private final int duration;
	private Ambulance ambulance;

	/*
	 * Constructor: constructs Call by parsing input String.
	 * 
	 * @parameter: the String representing a Call
	 */
	public Call(String callData) {
		String[] splitData = callData.trim().split("\\|"); // splits at "|"

		try {

			timeRequested = LocalDateTime.parse(splitData[0]);
			callID = splitData[1];
			priority = Enum.valueOf(Priority.class, splitData[2]);
			duration = Integer.valueOf(splitData[3]);
		} catch(Exception e) {
			throw new IllegalArgumentException(
					"The contents of the file are not in the desired format.");
		}
	}

	@Override
	public int getAssignedAmbulanceID() {
		return ambulance.getAmbulanceID();
	}

	@Override
	public LocalDateTime getAvailTime() {
		return dispatchTime.plusMinutes(duration);
	}

	@Override
	public String getCallID() {
		return callID;
	}

	@Override
	public LocalDateTime getCallTime() {
		return timeRequested;
	}

	@Override
	public int getDispatchDelayInMinutes() {
		Duration dispatchDelay = Duration.between(timeRequested, dispatchTime);
		return (int) dispatchDelay.toMinutes();

	}

	@Override
	public LocalDateTime getDispatchTime() {
		return dispatchTime;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public Priority getPriority() {
		return priority;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}
		Call otherCall = (Call) obj;

		return callID.equalsIgnoreCase(otherCall.callID)
				&& timeRequested.isEqual(otherCall.getCallTime())
				&& dispatchTime.isEqual(otherCall.getDispatchTime())
				&& priority.equals(otherCall.priority) && 
				duration == otherCall.getDuration() && ambulance
						.getAmbulanceID() == otherCall.getAssignedAmbulanceID();
	}

	@Override
	public int hashCode() {
		return Objects.hash(callID, timeRequested, dispatchTime, priority,
				duration, ambulance);
	}

	public void setAssignedAmbulance(Ambulance ambulance) {
		this.ambulance = ambulance;
	}

	public void setDispatchTime(LocalDateTime dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

}
