package xyz.madki.clask.team;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import butterknife.Bind;
import xyz.madki.clask.App;
import xyz.madki.clask.AppComponent;
import xyz.madki.clask.R;
import xyz.madki.clask.base.ActivityInjector;
import xyz.madki.clask.base.PresentedActivity;
import xyz.madki.clask.scope.PerActivity;

public class TeamActivity extends PresentedActivity<TeamPresenter, TeamActivity.Component>
                          implements TeamPresenter.IView {
  @Inject TeamPresenter presenter;
  @Inject MemberListAdapter adapter;
  @Bind(R.id.rv_member_list) RecyclerView members;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    members.setLayoutManager(new LinearLayoutManager(this));
    members.setAdapter(adapter);
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_team;
  }

  @NonNull
  @Override
  protected Component createComponent() {
    return DaggerTeamActivity_Component.builder()
            .appComponent(App.component(this))
            .build();
  }

  @NonNull
  @Override
  protected TeamPresenter getPresenter() {
    return presenter;
  }

  @PerActivity
  @dagger.Component(dependencies = AppComponent.class)
  public interface Component extends ActivityInjector<TeamActivity> {
    @Override
    void inject(TeamActivity activity);
  }
}
