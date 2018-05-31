package ch.fhnw.ivis.ivispro.service.dto;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ivis.ivispro.domain.Station;

public class StationDto {

	private String name;
	private String color;
	private float dimension;

	private GeoLocationDto location;

	public StationDto(Station station) {
		this.name = station.getName();
		this.location = new GeoLocationDto(station.getCoordinates());
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

	public static List<StationDto> fromList(List<Station> list) {
		List<StationDto> dtoList = new ArrayList<>();
		for (Station station : list) {
			dtoList.add(new StationDto(station));
		}
		return dtoList;
	}

}
