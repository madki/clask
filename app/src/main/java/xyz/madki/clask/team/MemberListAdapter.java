package xyz.madki.clask.team;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Intents;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
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
        implements ClickListener, Filterable {
  private List<Member> members;
  private List<Member> filteredMembers;
  private boolean[] toggles;
  private boolean showAll = false;
  private String searchTerm;
  private boolean searchBoxVisible = false;

  @Inject
  public MemberListAdapter() {
    Timber.d("Created MLA");
  }

  public void toggleShowAll() {
    this.showAll = !showAll;
    if (toggles != null) {
      for (int i = 0; i < toggles.length; i++) {
        toggles[i] = showAll;
      }
    }
    notifyDataSetChanged();
  }

  private void setToggles(boolean showAll, int length) {
    toggles = new boolean[length];
    for (int i = 0; i < length; i++) {
      toggles[i] = showAll;
    }
  }

  public void setSearchBoxVisible(boolean value) {
    searchBoxVisible = value;
  }

  public boolean isSearchBoxVisible() {
    return searchBoxVisible;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
    return new ViewHolder(v, this);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    if (!isValidPosition(position)) return;

    Member member = getItem(position);
    holder.tvName.setText(member.profile().realName());
    holder.tvHandle.setText(String.format("@%s", member.name()));
    holder.tvEmail.setText(member.profile().email());
    holder.tvNumber.setText(member.profile().phone());
    holder.tvSkype.setText(member.profile().skype());
    Glide.with(holder.profilePic.getContext())
            .load(member.profile().image72())
            .into(holder.profilePic);

    holder.details.setVisibility(toggles[position] ? View.VISIBLE : View.GONE);

    //set visiblities of details
    if (TextUtils.isEmpty(member.profile().phone())) {
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
    if (filteredMembers != null) return filteredMembers.size();
    return 0;
  }

  public void updateMembers(List<Member> members) {
    Observable.from(members)
            .filter(m -> !m.deleted() && !TextUtils.isEmpty(m.profile().realName()))
            .toList()
            .subscribe(list -> {
              this.members = list;
            });
    getFilter().filter(searchTerm);
    notifyDataSetChanged();
  }

  @NonNull
  private Member getItem(int position) {
    return filteredMembers.get(position);
  }

  private boolean isValidPosition(int position) {
    return (getItemCount() > 0) && (position >= 0) && (position < getItemCount());
  }

  @Override
  public void onClick(View v, int position) {
    if (!isValidPosition(position)) return;
    Member member = getItem(position);
    switch (v.getId()) {
      case R.id.btn_call:
        call(member.profile().phone(), v.getContext());
        break;
      case R.id.btn_add_contact:
        addToContacts(member, v.getContext());
        break;
      case R.id.container:
        toggles[position] = !toggles[position];
        notifyItemChanged(position);
        break;
      default:
        throw new IllegalStateException("Unhandled click");
    }
  }

  private void call(String number, Context context) {
    if (TextUtils.isEmpty(number)) {
      Toast.makeText(context, "Team member doesn't have a number", Toast.LENGTH_LONG).show();
      return;
    }

    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
    try {
      context.startActivity(intent);
    } catch (ActivityNotFoundException e) {
      Toast.makeText(context, "Sorry, unable to open dialer", Toast.LENGTH_LONG).show();
    }
  }

  private void addToContacts(Member member, Context context) {
    Intent intent = new Intent(Intents.Insert.ACTION);
    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
    intent.putExtra(Intents.Insert.NAME, member.profile().realName())
            .putExtra(Intents.Insert.PHONE, member.profile().phone())
            .putExtra(Intents.Insert.EMAIL, member.profile().email())
            .putExtra(Intents.Insert.COMPANY, "Headout");

    try {
      context.startActivity(intent);
    } catch (Exception e) {
      Toast.makeText(context, "Sorry, unable to save contact", Toast.LENGTH_LONG).show();
    }
  }

  private static void setVisibilityByText(TextView textView, String text) {
    if (TextUtils.isEmpty(text)) {
      textView.setVisibility(View.GONE);
    } else {
      textView.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public Filter getFilter() {
    return new ItemFilter();
  }

  private class ItemFilter extends Filter {

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      if (constraint == null) {
        searchTerm = null;
        FilterResults results = new FilterResults();
        List<Member> nlist = new ArrayList<>(members.size());
        nlist.addAll(members);
        results.values = nlist;
        results.count = nlist.size();
        return results;
      }

      String filterString = constraint.toString().toLowerCase();
      searchTerm = filterString;

      FilterResults results = new FilterResults();
      final List<Member> list = members;
      int count = list.size();

      final List<Member> nlist = new ArrayList<>(count);
      String filterableString;
      for (int i = 0; i < count; i++) {
        filterableString = list.get(i).profile().realName();
        if (filterableString != null && filterableString.toLowerCase().contains(filterString)) {
          nlist.add(list.get(i));
        }
      }

      results.values = nlist;
      results.count = nlist.size();

      return results;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void publishResults(CharSequence constraint, FilterResults results) {
      filteredMembers = (List<Member>) results.values;
      setToggles(showAll, filteredMembers.size());
      notifyDataSetChanged();
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
      listener.onClick(v, getAdapterPosition());
    }
  }
}
