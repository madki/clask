package xyz.madki.clask.api.models;

import auto.parcelgson.AutoParcelGson;
import auto.parcelgson.gson.annotations.SerializedName;

@AutoParcelGson
public abstract class Profile {
  @SerializedName("first_name")
  public abstract String firstName();

  @SerializedName("last_name")
  public abstract String lastName();

  @SerializedName("real_name")
  public abstract String realName();

  public abstract String email();

  public abstract String skype();

  public abstract String phone();

  @SerializedName("image_24")
  public abstract String image24();

  @SerializedName("image_32")
  public abstract String image32();

  @SerializedName("image_48")
  public abstract String image48();

  @SerializedName("image_72")
  public abstract String image72();

  @SerializedName("image_192")
  public abstract String image192();
}
