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

}
