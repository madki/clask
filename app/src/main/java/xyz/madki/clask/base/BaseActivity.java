package xyz.madki.clask.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Base activity where the inflation code and common code like
 * analytics tracker initialisation
 */
public abstract class BaseActivity extends AppCompatActivity {

  @Override @CallSuper
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    ButterKnife.bind(this);
  }

  @Override @CallSuper
  protected void onResume() {
    super.onResume();
  }

  @LayoutRes
  protected abstract int getLayoutId();

  protected String getScreenName() {
    return getClass().getName();
  }
}
