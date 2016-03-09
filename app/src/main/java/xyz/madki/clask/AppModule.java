package xyz.madki.clask;

import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import dagger.Module;
import dagger.Provides;
import xyz.madki.clask.adapter.MembersAdapter;
import xyz.madki.clask.adapter.TeamAdapter;
import xyz.madki.clask.api.models.Members;
import xyz.madki.clask.api.models.Team;
import xyz.madki.clask.scope.PerApp;

@Module
public class AppModule {
  private final App app;

  public AppModule(App app) {
    this.app = app;
  }

  @Provides
  public App provideApp() {
    return app;
  }

  @Provides @PerApp
  public RxSharedPreferences providePreferences() {
    return RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(app));
  }

  @Provides @PerApp
  public Preference<Team> provideTeamPref(RxSharedPreferences preferences, TeamAdapter adapter) {
    return preferences.getObject(PrefKeys.TEAM, adapter);
  }

  @Provides @PerApp
  public Preference<Members> provideMembersPref(RxSharedPreferences preferences, MembersAdapter adapter) {
    return preferences.getObject(PrefKeys.MEMBERS, adapter);
  }
}
