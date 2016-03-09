package xyz.madki.clask.adapter;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.f2prateek.rx.preferences.Preference;
import com.google.gson.Gson;

import javax.inject.Inject;

import xyz.madki.clask.api.models.Members;
import xyz.madki.clask.scope.PerApp;

@PerApp
public class MembersAdapter implements Preference.Adapter<Members> {
  private final Gson gson;

  @Inject
  public MembersAdapter(Gson gson) {
    this.gson = gson;
  }

  @Override
  public Members get(@NonNull String key, @NonNull SharedPreferences preferences) {
    String membersStr = preferences.getString(key, null);
    if (membersStr != null) return gson.fromJson(membersStr, Members.class);
    return null;
  }

  @Override
  public void set(@NonNull String key, @NonNull Members value, @NonNull SharedPreferences.Editor editor) {
    editor.putString(key, gson.toJson(value)).apply();
  }
}
