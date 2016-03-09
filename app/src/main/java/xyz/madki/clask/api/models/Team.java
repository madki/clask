package xyz.madki.clask.api.models;

import auto.parcelgson.AutoParcelGson;
import auto.parcelgson.gson.annotations.SerializedName;

@AutoParcelGson
public abstract class Team {
  public abstract boolean ok();
  @SerializedName("access_token")
  public abstract String accessToken();
  public abstract String scope();
  @SerializedName("team_name")
  public abstract String teamName();
  @SerializedName("team_id")
  public abstract String teamId();
}
