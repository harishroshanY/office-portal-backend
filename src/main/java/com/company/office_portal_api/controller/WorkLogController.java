package com.company.office_portal_api.controller;

import com.company.office_portal_api.model.WorkLog;
import com.company.office_portal_api.repository.WorkLogRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/work")
@CrossOrigin
public class WorkLogController {

    private final WorkLogRepository workLogRepository;

    public WorkLogController(WorkLogRepository workLogRepository) {
        this.workLogRepository = workLogRepository;
    }

    // 🔹 Start work / resume work
    @PostMapping("/start")
public void startWork(@RequestParam String email) {

    LocalDate today = LocalDate.now();

    Optional<WorkLog> todayLog =
            workLogRepository.findToday(email, today);

    if (todayLog.isPresent()) {
        // Resume existing day → just update loginTime
        WorkLog log = todayLog.get();
        log.setLoginTime(LocalDateTime.now());
        log.setLogoutTime(null); // mark active
        workLogRepository.save(log);
        return;
    }

    // First login of the day → create row
    WorkLog log = new WorkLog();
    log.setEmail(email);
    log.setLoginTime(LocalDateTime.now());
    log.setWorkedMinutes(0L);

    workLogRepository.save(log);
}


    // 🔹 End work (logout)
   @PostMapping("/end")
public void endWork(@RequestParam String email) {

    Optional<WorkLog> todayLog =
            workLogRepository.findToday(email, LocalDate.now());

    if (todayLog.isEmpty()) return;

    WorkLog log = todayLog.get();

    LocalDateTime now = LocalDateTime.now();

    long minutes =
            Duration.between(log.getLoginTime(), now).toMinutes();

    log.setLogoutTime(now);
    log.setWorkedMinutes(
        (log.getWorkedMinutes() == null ? 0 : log.getWorkedMinutes()) + minutes
    );

    workLogRepository.save(log);
}


    // 🔹 Active session (for timer resume)
    @GetMapping("/active")
    public WorkLog getActive(@RequestParam String email) {
        return workLogRepository.findActive(email).orElse(null);
    }

    // 🔹 Total worked minutes today
    @GetMapping("/today")
    public Long todayMinutes(@RequestParam String email) {
        return workLogRepository.totalMinutesToday(email, LocalDate.now());
    }

    @GetMapping("/admin/today")
public List<Map<String, Object>> adminTodayReport() {

    List<Object[]> rows = workLogRepository.getTodayReport();
    List<Map<String, Object>> result = new ArrayList<>();

    for (Object[] row : rows) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", row[0]);
        map.put("minutes", row[1]);
        result.add(map);
    }
    return result;
}

}
