package ch.fhnw.ivis.ivispro.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBeanBuilder;

import ch.fhnw.ivis.ivispro.domain.Station;
import ch.fhnw.ivis.ivispro.service.dto.StationDto;

@Service
public class StationService {

	private final Logger log = LoggerFactory.getLogger(StationService.class);

	private final String FREQUENCY_FILE = "ch/fhnw/ivis/ivispro/service/passenger_frequency_sbb_2016.csv";

	private List<Station> stations;

	@PostConstruct
	public void init() {
		log.info("StationService initialized, loading frequency data.");

		try {
			URL url = getClass().getClassLoader().getResource(FREQUENCY_FILE);
			FileReader reader = new FileReader(new File(url.getFile()));
			stations = new CsvToBeanBuilder<Station>(reader).withSeparator(';').withType(Station.class).build().parse();
			log.info("loaded {} stations from {}", stations.size(), FREQUENCY_FILE);
		} catch (IllegalStateException | FileNotFoundException e) {
			log.error("unable to initialize station service", e);
			throw new RuntimeException(e); // re-throw
		}

	}

	public List<StationDto> getAllStations() {
		return StationDto.fromList(stations);
	}

}
