
/**
 * Event.java
 * @author Tyler Lorenzi, Gregory Mayo, Ealrada Piroyan
 * @version 1.0 08/02/19
 */
class Event {
	private String name;
	private int startTime;
	private int endTime;
	/**
	 * Constructor creates an event with the specified parameters
	 * @param startTime int representing the hour the event's start time
	 * @param endTime int representing the hour the event's end time
	 * @param name String of the event's name
	 */
	public Event(String name, int startTime, int endTime) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	/**
	 * gets the event's start time
	 * @return int start time of the event
	 */
	public int getStartTime() {
		return startTime;
	}
	/**
	 * gets the event's end time
	 * @return int end time of the event
	 */
	public int getEndTime() {
		return endTime;
	}
	/**
	 * method will return the event's name as a String
	 * @return the event's name
	 */
	public String getName() {
		return name;
	}
}