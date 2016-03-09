package xyz.madki.clask.adapter;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.f2prateek.rx.preferences.Preference;
import com.google.gson.Gson;

import javax.inject.Inject;

import xyz.madki.clask.api.models.Team;
import xyz.madki.clask.scope.PerApp;

@PerApp
public class TeamAdapter implements Preference.Adapter<Team> {
  private final Gson gson;

  @Inject
  public TeamAdapter(Gson gson) {
    this.gson = gson;
  }

  @Override
  public Team get(@NonNull String key, @NonNull SharedPreferences preferences) {
    String teamString = preferences.getString(key, null);
    if(teamString != null) return gson.fromJson(teamString, Team.class);
    return null;
  }

  @Override
  public void set(@NonNull String key, @NonNull Team value, @NonNull SharedPreferences.Editor editor) {
    editor.putString(key, gson.toJson(value)).apply();
  }
}
