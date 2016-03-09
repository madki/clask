package xyz.madki.clask.splash;

import android.content.Intent;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import xyz.madki.clask.App;
import xyz.madki.clask.AppComponent;
import xyz.madki.clask.R;
import xyz.madki.clask.base.ActivityInjector;
import xyz.madki.clask.base.PresentedActivity;
import xyz.madki.clask.login.LoginActivity;
import xyz.madki.clask.scope.PerActivity;
import xyz.madki.clask.team.TeamActivity;

public class SplashActivity extends PresentedActivity<SplashPresenter, SplashActivity.Component>
      implements SplashPresenter.IView {
  @Inject SplashPresenter presenter;

  @NonNull
  @Override
  protected SplashPresenter getPresenter() {
    return presenter;
  }

  @NonNull
  @Override
  protected Component createComponent() {
    return DaggerSplashActivity_Component.builder()
            .appComponent(App.component(this))
            .build();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_splash;
  }

  @Override
  public void toLogin() {
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
    finish();
  }

  @Override
  public void toTeam() {
    Intent intent = new Intent(this, TeamActivity.class);
    startActivity(intent);
    finish();
  }

  @PerActivity
  @dagger.Component(dependencies = AppComponent.class)
  public interface Component extends ActivityInjector<SplashActivity> {
    @Override
    void inject(SplashActivity activity);
  }
}
