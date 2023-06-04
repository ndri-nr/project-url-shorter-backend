package gratis.contoh.urlshorter.app.model.response;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse implements Serializable {

	private static final long serialVersionUID = -7962121504868524451L;
	
	@NotNull
	private String accessToken;

}
