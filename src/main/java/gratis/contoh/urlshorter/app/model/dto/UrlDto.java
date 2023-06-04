package gratis.contoh.urlshorter.app.model.dto;

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
public class UrlDto implements Serializable {

	private static final long serialVersionUID = 2128066004562579809L;

	@NotNull
	private String url;

}
