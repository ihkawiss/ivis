package ch.fhnw.ivis.ivispro.domain;

import java.util.List;

import com.opencsv.bean.CsvBindByName;

/**
 * Representing a train station, made compatible with OpenCSV.
 */
public class Station {

	@CsvBindByName(column = "Bahnhof_Haltestelle", required = true)
	private String name;

	@CsvBindByName(column = "DTV")
	private float workWeekFrequency;

	@CsvBindByName(column = "DMW")
	private float weekFrequency;

	@CsvBindByName(column = "geopos", required = true)
	private String coordinates;

	private List<TrainEvent> events;

	private int trainsOnTime;

	private int trainsDelayed;

	private float delayRatio;

	public int getTrainsOnTime() {
		return trainsOnTime;
	}

	public void setTrainsOnTime(int trainsOnTime) {
		this.trainsOnTime = trainsOnTime;
	}

	public int getTrainsDelayed() {
		return trainsDelayed;
	}

	public void setTrainsDelayed(int trainsDelayed) {
		this.trainsDelayed = trainsDelayed;
	}

	public float getDelayRatio() {
		return delayRatio;
	}

	public void setDelayRatio(float delayRatio) {
		this.delayRatio = delayRatio;
	}

	public List<TrainEvent> getEvents() {
		return events;
	}

	public void setEvents(List<TrainEvent> events) {
		this.events = events;
	}

	public void setWorkWeekFrequency(float workWeekFrequency) {
		this.workWeekFrequency = workWeekFrequency;
	}

	public void setWeekFrequency(float weekFrequency) {
		this.weekFrequency = weekFrequency;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getWorkWeekFrequency() {
		return workWeekFrequency;
	}

	public void setWorkWeekFrequency(int workWeekFrequency) {
		this.workWeekFrequency = workWeekFrequency;
	}

	public float getWeekFrequency() {
		return weekFrequency;
	}

	public void setWeekFrequency(int weekFrequency) {
		this.weekFrequency = weekFrequency;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

}
