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
public class RegisterResponse implements Serializable {

	private static final long serialVersionUID = -8026635588496931244L;

	@NotNull
    private String fullName;
	
	@NotNull
    private String email;

}
