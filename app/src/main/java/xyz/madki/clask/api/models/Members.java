package xyz.madki.clask.api.models;

import java.util.List;

import auto.parcelgson.AutoParcelGson;

@AutoParcelGson
public abstract class Members {
  public abstract boolean ok();
  public abstract List<Member> members();
}
