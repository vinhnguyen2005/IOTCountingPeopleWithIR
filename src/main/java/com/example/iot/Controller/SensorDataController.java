package com.example.iot.Controller;

import com.example.iot.DTO.PeopleData;
import com.example.iot.Entity.PeopleCount;
import com.example.iot.Repository.PeopleCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SensorDataController {

    private final PeopleCountRepository repository;

    @Autowired
    public SensorDataController(PeopleCountRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/data")
    public ResponseEntity<PeopleData> receiveData(@RequestBody PeopleData data) {
        System.out.println("Received JSON: " + data);

        if (data == null || data.getCount() < 0) {
            return ResponseEntity.badRequest().body(null);
        }

        PeopleCount peopleCount = new PeopleCount();
        peopleCount.setCount(data.getCount());
        repository.save(peopleCount);

        PeopleData responseData = new PeopleData(peopleCount.getCount(), peopleCount.getTimestamp());

        return ResponseEntity.ok(responseData);
    }


    @GetMapping("/data")
    public List<PeopleCount> getAllData() {
        return repository.findAll();
    }
}
