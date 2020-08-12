package com.example.mumbaiguide;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private static final int animationDuration = 500;
    private static ArrayList<GuideItem> sights;
    private static ArrayList<GuideItem> eatingDrinking;
    private static ArrayList<GuideItem> shopping;
    private static ArrayList<GuideItem> activities;
    private static ArrayList<GuideItem> sightsFiltered;
    private static ArrayList<GuideItem> eatingDrinkingFiltered;
    private static ArrayList<GuideItem> shoppingFiltered;
    private static ArrayList<GuideItem> activitiesFiltered;
    private static boolean guideItemsBuilt = false;
    GuideItemFragmentAdapter guideItemFragmentAdapter;
    ViewPager2 viewPager;

    public static ArrayList<GuideItem> getSights() {
        return sightsFiltered;
    }

    public static ArrayList<GuideItem> getEatingDrinking() {
        return eatingDrinkingFiltered;
    }

    public static ArrayList<GuideItem> getShopping() {
        return shoppingFiltered;
    }

    public static ArrayList<GuideItem> getActivities() {
        return activitiesFiltered;
    }

    public static void displayCard(Activity activity, int id, final View clickedView) {
        GuideItem currentGuideItem = GuideItem.getGuideItemById(id);
        if (currentGuideItem != null) {
            View card = activity.findViewById(R.id.card);
            View cardOverlay = activity.findViewById(R.id.card_overlay);

            //Setting values
            ((TextView) card.findViewById(R.id.card_guide_item_name)).setText(
                    currentGuideItem.getName());
            ((TextView) card.findViewById(R.id.card_guide_item_subcat)).setText(
                    currentGuideItem.getSubcategory());
            ((TextView) card.findViewById(R.id.card_guide_item_neighborhood)).setText(
                    currentGuideItem.getNeighborhood());
            ((ImageView) card.findViewById(R.id.card_guide_item_image)).setImageResource(
                    currentGuideItem.getImage());
            if (currentGuideItem.isStarred()) {
                ((ImageView) card.findViewById(R.id.card_guide_item_star)).setImageResource(
                        R.drawable.ic_round_star_24);
            }
            card.findViewById(R.id.card_guide_item_star).setTag(currentGuideItem.getId());
            ((TextView) card.findViewById(R.id.card_guide_item_description)).setText(
                    currentGuideItem.getDescription());
            ((TextView) card.findViewById(R.id.card_guide_item_location)).setText(
                    currentGuideItem.getLocation());

            card.findViewById(R.id.card_guide_item_star).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GuideItem currentGuideItem = GuideItem.getGuideItemById(
                                    (int) view.getTag());
                            if (currentGuideItem != null) {
                                currentGuideItem.toggleStar();
                                ImageView star = (ImageView) view;
                                ImageView listStar = (ImageView) clickedView.findViewById(
                                        R.id.list_guide_item_star);
                                if (currentGuideItem.isStarred()) {
                                    star.setImageResource(R.drawable.ic_round_star_24);
                                    if (listStar != null) {
                                        listStar.setImageResource(R.drawable.ic_round_star_24);
                                    }
                                } else {
                                    star.setImageResource(R.drawable.ic_round_star_24_fade);
                                    if (listStar != null) {
                                        listStar.setImageResource(R.drawable.ic_round_star_24_fade);
                                    }
                                }
                            }
                        }
                    }
            );

            card.setAlpha(0f);
            card.setVisibility(View.VISIBLE);
            card.animate()
                    .alpha(1f)
                    .setDuration(animationDuration)
                    .setListener(null);

            cardOverlay.setAlpha(0f);
            cardOverlay.setVisibility(View.VISIBLE);
            cardOverlay.animate()
                    .alpha(1f)
                    .setDuration(animationDuration)
                    .setListener(null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!guideItemsBuilt) {
            buildGuideItems();
        }

        guideItemFragmentAdapter = new GuideItemFragmentAdapter(this);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(guideItemFragmentAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        String tabName = "";
                        int tabIcon = 0;

                        switch (position) {
                            case 0:
                                tabName = getString(R.string.tab_label_sights);
                                tabIcon = R.drawable.ic_baseline_photo_camera_24;
                                break;
                            case 1:
                                tabName = getString(R.string.tab_label_eating);
                                tabIcon = R.drawable.ic_baseline_local_dining_24;
                                break;
                            case 2:
                                tabName = getString(R.string.tab_label_shopping);
                                tabIcon = R.drawable.ic_baseline_shop_24;
                                break;
                            case 3:
                                tabName = getString(R.string.tab_label_activities);
                                tabIcon = R.drawable.ic_baseline_nature_people_24;
                                break;
                        }
                        tab.setText(tabName);
                        if (tabIcon != 0) {
                            tab.setIcon(tabIcon);
                        }
                    }
                }).attach();

        //Setting up spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.neighborhoods_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter = (String) parent.getItemAtPosition(position);
                applyFilter(filter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                clearFilter();
            }
        });

        //Setting OnClickListener on close card button
        findViewById(R.id.card_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCard();
            }
        });
    }

    private void buildGuideItems() {
        sights = new ArrayList<>(
                Arrays.asList(
                        new GuideItem(
                                getString(R.string.sights_cst_name),
                                getString(R.string.subcat_landmark),
                                getString(R.string.sights_cst_description),
                                getString(R.string.south_mumbai),
                                getString(R.string.sights_cst_location),
                                R.drawable.sights_cst),
                        new GuideItem(
                                getString(R.string.sights_gateway_name),
                                getString(R.string.subcat_landmark),
                                getString(R.string.sights_gateway_description),
                                getString(R.string.south_mumbai),
                                getString(R.string.sights_gateway_location),
                                R.drawable.sights_gateway),
                        new GuideItem(
                                getString(R.string.sights_haji_ali_name),
                                getString(R.string.subcat_religious),
                                getString(R.string.sights_haji_ali_description),
                                getString(R.string.upper_town),
                                getString(R.string.sights_haji_ali_location),
                                R.drawable.sights_haji_ali),
                        new GuideItem(
                                getString(R.string.sights_maa_hajjani_name),
                                getString(R.string.subcat_religious),
                                getString(R.string.sights_maa_hajjani_description),
                                getString(R.string.upper_town),
                                getString(R.string.sights_maa_hajjani_location),
                                R.drawable.sights_maa_hajjani),
                        new GuideItem(
                                getString(R.string.sights_mt_mary_name),
                                getString(R.string.subcat_religious),
                                getString(R.string.sights_mt_mary_description),
                                getString(R.string.bandra),
                                getString(R.string.sights_mt_mary_location),
                                R.drawable.sights_mt_mary)
                ));
        eatingDrinking = new ArrayList<>(
                Arrays.asList(
                        new GuideItem(
                                getString(R.string.eating_aaswad_name),
                                getString(R.string.subcat_maharashtrian),
                                getString(R.string.eating_aaswad_description),
                                getString(R.string.mid_city),
                                getString(R.string.eating_aaswad_location),
                                R.drawable.eating_aaswad),
                        new GuideItem(
                                getString(R.string.eating_cafe_zoe_name),
                                getString(R.string.subcat_western),
                                getString(R.string.eating_cafe_zoe_description),
                                getString(R.string.mid_city),
                                getString(R.string.eating_cafe_zoe_location),
                                R.drawable.eating_cafe_zoe),
                        new GuideItem(
                                getString(R.string.eating_haji_ali_juice_name),
                                getString(R.string.subcat_juice),
                                getString(R.string.eating_haji_ali_juice_description),
                                getString(R.string.upper_town),
                                getString(R.string.eating_haji_ali_juice_location),
                                R.drawable.eating_haji_ali_juice),
                        new GuideItem(
                                getString(R.string.eating_mcb_name),
                                getString(R.string.subcat_chaat),
                                getString(R.string.eating_mcb_description),
                                getString(R.string.bandra),
                                getString(R.string.eating_mcb_location),
                                R.drawable.eating_mcb),
                        new GuideItem(
                                getString(R.string.eating_merwan_name),
                                getString(R.string.subcat_parsi_irani),
                                getString(R.string.eating_merwan_description),
                                getString(R.string.upper_town),
                                getString(R.string.eating_merwan_location),
                                R.drawable.eating_merwan),
                        new GuideItem(
                                getString(R.string.eating_mondegar_name),
                                getString(R.string.subcat_bar),
                                getString(R.string.eating_mondegar_description),
                                getString(R.string.south_mumbai),
                                getString(R.string.eating_mondegar_location),
                                R.drawable.eating_mondegar),
                        new GuideItem(
                                getString(R.string.eating_pali_bhavan_name),
                                getString(R.string.subcat_north_indian),
                                getString(R.string.eating_pali_bhavan_description),
                                getString(R.string.bandra),
                                getString(R.string.eating_pali_bhavan_location),
                                R.drawable.eating_pali_bhavan),
                        new GuideItem(
                                getString(R.string.eating_shree_thaker_name),
                                getString(R.string.subcat_gujarati),
                                getString(R.string.eating_shree_thaker_description),
                                getString(R.string.upper_town),
                                getString(R.string.eating_shree_thaker_location),
                                R.drawable.eating_shree_thakur),
                        new GuideItem(
                                getString(R.string.eating_soam_name),
                                getString(R.string.subcat_guj_raj),
                                getString(R.string.eating_soam_description),
                                getString(R.string.upper_town),
                                getString(R.string.eating_soam_location),
                                R.drawable.eating_soam),
                        new GuideItem(
                                getString(R.string.eating_suzette_name),
                                getString(R.string.subcat_western),
                                getString(R.string.eating_suzette_description),
                                getString(R.string.bandra),
                                getString(R.string.eating_suzette_location),
                                R.drawable.eating_suzette),
                        new GuideItem(
                                getString(R.string.eating_totos_name),
                                getString(R.string.subcat_bar),
                                getString(R.string.eating_totos_description),
                                getString(R.string.bandra),
                                getString(R.string.eating_totos_location),
                                R.drawable.eating_totos)
                ));

        shopping = new ArrayList<>(
                Arrays.asList(
                        new GuideItem(
                                getString(R.string.shopping_anokhi_name),
                                getString(R.string.subcat_clothing),
                                getString(R.string.shopping_anokhi_description),
                                getString(R.string.bandra),
                                getString(R.string.shopping_anokhi_location),
                                R.drawable.shopping_anokhi),
                        new GuideItem(
                                getString(R.string.shopping_chor_bazaar_name),
                                getString(R.string.subcat_antiques),
                                getString(R.string.shopping_chor_bazaar_description),
                                getString(R.string.upper_town),
                                getString(R.string.shopping_chor_bazaar_location),
                                R.drawable.shopping_chor_bazaar),
                        new GuideItem(
                                getString(R.string.shopping_cowpathy_name),
                                getString(R.string.subcat_gifts),
                                getString(R.string.shopping_cowpathy_description),
                                getString(R.string.upper_town),
                                getString(R.string.shopping_cowpathy_location),
                                R.drawable.shopping_cowpathy)
                ));

        activities = new ArrayList<>(
                Arrays.asList(
                        new GuideItem(
                                getString(R.string.activities_cricket_name),
                                getString(R.string.subcat_sports),
                                getString(R.string.activities_cricket_description),
                                getString(R.string.south_mumbai),
                                getString(R.string.activities_cricket_location),
                                R.drawable.activities_cricket),
                        new GuideItem(
                                getString(R.string.activities_kabaddi_name),
                                getString(R.string.subcat_sports),
                                getString(R.string.activities_kabaddi_description),
                                getString(R.string.upper_town),
                                getString(R.string.activities_kabaddi_location),
                                R.drawable.activities_kabaddi),
                        new GuideItem(
                                getString(R.string.activities_monsoon_trek_name),
                                getString(R.string.subcat_outing),
                                getString(R.string.activities_monsoon_trek_description),
                                getString(R.string.other),
                                getString(R.string.activities_monsoon_trek_location),
                                R.drawable.activities_trek),
                        new GuideItem(
                                getString(R.string.activities_sailing_name),
                                getString(R.string.subcat_outing),
                                getString(R.string.activities_sailing_description),
                                getString(R.string.south_mumbai),
                                getString(R.string.activities_sailing_location),
                                R.drawable.activities_sailing),
                        new GuideItem(
                                getString(R.string.activities_sgnp_name),
                                getString(R.string.subcat_outing),
                                getString(R.string.activities_sgnp_description),
                                getString(R.string.other),
                                getString(R.string.activities_sgnp_location),
                                R.drawable.activities_sgnp)
                ));

        sightsFiltered = sights;
        eatingDrinkingFiltered = eatingDrinking;
        shoppingFiltered = shopping;
        activitiesFiltered = activities;
        guideItemsBuilt = true;
    }

    private void closeCard() {
        final View card = findViewById(R.id.card);
        final View cardOverlay = findViewById(R.id.card_overlay);
        card.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        card.setVisibility(View.GONE);
                    }
                });
        cardOverlay.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        cardOverlay.setVisibility(View.GONE);
                    }
                });
    }

    public void preventClicks(View view) {
    }

    private void applyFilter(String filter) {
        if (filter.equals(getString(R.string.filter_default))) {
            clearFilter();
        } else {
            sightsFiltered = new ArrayList<>();
            eatingDrinkingFiltered = new ArrayList<>();
            shoppingFiltered = new ArrayList<>();
            activitiesFiltered = new ArrayList<>();

            for (GuideItem guideItem : sights) {
                if (filter.equals(getString(R.string.starred))) {
                    if (guideItem.isStarred()) {
                        sightsFiltered.add(guideItem);
                    }
                } else {
                    if (guideItem.getNeighborhood().equals(filter)) {
                        sightsFiltered.add(guideItem);
                    }
                }
            }
            for (GuideItem guideItem : eatingDrinking) {
                if (filter.equals(getString(R.string.starred))) {
                    if (guideItem.isStarred()) {
                        eatingDrinkingFiltered.add(guideItem);
                    }
                } else {
                    if (guideItem.getNeighborhood().equals(filter)) {
                        eatingDrinkingFiltered.add(guideItem);
                    }
                }
            }
            for (GuideItem guideItem : shopping) {
                if (filter.equals(getString(R.string.starred))) {
                    if (guideItem.isStarred()) {
                        shoppingFiltered.add(guideItem);
                    }
                } else {
                    if (guideItem.getNeighborhood().equals(filter)) {
                        shoppingFiltered.add(guideItem);
                    }
                }
            }
            for (GuideItem guideItem : activities) {
                if (filter.equals(getString(R.string.starred))) {
                    if (guideItem.isStarred()) {
                        activitiesFiltered.add(guideItem);
                    }
                } else {
                    if (guideItem.getNeighborhood().equals(filter)) {
                        activitiesFiltered.add(guideItem);
                    }
                }
            }

            guideItemFragmentAdapter = new GuideItemFragmentAdapter(this);
            viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(guideItemFragmentAdapter);
        }
    }

    private void clearFilter() {
        sightsFiltered = sights;
        eatingDrinkingFiltered = eatingDrinking;
        shoppingFiltered = shopping;
        activitiesFiltered = activities;

        guideItemFragmentAdapter = new GuideItemFragmentAdapter(this);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(guideItemFragmentAdapter);
    }
}