package com.example.iot.Repository;

import com.example.iot.Entity.PeopleCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Map;


public interface PeopleCountRepository extends JpaRepository<PeopleCount, Long> {
    @Query("SELECT new map(FUNCTION('DATE_FORMAT', p.timestamp, '%Y-%m-%d %H:00:00') AS timestamp, AVG(p.count) AS count) FROM PeopleCount p WHERE p.timestamp >= CURRENT_DATE GROUP BY timestamp ORDER BY timestamp")
    List<Map<String, Object>> getHourlyPeopleCount();

    @Query("SELECT p FROM PeopleCount p ORDER BY p.timestamp DESC LIMIT 1")
    PeopleCount getLastPeopleCount();

    @Query("SELECT p FROM PeopleCount p ORDER BY p.timestamp DESC LIMIT 50")
    List<PeopleCount> getRecentPeopleCountTimeline();
}