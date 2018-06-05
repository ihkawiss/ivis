package ch.fhnw.ivis.ivispro.service.dto;

import ch.fhnw.ivis.ivispro.domain.MinuteDelayAmount;
import ch.fhnw.ivis.ivispro.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class StationDto implements Comparable<StationDto> {

    private String name;
    private String color;
    private float dimension;
    private float frequency;
    private float workFrequency;
    private int delayedTrains;
    private int trainsOnTime;
    private int delayRatio;
    private int totalTrains;
    private List<MinuteDelayAmount> delays;

    private GeoLocationDto location;

    public StationDto(Station station) {
        this.name = station.getName();
        this.frequency = station.getWeekFrequency();
        this.workFrequency = station.getWorkWeekFrequency();
        this.location = new GeoLocationDto(station.getCoordinates());
        this.delayedTrains = station.getTrainsDelayed();
        this.trainsOnTime = station.getTrainsOnTime();
        this.totalTrains = trainsOnTime + delayedTrains;
        this.delayRatio = (int) (delayedTrains == 0 ? 0.0f : delayedTrains / (float) (totalTrains) * 100);
        this.delays = station.getAmountsOfDelays();
    }

    public int getTrainsOnTime() {
        return trainsOnTime;
    }

    public void setTrainsOnTime(int trainsOnTime) {
        this.trainsOnTime = trainsOnTime;
    }

    public int getDelayRatio() {
        return delayRatio;
    }

    public void setDelayRatio(int delayRatio) {
        this.delayRatio = delayRatio;
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

    public int getTotalTrains() {
        return totalTrains;
    }

    public void setTotalTrains(int totalTrains) {
        this.totalTrains = totalTrains;
    }

    public List<MinuteDelayAmount> getDelays() {
        return delays;
    }

    public void setDelays(List<MinuteDelayAmount> delays) {
        this.delays = delays;
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
