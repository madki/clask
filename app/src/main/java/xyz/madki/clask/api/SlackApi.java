package xyz.madki.clask.api;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import xyz.madki.clask.api.models.Team;
import xyz.madki.clask.api.models.Members;

public interface SlackApi {
  String BASE_URL = "https://slack.com/api/";
  String AUTH_URL = "https://slack.com/oauth/authorize";
  String REDIRECT_URL = "http://madki.xyz/clask";

  String SCOPE = "users:read";
  String CLIENT_ID = "2316821691.25382988359";
  String CLIENT_SECRET = "d61ad238067093d21d0fd453bef0a67c";


  @GET("oauth.access")
  Observable<Team> getToken(@Query("client_id") String clientId,
                            @Query("client_secret") String clientSecret,
                            @Query("code") String code,
                            @Query("redirect_uri") String redirectUri);


  @GET("users.list")
  Observable<Members> getMembers(@Query("token") String token);
}
