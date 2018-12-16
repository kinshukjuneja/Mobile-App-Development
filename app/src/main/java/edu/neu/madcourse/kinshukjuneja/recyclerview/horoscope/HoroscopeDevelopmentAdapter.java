package edu.neu.madcourse.kinshukjuneja.recyclerview.horoscope;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.utils.horoscope.Help;

public class HoroscopeDevelopmentAdapter extends RecyclerView.Adapter<HoroscopeDevelopmentAdapter.ContentViewHolder> {

    private boolean isAcknowledgment;

    public HoroscopeDevelopmentAdapter(boolean isAcknowledgment) {
        this.isAcknowledgment = isAcknowledgment;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horoscope_development_item, viewGroup, false);
        HoroscopeDevelopmentAdapter.ContentViewHolder contentViewHolder = new HoroscopeDevelopmentAdapter.ContentViewHolder(view);

        return contentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder contentViewHolder, int i) {
        contentViewHolder.hdiContent.setText(isAcknowledgment ? Help.ACKNOWLEDGMENTS[i] : Help.FUTURE[i]);
    }

    @Override
    public int getItemCount() {
        return isAcknowledgment ? Help.ACKNOWLEDGMENTS.length : Help.FUTURE.length;
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {

        TextView hdiContent;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            hdiContent = itemView.findViewById(R.id.hdiContent);
        }
    }

}
