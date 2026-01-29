package com.example.events.concurrency;

import com.example.events.dto.EventRequest;
import com.example.events.repository.MachineEventRepository;
import com.example.events.service.EventIngestionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConcurrentIngestionTest {

    @Autowired
    private EventIngestionService ingestionService;

    @Autowired
    private MachineEventRepository repository;

    @Test
    void concurrentIngestion_sameEventId_onlyOneWins() throws Exception {

        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        EventRequest request = new EventRequest();
        request.eventId = "E-CONCURRENT";
        request.eventTime = Instant.now();
        request.machineId = "M-001";
        request.lineId = "L-01";
        request.durationMs = 1000;
        request.defectCount = 1;

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    ingestionService.ingest(List.of(request));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // 🔍 VERIFY
        assertThat(repository.count()).isEqualTo(1);

        var event = repository.findById("E-CONCURRENT").orElseThrow();
        assertThat(event.getDefectCount()).isEqualTo(1);
    }
}

