package xyz.madki.clask.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;

import icepick.Icepick;
import mortar.Presenter;
import mortar.bundler.BundleService;

/**
 * All presenters need to extend this class
 * @param <V> The View interface which extends IBundleProvider interface
 */
public class BasePresenter<V extends IBundleProvider> extends Presenter<V> {
  @Override
  protected BundleService extractBundleService(V view) {
    return view.getBundleService();
  }

  @Override
  @CallSuper
  protected void onLoad(Bundle savedInstanceState) {
    Icepick.restoreInstanceState(this, savedInstanceState);
  }

  @Override
  @CallSuper
  protected void onSave(Bundle outState) {
    Icepick.saveInstanceState(this, outState);
  }
}
