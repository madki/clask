package xyz.madki.clask.splash;

import android.os.Bundle;

import com.f2prateek.rx.preferences.Preference;

import javax.inject.Inject;

import xyz.madki.clask.api.models.Team;
import xyz.madki.clask.base.BasePresenter;
import xyz.madki.clask.base.IBundleProvider;
import xyz.madki.clask.scope.PerActivity;

@PerActivity
public class SplashPresenter extends BasePresenter<SplashPresenter.IView> {
  boolean toLogin = true;

  @Inject
  public SplashPresenter(Preference<Team> teamPreference) {
    Team team = teamPreference.get();
    toLogin = (team == null) || (!team.ok());
  }

  @Override
  protected void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);
    if(getView() == null) return;

    if(toLogin) getView().toLogin();
    else getView().toTeam();
  }

  public interface IView extends IBundleProvider {
    void toLogin();
    void toTeam();
  }
}
