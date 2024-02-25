/* @author: Dustin Hagstrom */
package P4;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import edu.metrostate.ics372.p2.Event;

public class Dispatcher {
	private Queue<Event> incomingCallQueue;
	private final PriorityQueue<Event> priorityCallQueue;

	/*
	 * Constructor: create a Dispatcher
	 * 
	 * @parameter: a Queue with Calls in the order they are called in to an
	 * AmbulanceDispatcher
	 */
	public Dispatcher(Queue<Event> incomingCallQueue) {
		this.incomingCallQueue = incomingCallQueue;
		if (incomingCallQueue.size() > 0) {
			priorityCallQueue = new PriorityQueue<Event>(incomingCallQueue.size(),
					new CallEventComparator());
		} else {
			priorityCallQueue = new PriorityQueue<Event>(10,
					new CallEventComparator());
		}
	}

	/*
	 * This function maps Ambulances to Calls
	 * 
	 * @parameter: A List of Ambulances that are available, The current
	 * LocalDateTime
	 */
	public void assignAmbulances(List<Ambulance> ambulances,
			LocalDateTime currentTime) {

		// move calls to priority queue that match current time
		while (!incomingCallQueue.isEmpty() && incomingCallQueue.peek()
				.getCallTime().compareTo(currentTime) == 0) {
			priorityCallQueue.add(incomingCallQueue.poll());
		}

		// assign calls to available ambulances
		for (int i = 0; i < ambulances.size()
				&& !priorityCallQueue.isEmpty(); i++) {
			Ambulance currentAmbulance = ambulances.get(i);
			Call currentCall = (Call) priorityCallQueue.poll();
			currentAmbulance.assignCall(currentCall);
			currentAmbulance.setTimeRemaining(currentCall.getDuration());
			currentCall.setDispatchTime(currentTime);
			currentCall.setAssignedAmbulance(currentAmbulance);
		}
	}

	/*
	 * This function indicates that the Dispatcher is complete with Dispatching
	 * operations. End conditions are the Calls received Queue is empty &&
	 * the Priority Queue is also empty, indicating all Calls dispatched.
	 * 
	 * @return
	 * True if the Dispatcher is complete, false otherwise.
	 */
	public boolean isDispatcherComplete() {
		return incomingCallQueue.isEmpty() && priorityCallQueue.isEmpty();
	}

	/*
	 * A Comparator that orders Calls into a Priority Queue based on Priority
	 * then by timeRequested, and breaking all ties by CallID
	 */
	private static class CallEventComparator implements Comparator<Event> {
		@Override
		public int compare(Event event1, Event event2) {
			// sort by priority
			int ordinalEvent1 = event1.getPriority().ordinal();
			int ordinalEvent2 = event2.getPriority().ordinal();

			// then by call time, break ties by callID
			if (ordinalEvent1 == ordinalEvent2) {
				LocalDateTime callTimeEvent1 = event1.getCallTime();
				LocalDateTime callTimeEvent2 = event2.getCallTime();
				if (callTimeEvent1.compareTo(callTimeEvent2) != 0) {
					return callTimeEvent1.compareTo(callTimeEvent2);
				} else {
					int callID1 = Integer
							.parseInt(event1.getCallID().substring(1));
					int callID2 = Integer
							.parseInt(event2.getCallID().substring(1));

					return callID1 - callID2;
				}
			} else {
				return ordinalEvent1 - ordinalEvent2;
			}
		}
	}

}
