package ch.fhnw.ivis.ivispro.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBeanBuilder;

import ch.fhnw.ivis.ivispro.domain.Station;
import ch.fhnw.ivis.ivispro.domain.TrainEvent;
import ch.fhnw.ivis.ivispro.service.dto.StationDto;

@Service
public class StationService {

    private final Logger log = LoggerFactory.getLogger(StationService.class);

    private final String FREQUENCY_FILE = "ch/fhnw/ivis/ivispro/service/passenger_frequency_sbb_2016.csv";
    private final String TRAIN_DATA = "ch/fhnw/ivis/ivispro/service/recent_train_data.csv";

    private List<Station> stations;
    private List<TrainEvent> events;

    @PostConstruct
    public void init() {
        log.info("StationService initialized, loading data.");

        try {
            long startTime = System.currentTimeMillis();
            URL url = getClass().getClassLoader().getResource(FREQUENCY_FILE);
            FileReader reader = new FileReader(new File(url.getFile()));
            stations = new CsvToBeanBuilder<Station>(reader).withSeparator(';').withType(Station.class).build().parse();
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("loaded {} stations in {}ms from {}", stations.size(), elapsedTime, FREQUENCY_FILE);

            startTime = System.currentTimeMillis();
            url = getClass().getClassLoader().getResource(TRAIN_DATA);
            reader = new FileReader(new File(url.getFile()));
            events = new CsvToBeanBuilder<TrainEvent>(reader).withSeparator(';').withType(TrainEvent.class).build()
                    .parse();
            elapsedTime = System.currentTimeMillis() - startTime;
            log.info("loaded {} train events in {}ms from {}", events.size(), elapsedTime, FREQUENCY_FILE);

            log.info("assigning train events to stations");
            startTime = System.currentTimeMillis();
            for (Station station : stations) {
                List<TrainEvent> found = events.stream()
                        .filter(e -> e.getStationName().equalsIgnoreCase(station.getName()))
                        .collect(Collectors.toList());
                station.setEvents(found);

                // calc some statistics
                int delayedTrains = found.stream().filter(e -> e.isHasArrivalDelay() || e.isHasDepartureDelay())
                        .collect(Collectors.toList()).size();
                int trainsOnTime = found.size() - delayedTrains;
                float delayRatio = delayedTrains / (float) (found.size() / 100);

                station.setDelayRatio(delayRatio);
                station.setTrainsOnTime(trainsOnTime);
                station.setTrainsDelayed(delayedTrains);
            }
            elapsedTime = System.currentTimeMillis() - startTime;
            log.info("assignment of {} train events took {}ms", events.size(), elapsedTime);

        } catch (IllegalStateException | FileNotFoundException e) {
            log.error("unable to initialize station service", e);
            throw new RuntimeException(e); // re-throw
        }

    }

    public List<StationDto> getAllStations(String frequencyPercentageFrom, String frequencyPercentageTo) {
        List<StationDto> stationDtos = StationDto.fromList(stations);

        Collections.sort(stationDtos);
        stationDtos = filterStations(frequencyPercentageFrom, frequencyPercentageTo, stationDtos);
        // descending by frequency
        Collections.reverse(stationDtos);

        // max delays of a station
        int maxDelays = stations.stream().max(Comparator.comparing(Station::getTrainsDelayed)).get().getTrainsDelayed();
        int minDelays = stations.stream().min(Comparator.comparing(Station::getTrainsDelayed)).get().getTrainsDelayed();

        // calculate dimension for each station
        float ratioPerPosition = 100f / stationDtos.size();
        for (int i = 0; i < stationDtos.size(); i++) {
            float dimension = (stationDtos.size() - i) * ratioPerPosition;
            stationDtos.get(i).setDimension(dimension);
            stationDtos.get(i).setColor(calcColor(maxDelays, minDelays, stationDtos.get(i).getDelayedTrains()));
        }

        return stationDtos;
    }

    private List<StationDto> filterStations(String frequencyPercentageFrom, String frequencyPercentageTo, List<StationDto> stationDtos) {
        // filter ascending by frequency
        if (frequencyPercentageFrom != null && frequencyPercentageTo != null) {
            int frequencyFromNumerical = Integer.valueOf(frequencyPercentageFrom);
            int frequencyToNumerical = Integer.valueOf(frequencyPercentageTo);
            int amountOfStations = stationDtos.size();
            int indexFrom = frequencyFromNumerical * amountOfStations / 100;
            int indexTo = frequencyToNumerical * amountOfStations / 100;
            stationDtos = stationDtos.stream().skip(indexFrom).limit(indexTo - indexFrom).collect(Collectors.toList());
        }
        return stationDtos;
    }

    private String calcColor(int max, int min, int value) {
        assert max > min && value <= max && value >= min;

        float valueRatio = value / (float) max;

        int red = (int) (255f * valueRatio);
        int green = (int) (255f - red);
        int blue = (int) (4f - 4 * valueRatio);

        return String.format("#%02x%02x%02x", red, green, blue);
    }

}
