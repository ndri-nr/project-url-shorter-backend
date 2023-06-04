package gratis.contoh.urlshorter.app.repository.query;

public class MstUrlQuery {
	
	public static final String getUrl = "SELECT a.id, "
			+ "a.url_id as \"url\", "
			+ "a.account_id, "
			+ "a.visited as \"totalVisited\", "
			+ "a.redirect_to as \"redirectTo\", "
			+ "a.last_visited as \"lastVisited\", "
			+ "a.deleted_at "
			+ "FROM mst_url a";

}
