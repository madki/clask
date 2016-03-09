package xyz.madki.clask.team;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import xyz.madki.clask.api.models.Member;
import xyz.madki.clask.scope.PerActivity;

@PerActivity
public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder> {
  private List<Member> members;

  @Inject
  public MemberListAdapter() {
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    if(members != null) return members.size();
    return 0;
  }

  public void updateMembers(List<Member> members) {
    this.members = members;
    notifyDataSetChanged();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View itemView) {
      super(itemView);
    }
  }
}
