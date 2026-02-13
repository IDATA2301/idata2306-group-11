package com.roamroute.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.roamroute.backend.entity.Destination;
import com.roamroute.backend.repository.DestinationRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    private final DestinationRepository destinationRepository;

    public DestinationController(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    @GetMapping
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }
    
}
