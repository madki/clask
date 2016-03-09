package xyz.madki.clask;


import com.f2prateek.rx.preferences.Preference;
import com.google.gson.Gson;

import dagger.Component;
import xyz.madki.clask.api.ApiComponent;
import xyz.madki.clask.api.SlackApi;
import xyz.madki.clask.api.models.Members;
import xyz.madki.clask.api.models.Team;
import xyz.madki.clask.scope.PerApp;

@PerApp
@Component(dependencies = {ApiComponent.class}, modules = AppModule.class)
public interface AppComponent {
  void inject(App app);
  SlackApi api();
  Gson gson();
  Preference<Team> teamPref();
  Preference<Members> membersPref();
}
