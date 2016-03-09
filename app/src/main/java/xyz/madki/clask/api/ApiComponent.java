package xyz.madki.clask.api;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {ApiModule.class, ParserModule.class, NetworkModule.class})
@Singleton
public interface ApiComponent {
  SlackApi slackApi();
  Gson gson();
}
