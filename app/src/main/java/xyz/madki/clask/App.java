package xyz.madki.clask;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import mortar.MortarScope;
import timber.log.Timber;
import xyz.madki.clask.api.DaggerApiComponent;

public class App extends Application {
  private MortarScope rootScope;
  private AppComponent component;

  @Override
  public Object getSystemService(String name) {
    if (rootScope == null) rootScope = MortarScope.buildRootScope().build("Root");
    return rootScope.hasService(name) ? rootScope.getService(name) : super.getSystemService(name);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    init();
    component.inject(this);
  }

  private void init() {
    initComponent();
    initLogging();
    LeakCanary.install(this); // to detect activity leaks
  }

  private void initComponent() {
    component = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .apiComponent(DaggerApiComponent.create())
            .build();
  }

  private void initLogging() {

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }

  public static AppComponent component(Context context) {
    return app(context).component;
  }

  public static App app(Context context) {
    return (App) context.getApplicationContext();
  }
}
