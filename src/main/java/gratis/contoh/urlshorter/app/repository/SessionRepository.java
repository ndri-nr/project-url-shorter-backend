package gratis.contoh.urlshorter.app.repository;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gratis.contoh.urlshorter.app.model.Session;
import gratis.contoh.urlshorter.app.repository.query.SessionQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SessionRepository extends R2dbcRepository<Session, Long> {

	Flux<Session> findByAccountIdAndExpiredAtGreaterThan(Long accountId, LocalDateTime expiredAt);
	Flux<Session> findBySessionIdAndExpiredAtGreaterThan(String sessionId, LocalDateTime expiredAt);
	
	@Modifying
    @Query(SessionQuery.deleteAllInactiveSession)
	Mono<Long> deleteAllInactiveSession(@Param(value = "now") LocalDateTime now);

}
