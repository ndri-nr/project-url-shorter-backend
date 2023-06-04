package gratis.contoh.urlshorter.app.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import gratis.contoh.urlshorter.app.model.MstUrl;
import reactor.core.publisher.Flux;

@Repository
public interface MstUrlRepository extends R2dbcRepository<MstUrl, Long> {

	Flux<MstUrl> findByUrlIdAndDeletedAtIsNull(String urlId);
	Flux<MstUrl> findByUrlIdAndAccountIdAndDeletedAtIsNull(String urlId, Long accountId);

}
