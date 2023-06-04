package gratis.contoh.urlshorter.app.repository.impl;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import gratis.contoh.mapper.ObjectMapper;
import gratis.contoh.urlshorter.app.model.MstUrl;
import gratis.contoh.urlshorter.app.model.request.PaginationRequest;
import gratis.contoh.urlshorter.app.repository.CustomMstUrlRepository;
import gratis.contoh.urlshorter.app.repository.query.MstUrlQuery;
import gratis.contoh.urlshorter.app.util.querybuilder.Criteria;
import gratis.contoh.urlshorter.app.util.querybuilder.QueryBuilder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class CustomMstUrlRepositoryImpl implements CustomMstUrlRepository {
	
	private DatabaseClient databaseClient;

    public CustomMstUrlRepositoryImpl(DatabaseClient databaseClient) {
    	this.databaseClient = databaseClient;
	}
    
    @Autowired
    private ObjectMapper<Object, MstUrl> objectToMstUrl;
    
    @Autowired
    private ObjectMapper<Object, Long> objectToLong;

	@Override
	public Flux<MstUrl> findAll(ArrayList<Criteria> criterias, PaginationRequest paginationRequest) {
		String base = MstUrlQuery.getUrl;
		String query = QueryBuilder.builder(base, criterias, paginationRequest);
		
		log.info("prepare statement " + query);

		return databaseClient.sql(query)
                .fetch()
                .all()
                .flatMap(item -> Mono.just(objectToMstUrl.convert(item)));
	}

	@Override
	public Mono<Long> countAll(ArrayList<Criteria> criterias) {
		String base = MstUrlQuery.getUrl;
		String query = QueryBuilder.countBuilder(base, criterias);
		
		log.info("prepare statement " + query);
		
		return databaseClient.sql(query)
                .fetch()
                .one()
                .map(item ->  {
                	Object res = null;
                	for (Map.Entry<String,Object> entry : item.entrySet()) {
                		res = entry.getValue();
                	}
                	return objectToLong.convert(res);
                });
	}

}
