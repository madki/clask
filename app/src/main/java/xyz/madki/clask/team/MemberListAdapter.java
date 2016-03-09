package xyz.madki.clask.team;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import timber.log.Timber;
import xyz.madki.clask.R;
import xyz.madki.clask.api.models.Member;
import xyz.madki.clask.base.ClickListener;
import xyz.madki.clask.scope.PerActivity;

@PerActivity
public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder>
        implements ClickListener {
  private List<Member> members;

  @Inject
  public MemberListAdapter() {
    Timber.d("Created MLA");
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
    return new ViewHolder(v, this);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    if(!isValidPosition(position)) return;

    Member member = getItem(position);
    holder.tvName.setText(member.profile().realName());
    holder.tvHandle.setText(member.name());
    holder.tvEmail.setText(member.profile().email());
    holder.tvNumber.setText(member.profile().phone());
    holder.tvSkype.setText(member.profile().skype());
    Glide.with(holder.profilePic.getContext())
            .load(member.profile().image72())
            .into(holder.profilePic);

    //set visiblities of details
    if(TextUtils.isEmpty(member.profile().phone())) {
      holder.call.setVisibility(View.GONE);
      holder.addContact.setVisibility(View.GONE);
      holder.tvNumber.setVisibility(View.GONE);
    } else {
      holder.call.setVisibility(View.VISIBLE);
      holder.addContact.setVisibility(View.VISIBLE);
      holder.tvNumber.setVisibility(View.VISIBLE);
    }

    setVisibilityByText(holder.tvEmail, member.profile().email());
    setVisibilityByText(holder.tvSkype, member.profile().skype());
  }

  @Override
  public int getItemCount() {
    if(members != null) return members.size();
    return 0;
  }

  public void updateMembers(List<Member> members) {
    Observable.from(members)
            .filter(m -> !m.deleted() && !TextUtils.isEmpty(m.profile().realName()))
            .toList()
            .subscribe(list -> {this.members = list;});
    notifyDataSetChanged();
  }

  @NonNull
  private Member getItem(int position) {
    return members.get(position);
  }

  private boolean isValidPosition(int position) {
    return (members != null) && (position >= 0) && (position < members.size());
  }

  @Override
  public void onClick(View v, int position) {
    if(!isValidPosition(position)) return;
    Member member = getItem(position);
    switch (v.getId()) {
      case R.id.btn_call:
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + member.profile().phone()));
        try {
          v.getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
          Toast.makeText(v.getContext(), "Sorry, unable to open dialer", Toast.LENGTH_LONG).show();
        }
        break;
      case R.id.btn_add_contact:
        Toast.makeText(v.getContext(), "Add " + getItem(position).name(), Toast.LENGTH_LONG).show();
        break;
      default:
        throw new IllegalStateException("Unhandled click");
    }
  }

  private static void setVisibilityByText(TextView textView, String text) {
    if(TextUtils.isEmpty(text)) {
      textView.setVisibility(View.GONE);
    } else {
      textView.setVisibility(View.VISIBLE);
    }
  }

  public static class ViewHolder extends RecyclerView.ViewHolder
          implements View.OnClickListener {
    @Bind(R.id.container) ViewGroup container;
    @Bind(R.id.tv_name) TextView tvName;
    @Bind(R.id.tv_email) TextView tvEmail;
    @Bind(R.id.tv_handle) TextView tvHandle;
    @Bind(R.id.tv_skype) TextView tvSkype;
    @Bind(R.id.tv_number) TextView tvNumber;
    @Bind(R.id.btn_call) View call;
    @Bind(R.id.btn_add_contact) View addContact;
    @Bind(R.id.iv_profile_pic) ImageView profilePic;
    @Bind(R.id.details) ViewGroup details;

    private ClickListener listener;

    public ViewHolder(View itemView, ClickListener listener) {
      super(itemView);
      this.listener = listener;
      ButterKnife.bind(this, itemView);
      call.setOnClickListener(this);
      addContact.setOnClickListener(this);
      container.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if(v.getId() == R.id.container) {
        if(details.getVisibility() == View.VISIBLE) {
          details.setVisibility(View.GONE);
        } else {
          details.setVisibility(View.VISIBLE);
        }
      } else {
        listener.onClick(v, getAdapterPosition());
      }
    }
  }
}
