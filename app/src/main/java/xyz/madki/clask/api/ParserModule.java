package xyz.madki.clask.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import auto.parcelgson.gson.AutoParcelGsonTypeAdapterFactory;
import dagger.Module;
import dagger.Provides;

@Module
public class ParserModule {

  @Provides
  @Singleton
  public Gson provideGson() {
    return new GsonBuilder()
            .registerTypeAdapterFactory(new AutoParcelGsonTypeAdapterFactory())
            .create();
  }
}
