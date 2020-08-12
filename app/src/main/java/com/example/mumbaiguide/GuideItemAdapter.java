package com.example.mumbaiguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GuideItemAdapter extends ArrayAdapter<GuideItem> {

    public GuideItemAdapter(Context context, ArrayList<GuideItem> guideItems) {
        super(context, 0, guideItems);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        //Get the guideItem located at this position
        final GuideItem currentGuideItem = getItem(position);

        //Name, Subcat, Neighborhood, Image, isStarred
        if (currentGuideItem != null) {
            ((TextView) listItemView.findViewById(R.id.list_guide_item_name)).setText(
                    currentGuideItem.getName());
            ((TextView) listItemView.findViewById(R.id.list_guide_item_subcat)).setText(
                    currentGuideItem.getSubcategory());
            ((TextView) listItemView.findViewById(R.id.list_guide_item_neighborhood)).setText(
                    currentGuideItem.getNeighborhood());
            ((ImageView) listItemView.findViewById(R.id.list_guide_item_image)).setImageResource(
                    currentGuideItem.getImage());
            if (currentGuideItem.isStarred()) {
                ((ImageView) listItemView.findViewById(R.id.list_guide_item_star)).setImageResource(
                        R.drawable.ic_round_star_24);
            }
            listItemView.findViewById(R.id.list_guide_item_star).setTag(currentGuideItem.getId());
            listItemView.setTag(currentGuideItem.getId());
        }

        //Set OnClickListener to open up card
        listItemView.findViewById(R.id.list_guide_item).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = -1;
                        if (view.getTag() != null) {
                            id = (int) view.getTag();
                        }
                        MainActivity.displayCard((Activity) view.getContext(), id, view);
                    }
                }
        );

        //OnClickListener on the Star button to favorite an item
        listItemView.findViewById(R.id.list_guide_item_star).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GuideItem currentGuideItem = GuideItem.getGuideItemById(
                                (int) view.getTag());
                        if (currentGuideItem != null) {
                            currentGuideItem.toggleStar();
                            ImageView star = (ImageView) view;
                            if (currentGuideItem.isStarred()) {
                                star.setImageResource(R.drawable.ic_round_star_24);
                            } else {
                                star.setImageResource(R.drawable.ic_round_star_24_fade);
                            }
                        }
                    }
                }
        );

        return listItemView;
    }
}
