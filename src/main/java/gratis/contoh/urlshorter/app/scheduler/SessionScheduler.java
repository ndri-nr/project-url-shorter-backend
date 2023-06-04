package gratis.contoh.urlshorter.app.scheduler;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gratis.contoh.urlshorter.app.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SessionScheduler {
	
	@Autowired
	private SessionRepository service;
	
	@Scheduled(cron = "59 59 23 * * *")
    public void deleteAllInactiveSession() {
		log.info("start to prepare session scheduler...");
        service.deleteAllInactiveSession(LocalDateTime.now())
        	.subscribe(item -> log.info(item + " inactive session(s) deleted"));
    }
	
}
