package com.example.iot.Repository;

import com.example.iot.Entity.PeopleCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Date;

public interface PeopleCountRepository extends JpaRepository<PeopleCount, Long> {
    @Query("SELECT new map(FUNCTION('DATE_FORMAT', p.timestamp, '%Y-%m-%d %H:00:00') AS timestamp, AVG(p.count) AS count) " +
            "FROM PeopleCount p WHERE DATE(p.timestamp) = :selectedDate GROUP BY timestamp ORDER BY timestamp")
    List<Map<String, Object>> getHourlyPeopleCountByDate(@Param("selectedDate") Date selectedDate);

    @Query("SELECT p FROM PeopleCount p WHERE DATE(p.timestamp) = :selectedDate ORDER BY p.timestamp DESC")
    List<PeopleCount> getRecentPeopleCountTimelineByDate(@Param("selectedDate") Date selectedDate);

    @Query("SELECT p FROM PeopleCount p ORDER BY p.timestamp DESC LIMIT 1")
    PeopleCount getLastPeopleCount();
}
