package com.example.iot.Service;

import com.example.iot.Entity.PeopleCount;
import com.example.iot.Repository.PeopleCountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Date;

@Service
public class PeopleCountService {
    private final PeopleCountRepository peopleCountRepository;

    public PeopleCountService(PeopleCountRepository peopleCountRepository) {
        this.peopleCountRepository = peopleCountRepository;
    }

    public List<Map<String, Object>> getHourlyPeopleCountByDate(Date selectedDate) {
        return peopleCountRepository.getHourlyPeopleCountByDate(selectedDate);
    }

    public List<PeopleCount> getRecentPeopleCountTimelineByDate(Date selectedDate) {
        return peopleCountRepository.getRecentPeopleCountTimelineByDate(selectedDate);
    }

    public PeopleCount getPeopleCount() {
        return peopleCountRepository.getLastPeopleCount();
    }
}
