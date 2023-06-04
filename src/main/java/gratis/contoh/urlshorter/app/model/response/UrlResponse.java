package gratis.contoh.urlshorter.app.model.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlResponse implements Serializable {
	
	private static final long serialVersionUID = -8241560540786167041L;
	
	@NotNull
	private String url;
	
	@NotNull
	private String redirectTo;
	
	@NotNull
	private String totalVisited;
	
	@NotNull
	private LocalDateTime lastVisited;

}
