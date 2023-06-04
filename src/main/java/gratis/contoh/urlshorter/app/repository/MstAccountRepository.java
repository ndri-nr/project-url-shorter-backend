package gratis.contoh.urlshorter.app.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import gratis.contoh.urlshorter.app.model.MstAccount;
import reactor.core.publisher.Flux;

@Repository
public interface MstAccountRepository extends R2dbcRepository<MstAccount, Long> {
	
	Flux<MstAccount> findByEmail(String email);

}
