package P4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.metrostate.ics372.p2.Event;

class AmbulanceDispatcherTest {

	// Z: ZERO CASE

	@Test
	@DisplayName("Zero Case: -> zero call record")
	void testZeroCase() {
		
		Event[] testArray = new Event[0];
		
		assertArrayEquals(testArray,
				AmbulanceDispatcher.processEvents(1, "zero_case"));
	}

	@Test
	@DisplayName("negative Case: three calls, -1 ambulance")
	void testNegativeCase() {

		assertThrows(IllegalArgumentException.class,
				() -> AmbulanceDispatcher.processEvents(-1, "three_case"));
	}

	// O: ONE CASE
	@Test
	@DisplayName("One Case: one call, one Ambulance")
	void testOneCase() {
		String lineFromTestFile = "2023-05-24T00:54|X10008|T1|70";
		Call call = new Call(lineFromTestFile);
		call.setAssignedAmbulance(new Ambulance(1));
		call.setDispatchTime(call.getCallTime());
		Event[] testArray = new Event[] { call };

		assertArrayEquals(testArray,
				AmbulanceDispatcher.processEvents(1, "one_case"));
	}

	// M: many or more complicated case

	@Test
	@DisplayName("Many Case: Two calls, one Ambulance")
	void testTwoCallsCase() {
		String[] linesFromTestFile = { "2023-05-24T00:02|X10001|T2|20",
				"2023-05-24T00:02|X10002|T3|20" };
		Call[] calls = { new Call(linesFromTestFile[0]),
				new Call(linesFromTestFile[1]) };
		Ambulance ambulance = new Ambulance(1);
		calls[0].setAssignedAmbulance(ambulance);
		calls[0].setDispatchTime(calls[0].getCallTime().plusMinutes(20));
		calls[1].setAssignedAmbulance(ambulance);
		calls[1].setDispatchTime(calls[0].getCallTime());
		Event[] testArray = (Event[]) new Call[] { calls[1], calls[0] };

		assertArrayEquals(testArray,
				AmbulanceDispatcher.processEvents(1, "two_case"));
	}

	@Test
	@DisplayName("Many Case: Two calls, two Ambulances")
	void testOneCase1() {

		String[] linesFromTestFile = { "2023-05-24T00:02|X10001|T2|20",
				"2023-05-24T00:02|X10002|T3|20" };
		Call[] calls = { new Call(linesFromTestFile[0]),
				new Call(linesFromTestFile[1]) };
		calls[0].setAssignedAmbulance(new Ambulance(2));
		calls[0].setDispatchTime(calls[0].getCallTime());
		calls[1].setAssignedAmbulance(new Ambulance(1));
		calls[1].setDispatchTime(calls[1].getCallTime());
		Event[] testArray = (Event[]) new Call[] { calls[1], calls[0] };

		assertArrayEquals(testArray,
				AmbulanceDispatcher.processEvents(2, "two_case"));
	}

	@Test
	@DisplayName("Many Case: Two calls, three Ambulances")
	void testTwoCallsCase2() {
		String[] linesFromTestFile = { "2023-05-24T00:02|X10001|T2|20",
				"2023-05-24T00:02|X10002|T3|20" };
		Call[] calls = { new Call(linesFromTestFile[0]),
				new Call(linesFromTestFile[1]) };
		calls[0].setAssignedAmbulance(new Ambulance(2));
		calls[0].setDispatchTime(calls[0].getCallTime());
		calls[1].setAssignedAmbulance(new Ambulance(1));
		calls[1].setDispatchTime(calls[1].getCallTime());
		Event[] testArray = (Event[]) new Call[] { calls[1], calls[0] };

		assertArrayEquals(testArray,
				AmbulanceDispatcher.processEvents(3, "two_case"));
	}

	@Test
	@DisplayName("Many Case: Three calls, one Ambulance")
	void testThreeCallsCase0() {

		String[] linesFromTestFile = { "2023-05-24T00:02|X10001|T1|20",
				"2023-05-24T00:02|X10002|T3|20",
				"2023-05-24T00:02|X10003|T2|70" };
		Call[] calls = { new Call(linesFromTestFile[0]),
				new Call(linesFromTestFile[1]),
				new Call(linesFromTestFile[2]) };
		Ambulance ambulance = new Ambulance(1);
		calls[0].setAssignedAmbulance(ambulance);
		calls[0].setDispatchTime(calls[0].getCallTime().plusMinutes(90));
		calls[1].setAssignedAmbulance(ambulance);
		calls[1].setDispatchTime(calls[0].getCallTime());
		calls[2].setAssignedAmbulance(ambulance);
		calls[2].setDispatchTime(calls[0].getCallTime().plusMinutes(20));
		Event[] testArray = (Event[]) new Call[] { calls[1], calls[2],
				calls[0] };

		assertArrayEquals(testArray,
				AmbulanceDispatcher.processEvents(1, "three_case"));
	}

	@Test
	@DisplayName("Many Case: three calls, two Ambulance")
	void testThreeCallsCase1() {
		String[] linesFromTestFile = { "2023-05-24T00:02|X10001|T1|20",
				"2023-05-24T00:02|X10002|T3|20",
				"2023-05-24T00:02|X10003|T2|70" };
		Call[] calls = { new Call(linesFromTestFile[0]),
				new Call(linesFromTestFile[1]),
				new Call(linesFromTestFile[2]) };
		Ambulance ambulance = new Ambulance(1);
		Ambulance ambulance2 = new Ambulance(2);
		calls[0].setAssignedAmbulance(ambulance);
		calls[0].setDispatchTime(calls[0].getCallTime().plusMinutes(20));
		calls[1].setAssignedAmbulance(ambulance);
		calls[1].setDispatchTime(calls[0].getCallTime());
		calls[2].setAssignedAmbulance(ambulance2);
		calls[2].setDispatchTime(calls[0].getCallTime());
		Event[] testArray = (Event[]) new Call[] { calls[1], calls[0],
				calls[2] };

		assertArrayEquals(testArray,
				AmbulanceDispatcher.processEvents(2, "three_case"));
	}

