package ch.fhnw.ivis.ivispro.domain;

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
