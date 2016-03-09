package xyz.madki.clask.base;

import android.app.Activity;

public interface ActivityInjector<A extends Activity> {
  void inject(A activity);
}
