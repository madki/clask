package xyz.madki.clask.base;

import android.support.annotation.NonNull;

import mortar.Presenter;
import mortar.bundler.BundleService;

public abstract class PresentedActivity<P extends Presenter, C extends ActivityInjector>
        extends ScopedActivity<C> implements IBundleProvider {

  @Override
  @SuppressWarnings("unchecked")
  protected void onStart() {
    super.onStart();
    getPresenter().takeView(this);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void onStop() {
    super.onStop();
    getPresenter().dropView(this);
  }

  @Override
  public BundleService getBundleService() {
    return BundleService.getBundleService(this);
  }

  @NonNull protected abstract P getPresenter();
}
