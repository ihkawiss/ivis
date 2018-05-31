package ch.fhnw.ivis.ivispro.service.dto;

public class GeoLocationDto {

	private double longitude;
	private double latitude;

	public GeoLocationDto(String coordinates) {
		String[] tuple = coordinates.replaceAll("\\s+", "").split(",");
		longitude = Double.parseDouble(tuple[0]);
		latitude = Double.parseDouble(tuple[1]);
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}
