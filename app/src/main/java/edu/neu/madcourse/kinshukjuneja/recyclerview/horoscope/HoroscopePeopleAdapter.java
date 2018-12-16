package edu.neu.madcourse.kinshukjuneja.recyclerview.horoscope;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.activity.horoscope.HoroscopeMainActivity;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.Friend;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.HoroscopeCache;

public class HoroscopePeopleAdapter extends RecyclerView.Adapter<HoroscopePeopleAdapter.FriendsViewHolder> {

    private boolean areFriends;
    private RecyclerView recyclerView;
    private HoroscopeMainActivity horoscopeMainActivity;

    public HoroscopePeopleAdapter(boolean areFriends, RecyclerView recyclerView, HoroscopeMainActivity horoscopeActivity) {
        this.areFriends = areFriends;
        this.recyclerView = recyclerView;
        this.horoscopeMainActivity = horoscopeActivity;
    }

    @NonNull
    @Override
    public HoroscopePeopleAdapter.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horoscope_person_item, viewGroup, false);
        HoroscopePeopleAdapter.FriendsViewHolder friendsViewHolder = new HoroscopePeopleAdapter.FriendsViewHolder(view);

        Button hfiEdit = view.findViewById(R.id.hfiEdit);
        Button hfiDelete = view.findViewById(R.id.hfiDelete);
        LinearLayout buttons = view.findViewById(R.id.hfiRight);

        if(areFriends) {
            hfiEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int friendIndex = recyclerView.getChildLayoutPosition(view);
                    horoscopeMainActivity.editFriendClicked(friendIndex);
                }
            });

            hfiDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int friendIndex = recyclerView.getChildLayoutPosition(view);
                    horoscopeMainActivity.deleteFriendClicked(friendIndex);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int friendIndex = recyclerView.getChildLayoutPosition(view);
                    horoscopeMainActivity.friendItemClicked(friendIndex);
                }
            });
        } else {
            hfiEdit.setVisibility(View.GONE);
            hfiDelete.setVisibility(View.GONE);
        }
        return friendsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HoroscopePeopleAdapter.FriendsViewHolder friendsViewHolder, int i) {
        Friend friend = areFriends ? HoroscopeCache.friends.get(i) : HoroscopeCache.nearMe.get(i);
        friendsViewHolder.hfiName.setText(friend.getName());
        friendsViewHolder.hfiCity.setText(friend.getCity());
        friendsViewHolder.hfiZodiacImg.setBackgroundResource(friend.getZodiac().image);
        friendsViewHolder.hfiZodiacName.setText(friend.getZodiac().toString());
        friendsViewHolder.hfiCompatibility.setText(Integer.toString(friend.getCompatibility()) + "%");
    }

    @Override
    public int getItemCount() {
        return areFriends ? HoroscopeCache.friends.size() : HoroscopeCache.nearMe.size();
    }

    public class FriendsViewHolder extends RecyclerView.ViewHolder {

        TextView hfiName;
        TextView hfiCity;
        TextView hfiCompatibility;
        ImageView hfiZodiacImg;
        TextView hfiZodiacName;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            hfiName = itemView.findViewById(R.id.hfiName);
            hfiCity = itemView.findViewById(R.id.hfiCity);
            hfiCompatibility = itemView.findViewById(R.id.hfiCompatibility);
            hfiZodiacImg = itemView.findViewById(R.id.hfiZodiacImg);
            hfiZodiacName = itemView.findViewById(R.id.hfiZodiacName);
        }
    }
}