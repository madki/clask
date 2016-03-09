package xyz.madki.clask;

import android.net.Uri;

import xyz.madki.clask.api.SlackApi;

public final class SlackUtils {

  private SlackUtils() {
    throw new IllegalStateException("No instances");
  }

  public static String getAuthUrl(String clientId, String scope, String redirectUri) {
    Uri uri = Uri.parse(SlackApi.AUTH_URL)
            .buildUpon()
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("scope", scope)
            .appendQueryParameter("redirect_uri", redirectUri)
            .build();
    return uri.toString();
  }


}
