package gratis.contoh.urlshorter.app.repository.query;

public class SessionQuery {
	public static final String deleteAllInactiveSession = "DELETE "
			+ "FROM session s "
			+ "WHERE s.expired_at <= :now";
}
