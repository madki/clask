package xyz.madki.clask.team;

import android.os.Bundle;

import com.f2prateek.rx.preferences.Preference;
import com.google.gson.Gson;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;
import xyz.madki.clask.api.SlackApi;
import xyz.madki.clask.api.models.Members;
import xyz.madki.clask.api.models.Team;
import xyz.madki.clask.base.BasePresenter;
import xyz.madki.clask.base.IBundleProvider;
import xyz.madki.clask.scope.PerActivity;

@PerActivity
public class TeamPresenter extends BasePresenter<TeamPresenter.IView> {
  MemberListAdapter adapter;
  Preference<Members> members;
  Team team;

  CompositeSubscription subscriptions;
  CompositeSubscription viewSubscriptions;

  @Inject
  public TeamPresenter(SlackApi slackApi,
                       Gson gson,
                       MemberListAdapter adapter,
                       Preference<Team> teamPref,
                       Preference<Members> membersPreference) {
    subscriptions = new CompositeSubscription();
    viewSubscriptions = new CompositeSubscription();

    team = teamPref.get();
    members = membersPreference;

    this.adapter = adapter;

    if (team != null) {
      Timber.d(gson.toJson(team));
      subscriptions.add(
              slackApi.getMembers(team.accessToken())
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(members.asAction(), this::error)
      );
    }

    subscriptions.add(
            members.asObservable()
                    .filter(members1 -> members1 != null)
                    .map(Members::members)
                    .doOnNext(m -> Timber.d(" " + m))
                    .subscribe(adapter::updateMembers, this::error)
    );
  }

  private void error(Throwable t) {

  }

  @Override
  protected void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);
  }

  @Override
  public void dropView(IView view) {
    if (viewSubscriptions != null && !viewSubscriptions.isUnsubscribed()) {
      viewSubscriptions.unsubscribe();
    }
    super.dropView(view);
  }

  @Override
  protected void onExitScope() {
    super.onExitScope();
    if (subscriptions != null && !subscriptions.isUnsubscribed()) {
      subscriptions.unsubscribe();
    }
  }

  public interface IView extends IBundleProvider {
  }
}
