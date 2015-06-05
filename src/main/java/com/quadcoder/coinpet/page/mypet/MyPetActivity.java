package com.quadcoder.coinpet.page.mypet;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quadcoder.coinpet.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.network.response.Goal;
import com.quadcoder.coinpet.page.common.Constants;
import com.quadcoder.coinpet.page.common.GoalSettingActivity;
import com.quadcoder.coinpet.page.common.Utils;

public class MyPetActivity extends ActionBarActivity implements ActionBar.TabListener, HistoryFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_pet);
        View main = getLayoutInflater().inflate(R.layout.activity_my_pet, null);
        Utils.getInstance().overrideFonts(MyPetActivity.this, main);
        setContentView(main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_my_pet, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);

            if(position == 0)
                return PlaceholderFragment.newInstance(0);
            else
                return HistoryFragment.newInstance(1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);

            }
            return null;
        }
    }

    static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        TextView tvGoal;
        ImageButton imgbtnEdit;
        TextView tvGoalMoney;
        TextView tvNowMoney;
        TextView tvHowMore;
        ProgressBar pbarMore;
        TextView tvGoalDate;
        TextView tvUntilGoal;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_pet, container, false);
            tvGoal = (TextView)rootView.findViewById(R.id.tvGoal);
            imgbtnEdit = (ImageButton)rootView.findViewById(R.id.imgbtnEdit);
            tvGoalMoney = (TextView)rootView.findViewById(R.id.tvGoalMoney);
            tvNowMoney = (TextView)rootView.findViewById(R.id.tvNowMoney);
            tvHowMore = (TextView)rootView.findViewById(R.id.tvHowMore);
            pbarMore = (ProgressBar)rootView.findViewById(R.id.pbarMore);
            tvGoalDate = (TextView)rootView.findViewById(R.id.tvGoalDate);
            tvUntilGoal = (TextView)rootView.findViewById(R.id.tvUntilGoal);


            imgbtnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), GoalSettingActivity.class));
                }
            });

            Goal goal = PropertyManager.getInstance().mGoal;
            tvGoal.setText(goal.content);
            tvGoalMoney.setText("목표 금액   " + goal.goal_cost + "원");
            tvNowMoney.setText("현재 금액   " + goal.now_cost + "원");
            tvHowMore.setText("목표까지 " + (goal.goal_cost - goal.now_cost) + "원 남았어요");
            pbarMore.setMax(goal.goal_cost);
            pbarMore.setProgress(goal.now_cost);
            tvGoalDate.setText("D-" + goal.plus);
            tvUntilGoal.setText(goal.goal_date);

            return rootView;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