	@Test
	@DisplayName("Many Case: three calls, three Ambulance")
	void testThreeCallsCase2() {
		String[] linesFromTestFile = { "2023-05-24T00:02|X10001|T1|20",
				"2023-05-24T00:02|X10002|T3|20",
				"2023-05-24T00:02|X10003|T2|70" };
		Call[] calls = { new Call(linesFromTestFile[0]),
				new Call(linesFromTestFile[1]),
				new Call(linesFromTestFile[2]) };
		Ambulance ambulance = new Ambulance(1);
		Ambulance ambulance2 = new Ambulance(2);
		Ambulance ambulance3 = new Ambulance(3);
		calls[0].setAssignedAmbulance(ambulance3);
		calls[0].setDispatchTime(calls[0].getCallTime());
		calls[1].setAssignedAmbulance(ambulance);
		calls[1].setDispatchTime(calls[0].getCallTime());
		calls[2].setAssignedAmbulance(ambulance2);
		calls[2].setDispatchTime(calls[0].getCallTime());
		Event[] testArray = (Event[]) new Call[] { calls[1], calls[2],
				calls[0] };

		assertArrayEquals(testArray,
				AmbulanceDispatcher.processEvents(3, "three_case"));
	}

	@Test
	@DisplayName("Many Case: three calls, three Ambulance, one is dispatched")
	void testThreeCallsCase3() {
		String[] linesFromTestFile = { "2023-05-24T00:02|X10001|T1|20",
				"2023-05-24T00:22|X10002|T3|20",
				"2023-05-24T00:42|X10003|T2|70" };
		Call[] calls = { new Call(linesFromTestFile[0]),
				new Call(linesFromTestFile[1]),
				new Call(linesFromTestFile[2]) };
		Ambulance ambulance = new Ambulance(1);
		calls[0].setAssignedAmbulance(ambulance);
		calls[0].setDispatchTime(calls[0].getCallTime());
		calls[1].setAssignedAmbulance(ambulance);
		calls[1].setDispatchTime(calls[0].getCallTime().plusMinutes(20));
		calls[2].setAssignedAmbulance(ambulance);
		calls[2].setDispatchTime(calls[0].getCallTime().plusMinutes(40));
		Event[] testArray = (Event[]) new Call[] { calls[0], calls[1],
				calls[2] };

		assertArrayEquals(testArray,
				AmbulanceDispatcher.processEvents(3, "three_case2"));
	}

	@DisplayName("Many Case: three calls, 800 Ambulance")
	@Test
	public void testMaxNumberAmbulances() {
		String[] linesFromTestFile = { "2023-05-24T00:02|X10001|T1|20",
				"2023-05-24T00:02|X10002|T3|20",
				"2023-05-24T00:02|X10003|T2|70" };
		Call[] calls = { new Call(linesFromTestFile[0]),
				new Call(linesFromTestFile[1]),
				new Call(linesFromTestFile[2]) };
		Ambulance ambulance = new Ambulance(1);
		Ambulance ambulance2 = new Ambulance(2);
		Ambulance ambulance3 = new Ambulance(3);
		calls[0].setAssignedAmbulance(ambulance3);
		calls[0].setDispatchTime(calls[0].getCallTime());
		calls[1].setAssignedAmbulance(ambulance);
		calls[1].setDispatchTime(calls[0].getCallTime());
		calls[2].setAssignedAmbulance(ambulance2);
		calls[2].setDispatchTime(calls[0].getCallTime());
		Event[] testArray = (Event[]) new Call[] { calls[1], calls[2],
				calls[0] };

		assertArrayEquals(testArray,
				AmbulanceDispatcher.processEvents(800, "three_case"));
	}


	// B: boundary case
	@DisplayName("Test null file path")
	@Test
	void testNullFile() {

		assertThrows(NullPointerException.class, () -> {
			AmbulanceDispatcher.processEvents(1, null);
		});

	}
	
	@DisplayName("erroneous whitespace")
	@Test
	void testWhitespaceFile() {
		
		assertThrows(IllegalArgumentException.class, () -> {
			AmbulanceDispatcher.processEvents(1, "erroneous_whitespace");
		});
		
	}
	
	@DisplayName("erroneous whitespace")
	@Test
	void testWhitespaceFile3() {
		
		assertThrows(IllegalArgumentException.class, () -> {
			AmbulanceDispatcher.processEvents(1, "erroneous_whitespace2");
		});
		
	}
	
	@DisplayName("erroneous whitespace")
	@Test
	void testWhitespaceFile4() {
		
		assertThrows(IllegalArgumentException.class, () -> {
			AmbulanceDispatcher.processEvents(1, "erroneous_whitespace3");
		});
		
	}

	@DisplayName("Test invalid file name")
	@Test
	void testFileNotFoundException0() {

		assertThrows(IllegalArgumentException.class, () -> {
			AmbulanceDispatcher.processEvents(1, "notAFilePath");
		});
	}

	@DisplayName("Test empty String file name")
	@Test
	void testFileNotFoundException1() {

		assertThrows(IllegalArgumentException.class, () -> {
			AmbulanceDispatcher.processEvents(1, "");
		});

	}
}
