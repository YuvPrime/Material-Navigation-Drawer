package com.materialnavigationdrawer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yuvaraj on 5/7/15.
 */
public class NavigationDrawerFragment extends Fragment {

    DrawerLayout mdrawerLayout;
    ActionBarDrawerToggle drawerToggle;
    private NavigationDrawerCallbacks mCallbacks;
    Toolbar mtoolbar;
    Activity mactivity;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    CharSequence item_clicked;
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";


    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        item_clicked = getActivity().getTitle();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition, item_clicked);


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);


        mDrawerListView.setAdapter(new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                new String[]{"Section One","Section two","Section three"}));
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 item_clicked = ((TextView)view).getText().toString();

                selectItem(position, item_clicked);
            }
        });

        return mDrawerListView;

    }


    public void setUp(MainActivity activity, int fragment_navigation_drawer, DrawerLayout drawerLayout, Toolbar app_bar) {
        mFragmentContainerView = getActivity().findViewById(fragment_navigation_drawer);
        mdrawerLayout = drawerLayout;
        mtoolbar = app_bar;
        mactivity = activity;




        drawerToggle = new ActionBarDrawerToggle(mactivity,mdrawerLayout,mtoolbar,R.string.open_drawer,R.string.close_drawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("Drawer", "Drawer Opened");

                if (isAdded())
                {
                    return;
                }

                mactivity.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("Drawer", "Drawer Closed");

                if (isAdded()) {
                    return;
                }

                mactivity.invalidateOptionsMenu();
            }


        };

        mdrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
                Log.d("Drawer", "Sync State is Called");
            }
        });

        mdrawerLayout.setDrawerListener(drawerToggle);
        if (!mUserLearnedDrawer){
            mdrawerLayout.openDrawer(mFragmentContainerView);

        }


    }

    private void selectItem(int position, CharSequence item_clicked) {
        mCurrentSelectedPosition = position;

        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }

        if (mdrawerLayout != null) {
            mdrawerLayout.closeDrawer(mFragmentContainerView);
        }

        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position, item_clicked);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }




    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position, CharSequence item_clicked);
    }

}
