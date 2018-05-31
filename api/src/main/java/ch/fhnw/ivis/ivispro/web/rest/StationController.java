package ch.fhnw.ivis.ivispro.web.rest;

import java.util.List;

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

	@Autowired
	private StationService stationService;

	@GetMapping
	public ResponseEntity<List<StationDto>> getAllStations(@RequestParam(required = false) String subset) {
		List<StationDto> allStations = stationService.getAllStations(0, 400);
		return new ResponseEntity<>(allStations, HttpStatus.OK);
	}

}
