/*
 * @author: Dustin Hagstrom
 */
package P4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

import edu.metrostate.ics372.p2.Event;

public class AmbulanceDispatcher {
	private Dispatcher dispatcher;
	private List<Ambulance> ambulances;
	private LocalDateTime currentTime;

	/*
	 * Constructor: private constructor used in performance of dispatch
	 * simulation
	 * 
	 * @parameter: the number of ambulances to use in the simulation
	 */
	private AmbulanceDispatcher(int numAmbulances) {
		if (numAmbulances < 1) {
			throw new IllegalArgumentException(
					"There must be at least one ambulance");
		}
		List<Ambulance> ambulances = new ArrayList<>();
		for (int i = 1; i <= numAmbulances; i++) {
			ambulances.add(new Ambulance(i));
		}
		this.ambulances = ambulances;
	}

	/*
	 * Main logic function: This function is called to perform a dispatch
	 * simulation.
	 * 
	 * @parameters: the number of ambulances to use and the string file name of
	 * the input file to simulate the receiving of emergency calls by a
	 * ambulance dispatcher.
	 * 
	 * @return An array of Call data for processing
	 */
	public static Event[] processEvents(int numAmbulances,
			String scenarioFilename) {
		Objects.requireNonNull(scenarioFilename,
				"Scenario file cannot be null.");

		Queue<Event> incomingCallQueue;
		AmbulanceDispatcher simulation;
		/* make a new simulation and give it data to process */
		try {
			simulation = new AmbulanceDispatcher(numAmbulances);
			incomingCallQueue = simulation.parseFile(scenarioFilename);
			
			if (!incomingCallQueue.isEmpty()) {
				simulation.currentTime = incomingCallQueue.peek().getCallTime();
			}
			simulation.setDispatcher(new Dispatcher(incomingCallQueue));

			while (!simulation.dispatcher.isDispatcherComplete()
					|| !simulation.allAmbulancesAvailable()) {
				simulation.activateDispatcher();
				simulation.advanceTimeByOneMinute();
			}

		} catch (IllegalArgumentException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(
					"The scenario file name that you provided is incorrect.");
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"There was an error reading the contents of this file.");
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(
					"The file is not in the correct format.");
		}

		/* Collect Call data from the ambulances that were assigned to them */
		List<Event> callRecords = simulation.ambulances.stream()
				.flatMap(ambulance -> ambulance.getCallsAssigned().stream())
				.collect(Collectors.toList());

		return callRecords.toArray(new Event[0]);
	}

	/*
	 * This method parses an input file, constructs Calls, and returns a Queue
	 * with Calls in the order read from file
	 * 
	 * @parameter scenarioFilename The String name of the input file
	 * 
	 * @return A Queue with Calls in the order read
	 */
	private Queue<Event> parseFile(String scenarioFilename) throws IOException {
		Queue<Event> incomingCallQueue = new LinkedList<>();
		ClassLoader classLoader = AmbulanceDispatcher.class.getClassLoader();
		InputStream inputStream = classLoader
				.getResourceAsStream(scenarioFilename);

		if (inputStream == null) {
			throw new FileNotFoundException();
		}

		try (BufferedReader fileReader = new BufferedReader(
				new InputStreamReader(inputStream))) {
			String line;
			while ((line = fileReader.readLine()) != null) {
				if (!line.isBlank()) {
					incomingCallQueue.add(new Call(line));
				}
			}
		} catch (IOException e) {
			throw e;
		}

		return incomingCallQueue;
	}

	/*
	 * Helper function: Advance simulation time by one minute, decrement all
	 * ambulances time remaining
	 */
	private void advanceTimeByOneMinute() {
		currentTime = currentTime.plusMinutes(1);
		ambulances.stream()
				.forEach(ambulance -> ambulance.decrementCallTimeRemaining());

	}

	/*
	 * Helper function: activates the simulation dispatcher to assign available
	 * ambulances to calls in the dispatcher's priority queue.
	 */
	private void activateDispatcher() {
		List<Ambulance> availableAmbulances = ambulances.stream()
				.filter(ambulance -> ambulance.isAvailable()).toList();

		dispatcher.assignAmbulances(availableAmbulances, currentTime);
	}

	/*
	 * Helper function: it identifies if all of the Ambulances are available to
	 * answer calls; this is one of the program end conditions.
	 * 
	 * @return True if all ambulances are available, false otherwise
	 */
	private boolean allAmbulancesAvailable() {
		return ambulances.stream().filter(ambulance -> ambulance.isAvailable())
				.count() == ambulances.size();
	}

	/*
	 * Setter function: it sets the dispatcher field of AmbulanceDispatcher
	 */
	private void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

}
