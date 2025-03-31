package com.example.iot.Controller;

import com.example.iot.Entity.PeopleCount;
import com.example.iot.Service.PeopleCountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class DisplayController {
    private final PeopleCountService peopleCountService;

    public DisplayController(PeopleCountService peopleCountService) {
        this.peopleCountService = peopleCountService;
    }

    @GetMapping("/display")
    public String display(Model model) {
        // Get current count
        PeopleCount currentCount = peopleCountService.getPeopleCount();
        model.addAttribute("currentCount", currentCount);

        // Get hourly data
        List<Map<String, Object>> hourlyData = peopleCountService.getHourlyPeopleCount();
        model.addAttribute("hourlyData", hourlyData);

        // Get timeline data
        List<PeopleCount> timelineData = peopleCountService.getRecentPeopleCountTimeline();
        model.addAttribute("timelineData", timelineData);

        return "dashboard";
    }
}