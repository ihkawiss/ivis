package ch.fhnw.ivis.ivispro.domain;

import java.util.Date;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

public class TrainEvent {

	@CsvBindByName(column = "VERKEHRSMITTEL_TEXT")
	private String vehicle;

	@CsvBindByName(column = "HALTESTELLEN_NAME")
	private String stationName;

	@CsvBindByName(column = "ANKUNFTSZEIT")
	@CsvDate("yyyy-MM-dd'T'hh:mm:ss")
	private Date plannedArrival;

	@CsvBindByName(column = "AN_PROGNOSE")
	@CsvDate("yyyy-MM-dd'T'hh:mm:ss")
	private Date actualArrival;

	@CsvBindByName(column = "ABFAHRTSZEIT")
	@CsvDate("yyyy-MM-dd'T'hh:mm:ss")
	private Date plannedDeparture;

	@CsvBindByName(column = "ABFAHRTSZEIT")
	@CsvDate("yyyy-MM-dd'T'hh:mm:ss")
	private Date actualDeparture;

	@CsvBindByName(column = "ankunftsverspatung")
	private boolean hasArrivalDelay;

	@CsvBindByName(column = "abfahrtsverspatung")
	private boolean hasDepartureDelay;

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Date getPlannedArrival() {
		return plannedArrival;
	}

	public void setPlannedArrival(Date plannedArrival) {
		this.plannedArrival = plannedArrival;
	}

	public Date getActualArrival() {
		return actualArrival;
	}

	public void setActualArrival(Date actualArrival) {
		this.actualArrival = actualArrival;
	}

	public Date getPlannedDeparture() {
		return plannedDeparture;
	}

	public void setPlannedDeparture(Date plannedDeparture) {
		this.plannedDeparture = plannedDeparture;
	}

	public Date getActualDeparture() {
		return actualDeparture;
	}

	public void setActualDeparture(Date actualDeparture) {
		this.actualDeparture = actualDeparture;
	}

	public boolean isHasArrivalDelay() {
		return hasArrivalDelay;
	}

	public void setHasArrivalDelay(boolean hasArrivalDelay) {
		this.hasArrivalDelay = hasArrivalDelay;
	}

	public boolean isHasDepartureDelay() {
		return hasDepartureDelay;
	}

	public void setHasDepartureDelay(boolean hasDepartureDelay) {
		this.hasDepartureDelay = hasDepartureDelay;
	}

}
