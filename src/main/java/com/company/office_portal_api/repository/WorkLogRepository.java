package com.company.office_portal_api.repository;

import com.company.office_portal_api.model.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

    @Query("""
        SELECT w FROM WorkLog w
        WHERE w.email = :email
          AND w.logoutTime IS NULL
    """)
    Optional<WorkLog> findActive(String email);

    @Query("""
        SELECT w FROM WorkLog w
        WHERE w.email = :email
          AND DATE(w.loginTime) = :date
          AND w.logoutTime IS NULL
    """)
    Optional<WorkLog> findActiveLog(String email, LocalDate date);

    @Query("""
        SELECT COALESCE(SUM(w.workedMinutes),0)
        FROM WorkLog w
        WHERE w.email = :email
          AND DATE(w.loginTime) = :date
    """)
    Long totalMinutesToday(String email, LocalDate date);

    @Query("""
    SELECT w FROM WorkLog w
    WHERE w.email = :email
      AND DATE(w.loginTime) = :date
""")
Optional<WorkLog> findToday(String email, LocalDate date);

    @Query("""
  SELECT w.email, SUM(w.workedMinutes)
  FROM WorkLog w
  WHERE DATE(w.loginTime) = CURRENT_DATE
  GROUP BY w.email
""")
List<Object[]> getTodayReport();

}
