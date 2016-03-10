package xyz.madki.clask.team;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.Bind;
import xyz.madki.clask.App;
import xyz.madki.clask.AppComponent;
import xyz.madki.clask.R;
import xyz.madki.clask.base.ActivityInjector;
import xyz.madki.clask.base.PresentedActivity;
import xyz.madki.clask.scope.PerActivity;

public class TeamActivity extends PresentedActivity<TeamPresenter, TeamActivity.Component>
        implements TeamPresenter.IView, TextWatcher {
  @Inject TeamPresenter presenter;
  @Inject MemberListAdapter adapter;
  @Bind(R.id.rv_member_list) RecyclerView members;
  @Bind(R.id.et_search) EditText searchBox;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    members.setLayoutManager(new LinearLayoutManager(this));
    members.setAdapter(adapter);
    searchBox.addTextChangedListener(this);
    searchBox.setVisibility(adapter.isSearchBoxVisible() ? View.VISIBLE : View.GONE);
  }

  @Override
  public void onBackPressed() {
    if (adapter.isSearchBoxVisible()) {
      hideSearchBox();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.show_all:
        adapter.toggleShowAll();
        return true;
      case R.id.search:
        if (adapter.isSearchBoxVisible()) {
          hideSearchBox();
        } else {
          showSearchBox();
        }
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void hideSearchBox() {
    searchBox.setText("");
    searchBox.setVisibility(View.GONE);
    adapter.setSearchBoxVisible(false);
  }

  private void showSearchBox() {
    searchBox.setVisibility(View.VISIBLE);
    adapter.setSearchBoxVisible(true);
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

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    adapter.getFilter().filter(s);
  }

  @Override
  public void afterTextChanged(Editable s) {

  }

  @PerActivity
  @dagger.Component(dependencies = AppComponent.class)
  public interface Component extends ActivityInjector<TeamActivity> {
    @Override
    void inject(TeamActivity activity);
  }
}
