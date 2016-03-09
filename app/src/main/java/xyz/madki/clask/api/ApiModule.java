package xyz.madki.clask.api;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class ApiModule {

  @Provides
  @Singleton
  public SlackApi provideSlackApi(OkHttpClient client, Gson gson) {
    Retrofit retrofit = new Retrofit.Builder()
            .client(client)
            .baseUrl(SlackApi.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    return retrofit.create(SlackApi.class);
  }

}
