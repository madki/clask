package xyz.madki.clask.login;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntDef;

import com.f2prateek.rx.preferences.Preference;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Inject;

import icepick.State;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import xyz.madki.clask.SlackUtils;
import xyz.madki.clask.api.SlackApi;
import xyz.madki.clask.api.models.Team;
import xyz.madki.clask.base.BasePresenter;
import xyz.madki.clask.base.IBundleProvider;
import xyz.madki.clask.scope.PerActivity;

@PerActivity
public class LoginPresenter extends BasePresenter<LoginPresenter.IView> {
  @State String code = null;
  @State
  @StatusType
  private int state = Status.WAITING;
  Subscription tokenSubscription;
  private final SlackApi slackApi;
  private Preference<Team> teamPref;

  @Inject
  public LoginPresenter(SlackApi slackApi, Preference<Team> teamPref) {
    this.slackApi = slackApi;
    this.teamPref = teamPref;
  }

  public boolean shouldOverrideUrlLoading(String url) {
    if (url != null && url.contains(SlackApi.REDIRECT_URL)) {
      if (getView() != null) {
        getView().showLoading();
      }
      Uri uri = Uri.parse(url);
      code = uri.getQueryParameter("code");
      getAccessToken();
      return true;
    }

    return false;
  }

  private void getAccessToken() {
    if (tokenSubscription != null && tokenSubscription.isUnsubscribed()) return;
    tokenSubscription = slackApi.getToken(SlackApi.CLIENT_ID,
            SlackApi.CLIENT_SECRET, code, SlackApi.REDIRECT_URL)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext((team) -> {
              if (getView() != null) getView().onLoginSuccess();
              Timber.d("Team: " + team);
            })
            .subscribe(teamPref.asAction(), e -> {
              error();
            });
  }

  @Override
  protected void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);
    switch (state) {
      case Status.WAITING:
        init();
        break;
      case Status.CODE_GRANTED:
        getAccessToken();
        break;
      case Status.ERROR:
        error();
        break;
      case Status.SUCCESS:
        success();
        break;
    }
  }

  private void init() {
    if(tokenSubscription != null && !tokenSubscription.isUnsubscribed()) {
      tokenSubscription.unsubscribe();
    }
    if (getView() == null) return;
    getView().showWebView();
    getView().loadUrl(SlackUtils.getAuthUrl(SlackApi.CLIENT_ID,
            SlackApi.SCOPE, SlackApi.REDIRECT_URL));
  }

  private void success() {
    state = Status.SUCCESS;
    if (getView() != null) getView().onLoginSuccess();
  }

  private void error() {
    state = Status.ERROR;
    if (getView() == null) return;

    getView().showError();
    init();
  }

  @Override
  protected void onExitScope() {
    super.onExitScope();
    if (tokenSubscription != null && !tokenSubscription.isUnsubscribed()) {
      tokenSubscription.unsubscribe();
    }
  }

  public interface IView extends IBundleProvider {
    void showWebView();

    void loadUrl(String url);

    void showLoading();

    void showError();

    void onLoginSuccess();
  }


  @Retention(RetentionPolicy.SOURCE)
  @IntDef({Status.WAITING, Status.CODE_GRANTED, Status.SUCCESS, Status.ERROR})
  @interface StatusType {
  }

  private static final class Status {
    private static final int WAITING = 1;
    private static final int CODE_GRANTED = 2;
    private static final int SUCCESS = 3;
    private static final int ERROR = 4;
  }
}
