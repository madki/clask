package xyz.madki.clask.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

/**
 * Activity with a Mortar scope
 *
 * Builds the Mortar scope and saves the state with BundleService
 * @param <C> Dagger Component for this Activity
 */
public abstract class ScopedActivity<C> extends BaseActivity {
  @Override
  public Object getSystemService(@NonNull String name) {
    MortarScope activityScope = MortarScope.findChild(getApplicationContext(), getScopeName());

    if (activityScope == null) {
      activityScope = MortarScope.buildChild(getApplicationContext()) //
              .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
              .withService(DaggerService.SERVICE_NAME, createComponent())
              .build(getScopeName());
    }

    return activityScope.hasService(name) ? activityScope.getService(name)
            : super.getSystemService(name);
  }

  @Override @CallSuper
  @SuppressWarnings("unchecked")
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);

    C component = getComponent();
    if (component instanceof ActivityInjector) {
      ((ActivityInjector) component).inject(this);
    }
  }

  @Override @CallSuper
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
  }

  @Override @CallSuper
  protected void onDestroy() {
    if (isFinishing()) {
      MortarScope activityScope = MortarScope.findChild(getApplicationContext(), getScopeName());
      if (activityScope != null) activityScope.destroy();
    }
    super.onDestroy();
  }

  @NonNull
  protected abstract C createComponent();

  protected String getScopeName() {
    return getClass().getName();
  }

  public C getComponent() {
    return DaggerService.<C>getDaggerComponent(this);
  }

}
