package com.example.events.repository;

import com.example.events.models.MachineEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MachineEventRepository extends JpaRepository<MachineEvent, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from MachineEvent e where e.eventId = :eventId")
    Optional<MachineEvent> findByIdForUpdate(@Param("eventId") String eventId);

    List<MachineEvent> findByMachineIdAndEventTimeBetween(
        String machineId,
        Instant start,
        Instant end
    );
    
    long countByMachineIdAndEventTimeGreaterThanEqualAndEventTimeLessThan(
            String machineId,
            Instant start,
            Instant end
    );
    
    
    
    @Query("""
    	    SELECT COALESCE(SUM(e.defectCount), 0)
    	    FROM MachineEvent e
    	    WHERE e.machineId = :machineId
    	      AND e.eventTime >= :start
    	      AND e.eventTime < :end
    	      AND e.defectCount != -1
    	""")

    long sumDefects(
            @Param("machineId") String machineId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );
}