package com.example.iot.Controller;

import com.example.iot.Entity.PeopleCount;
import com.example.iot.Service.PeopleCountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class DisplayController {
    private final PeopleCountService peopleCountService;

    public DisplayController(PeopleCountService peopleCountService) {
        this.peopleCountService = peopleCountService;
    }

    @GetMapping("/display")
    public String display(@RequestParam(required = false) String selectedDate, Model model) {
        if (selectedDate == null || selectedDate.isEmpty()) {
            selectedDate = LocalDate.now().toString();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(selectedDate, formatter);
        Date date = java.sql.Date.valueOf(localDate);
        PeopleCount currentCount = peopleCountService.getPeopleCount();
        model.addAttribute("currentCount", currentCount);
        List<Map<String, Object>> hourlyData = peopleCountService.getHourlyPeopleCountByDate(date);
        model.addAttribute("hourlyData", hourlyData);
        List<PeopleCount> timelineData = peopleCountService.getRecentPeopleCountTimelineByDate(date);
        model.addAttribute("timelineData", timelineData);
        model.addAttribute("currentCountFormatted", currentCount.getFormattedTimestamp());

        return "dashboard";
    }
}
