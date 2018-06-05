package ch.fhnw.ivis.ivispro.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.ivis.ivispro.service.StationService;
import ch.fhnw.ivis.ivispro.service.dto.StationDto;

@RestController
@RequestMapping("/api/station")
public class StationController {

    private final Logger log = LoggerFactory.getLogger(StationController.class);

    @Autowired
    private StationService stationService;

    @GetMapping
    public ResponseEntity<List<StationDto>> getAllStations(@RequestParam(required = false) String subset,
                                                           @RequestParam(required = false) String frequencyFrom,
                                                           @RequestParam(required = false) String frequencyTo) {
        long startTime = System.currentTimeMillis();
        log.info("new request to get all station data");
        List<StationDto> allStations = stationService.getAllStations(frequencyFrom, frequencyTo);
        log.info("served get all station data request within {}ms", System.currentTimeMillis() - startTime);
        return new ResponseEntity<>(allStations, HttpStatus.OK);
    }

}
