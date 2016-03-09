package xyz.madki.clask.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import xyz.madki.clask.App;
import xyz.madki.clask.AppComponent;
import xyz.madki.clask.R;
import xyz.madki.clask.base.ActivityInjector;
import xyz.madki.clask.base.PresentedActivity;
import xyz.madki.clask.scope.PerActivity;
import xyz.madki.clask.team.TeamActivity;

public class LoginActivity extends PresentedActivity<LoginPresenter, LoginActivity.Component>
        implements LoginPresenter.IView {
  @Inject LoginPresenter presenter;
  @Bind(R.id.pb_web_progress) ProgressBar progressBar;
  @Bind(R.id.web_view) WebView webView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_login;
  }

  @NonNull
  @Override
  protected Component createComponent() {
    return DaggerLoginActivity_Component.builder()
            .appComponent(App.component(this))
            .build();
  }

  @NonNull
  @Override
  protected LoginPresenter getPresenter() {
    return presenter;
  }

  @Override
  public void showWebView() {
    webView.setVisibility(View.VISIBLE);
    webView.clearCache(true);
    webView.clearHistory();

    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return presenter.shouldOverrideUrlLoading(url) ||
                super.shouldOverrideUrlLoading(view, url);
      }
    });
  }

  @Override
  public void loadUrl(String url) {
    webView.loadUrl(url);
  }

  @Override
  public void showLoading() {
    Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show();
  }

  @Override
  public void showError() {
    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onLoginSuccess() {
    Intent intent = new Intent(this, TeamActivity.class);
    startActivity(intent);
  }

  @PerActivity
  @dagger.Component(dependencies = AppComponent.class)
  public interface Component extends ActivityInjector<LoginActivity> {
    @Override
    void inject(LoginActivity activity);
  }
}
