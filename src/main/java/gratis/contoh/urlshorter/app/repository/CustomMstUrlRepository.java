package gratis.contoh.urlshorter.app.repository;

import java.util.ArrayList;

import gratis.contoh.urlshorter.app.model.MstUrl;
import gratis.contoh.urlshorter.app.model.request.PaginationRequest;
import gratis.contoh.urlshorter.app.util.querybuilder.Criteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomMstUrlRepository {

	Flux<MstUrl> findAll(ArrayList<Criteria> criterias, PaginationRequest paginationRequest);
	
	Mono<Long> countAll(ArrayList<Criteria> criterias);

}
