package com.pechenkin.travelmoney.page.main;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.main.fragment.CostListFragment;
import com.pechenkin.travelmoney.page.main.fragment.MembersListFragment;
import com.pechenkin.travelmoney.page.main.fragment.OtherFragment;
import com.pechenkin.travelmoney.page.main.fragment.TripsListFragment;

public class MainPageNew extends BasePage {
    @Override
    protected int getPageId() {
        return R.layout.new_activity_main;
    }

    @Override
    protected String getTitleHeader() {
        return "";
    }

    @Override
    protected boolean fillFields() {

        FragmentManager manager = MainActivity.INSTANCE.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment, new CostListFragment()).commit();

        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return 0;
    }

    @Override
    protected void helps() {


    }


    @Override
    public void clickBackButton() {
        BottomNavigationView navView = MainActivity.INSTANCE.findViewById(R.id.nav_view);
        if (navView.getSelectedItemId() == R.id.navigation_list) {
            MainActivity.INSTANCE.finish();
        } else {
            navView.setSelectedItemId(R.id.navigation_list);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void addEvents() {
        BottomNavigationView navView = MainActivity.INSTANCE.findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(menuItem -> {

            if (navView.getSelectedItemId() == menuItem.getItemId()) {
                return false;
            }

            FragmentManager manager = MainActivity.INSTANCE.getSupportFragmentManager();
            Fragment currentFragment = null;
            switch (menuItem.getItemId()) {
                case R.id.navigation_list:
                    currentFragment = new CostListFragment();
                    break;
                case R.id.navigation_members:
                    currentFragment = new MembersListFragment();
                    break;
                case R.id.navigation_trips:
                    currentFragment = new TripsListFragment();
                    break;
                case R.id.navigation_more:
                    currentFragment = new OtherFragment();
                    break;
            }

            if (currentFragment != null) {
                manager.beginTransaction().replace(R.id.fragment, currentFragment).commit();
                return true;
            }
            return false;

        });
    }
}
