package com.example.iot.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {
    public static void main(String[] args) {
        // Giả sử thời gian lấy từ Arduino là một Instant
        Instant instant = Instant.now(); // Lấy thời gian hiện tại (UTC)

        // Chuyển đổi sang giờ Việt Nam (UTC+7)
        ZonedDateTime vietnamTime = instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));

        // Hiển thị thời gian dưới định dạng YYYY-MM-DD HH:mm:ss
        String formattedTime = vietnamTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("Thời gian theo giờ Việt Nam: " + formattedTime);
    }
}