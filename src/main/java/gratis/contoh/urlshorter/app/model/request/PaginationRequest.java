package gratis.contoh.urlshorter.app.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequest {
	
	@NotNull
	private Integer page;
	
	@NotNull
	private Integer size;
	
	private String sort;
	
	private String search;
}
