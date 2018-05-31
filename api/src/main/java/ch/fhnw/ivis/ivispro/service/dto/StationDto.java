package ch.fhnw.ivis.ivispro.service.dto;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ivis.ivispro.domain.Station;

public class StationDto implements Comparable<StationDto> {

	private String name;
	private String color;
	private float dimension;
	private float frequency;
	private float workFrequency;
	private int delayedTrains;

	private GeoLocationDto location;

	public StationDto(Station station) {
		this.name = station.getName();
		this.frequency = station.getWeekFrequency();
		this.workFrequency = station.getWorkWeekFrequency();
		this.location = new GeoLocationDto(station.getCoordinates());
		this.setDelayedTrains(station.getTrainsDelayed());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public float getDimension() {
		return dimension;
	}

	public void setDimension(float dimension) {
		this.dimension = dimension;
	}

	public GeoLocationDto getLocation() {
		return location;
	}

	public void setLocation(GeoLocationDto location) {
		this.location = location;
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public float getWorkFrequency() {
		return workFrequency;
	}

	public void setWorkFrequency(float workFrequency) {
		this.workFrequency = workFrequency;
	}

	public int getDelayedTrains() {
		return delayedTrains;
	}

	public void setDelayedTrains(int delayedTrains) {
		this.delayedTrains = delayedTrains;
	}

	public static List<StationDto> fromList(List<Station> list) {
		List<StationDto> dtoList = new ArrayList<>();
		for (Station station : list) {
			if (station.getWeekFrequency() > 0) {
				dtoList.add(new StationDto(station));
			}
		}
		return dtoList;
	}

	@Override
	public int compareTo(StationDto o) {
		return Float.compare(frequency, o.getFrequency());
	}

}
