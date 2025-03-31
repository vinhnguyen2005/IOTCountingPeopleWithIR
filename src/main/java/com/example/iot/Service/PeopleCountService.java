package com.example.iot.Service;

import com.example.iot.Entity.PeopleCount;
import com.example.iot.Repository.PeopleCountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PeopleCountService {
    private final PeopleCountRepository peopleCountRepository;

    public PeopleCountService(PeopleCountRepository peopleCountRepository) {
        this.peopleCountRepository = peopleCountRepository;
    }

    public List<Map<String, Object>> getHourlyPeopleCount() {
        return peopleCountRepository.getHourlyPeopleCount();
    }

    public PeopleCount getPeopleCount() {
        return peopleCountRepository.getLastPeopleCount();
    }

    public List<PeopleCount> getRecentPeopleCountTimeline() {
        return peopleCountRepository.getRecentPeopleCountTimeline();
    }
}