package xyz.madki.clask.api.models;

import auto.parcelgson.AutoParcelGson;
import auto.parcelgson.gson.annotations.SerializedName;

@AutoParcelGson
public abstract class Member {
  public abstract String id();

  public abstract String name();

  public abstract boolean deleted();

  public abstract String color();

  public abstract Profile profile();

  @SerializedName("is_admin")
  public abstract boolean isAdmin();

  @SerializedName("is_owner")
  public abstract boolean isOwner();

  @SerializedName("has_2fa")
  public abstract boolean has2fa();

  @SerializedName("has_files")
  public abstract boolean hasFiles();
}
