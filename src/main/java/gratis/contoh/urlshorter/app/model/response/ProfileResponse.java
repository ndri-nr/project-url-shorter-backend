package gratis.contoh.urlshorter.app.model.response;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor 
@AllArgsConstructor
@Builder
@Data
public class ProfileResponse implements Serializable {

	private static final long serialVersionUID = -3592263835898658152L;

	@NotNull
    private String fullName;
	
	@NotNull
    private String email;

}
