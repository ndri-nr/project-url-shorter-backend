package gratis.contoh.urlshorter.app.exception;

public class BadRequestException extends RuntimeException {
	
	private static final long serialVersionUID = 2812613517227857948L;

	public BadRequestException(String error) {
        super(error);
    }
	
}