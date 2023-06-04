package gratis.contoh.urlshorter.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gratis.contoh.mapper.FieldMapper;
import gratis.contoh.mapper.MapperTemplate;
import gratis.contoh.mapper.ObjectMapper;
import gratis.contoh.urlshorter.app.model.MstAccount;
import gratis.contoh.urlshorter.app.model.MstUrl;
import gratis.contoh.urlshorter.app.model.dto.RegisterDto;
import gratis.contoh.urlshorter.app.model.dto.UrlDto;
import gratis.contoh.urlshorter.app.model.response.ProfileResponse;
import gratis.contoh.urlshorter.app.model.response.RegisterResponse;
import gratis.contoh.urlshorter.app.model.response.UrlResponse;

@Configuration
public class AutoMapperConfiguration {
	
	@Bean
	ObjectMapper<RegisterDto, MstAccount> registerDtoToMstAccount() {
	    return new MapperTemplate<>(
	    		RegisterDto.class, 
	    		MstAccount.class);
	}
	
	@Bean
	ObjectMapper<MstAccount, RegisterResponse> mstAccountToRegisterResponse() {
	    return new MapperTemplate<>(
	    		MstAccount.class, 
	    		RegisterResponse.class);
	}
	
	@Bean
	ObjectMapper<UrlDto, MstUrl> urlDtoToMstUrl() {
	    return new MapperTemplate<>(
	    		UrlDto.class, 
	    		MstUrl.class,
	    		new FieldMapper("url", "redirectTo"));
	}
	
	@Bean
	ObjectMapper<MstUrl, UrlResponse> mstUrlToUrlResponse() {
	    return new MapperTemplate<>(
	    		MstUrl.class, 
	    		UrlResponse.class,
	    		new FieldMapper("urlId", "url"),
	    		new FieldMapper("visited", "totalVisited"));
	}

	@Bean
	ObjectMapper<MstAccount, ProfileResponse> mstAccountToProfileResponse() {
	    return new MapperTemplate<>(
	    		MstAccount.class, 
	    		ProfileResponse.class);
	}
	
	@Bean
	ObjectMapper<Object, MstUrl> objectToMstUrl() {
		return new MapperTemplate<>(
				Object.class, 
				MstUrl.class,
	    		new FieldMapper("totalVisited", "visited"),
	    		new FieldMapper("url", "urlId"));
	}
	
	@Bean
	ObjectMapper<Object, Long> objectToLong() {
		return new MapperTemplate<>(
				Object.class, 
				Long.class);
	}

}
