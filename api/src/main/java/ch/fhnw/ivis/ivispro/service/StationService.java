package ch.fhnw.ivis.ivispro.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Collections;
import java.util.List;

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
		log.info("StationService initialized, loading frequency data.");

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
			events = new CsvToBeanBuilder<TrainEvent>(reader).withSeparator(';').withType(TrainEvent.class).build().parse();
			elapsedTime = System.currentTimeMillis() - startTime;
			log.info("loaded {} train events in {}ms from {}", events.size(), elapsedTime, FREQUENCY_FILE);
		} catch (IllegalStateException | FileNotFoundException e) {
			log.error("unable to initialize station service", e);
			throw new RuntimeException(e); // re-throw
		}

	}

	public List<StationDto> getAllStations(int start, int end) {
		List<StationDto> stationDtos = StationDto.fromList(stations);

		// descending by frequency
		Collections.sort(stationDtos);
		Collections.reverse(stationDtos);

		stationDtos = stationDtos.subList(start, end);

		// calculate dimension for each station
		float ratioPerPosition = 100f / stationDtos.size();
		for (int i = 0; i < stationDtos.size(); i++) {
			float dimension = (stationDtos.size() - i) * ratioPerPosition;
			stationDtos.get(i).setDimension(dimension);
		}

		return stationDtos;
	}

}
