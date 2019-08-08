package com.pechenkin.travelmoney.page.main;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.query.member.MembersQueryResult;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.main.fragment.BaseMainPageFragment;
import com.pechenkin.travelmoney.page.main.fragment.CostListFragment;
import com.pechenkin.travelmoney.page.main.fragment.MembersListFragment;
import com.pechenkin.travelmoney.page.main.fragment.OtherFragment;
import com.pechenkin.travelmoney.page.main.fragment.TripsListFragment;

public class MainPage extends BasePage {
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

        MainActivity.INSTANCE.setTitle(t_trips.ActiveTrip.name);

        BottomNavigationView navView = MainActivity.INSTANCE.findViewById(R.id.nav_view);

        if (hasParam()) {

            if (getParam().getId() == R.id.navigation_more) {
                navView.setSelectedItemId(R.id.navigation_more);
                renderFragment(new OtherFragment());
            } else if (getParam().getId() == R.id.navigation_trips) {
                navView.setSelectedItemId(R.id.navigation_trips);
                renderFragment(new TripsListFragment());
            } else if (getParam().getId() == R.id.navigation_members) {
                navView.setSelectedItemId(R.id.navigation_members);
                renderFragment(new MembersListFragment());
            }
        } else {
            MembersQueryResult membersByActiveTrip = t_members.getAllByTripId(t_trips.ActiveTrip.id);
            //Если в текущей поездке не указаны участники то по умолчанию открываем страничку с перечнем участников
            if (!membersByActiveTrip.hasRows() || membersByActiveTrip.getAllRows().length < 2) {
                navView.setSelectedItemId(R.id.navigation_members);
                renderFragment(new MembersListFragment());
            } else {
                navView.setSelectedItemId(R.id.navigation_list);
                renderFragment(new CostListFragment());
            }
        }


        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return 0;
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
    public void addEvents() {
        BottomNavigationView navView = MainActivity.INSTANCE.findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(menuItem -> {

            if (navView.getSelectedItemId() == menuItem.getItemId()) {
                return false;
            }

            BaseMainPageFragment currentFragment = null;
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
                renderFragment(currentFragment);
                return true;
            }
            return false;

        });

    }

    /**
     * Запускает отрисовку фрагмента в отдельном потоке, что бы не лагали кнопки меню ели долго отрисовывается фрагмент
     */
    private void renderFragment(BaseMainPageFragment fragment){
        Runnable runnable = new FragmentLoader(fragment);
        new Thread(runnable).start();
    }

    static class FragmentLoader implements Runnable {

        private final BaseMainPageFragment currentFragment;

        FragmentLoader(BaseMainPageFragment currentFragment) {
            this.currentFragment = currentFragment;
        }

        @Override
        public void run() {
            try {
                // костыль
                // хоть и в отдельном потоке, но ui ве равно лагает, видимо андроид не может одновременно из разных потоков рендерить формы
                //для этого ждем, что бы кнопка меню упела показать всю анимацию
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            MainActivity.INSTANCE.setRefreshActon(null);
            FragmentManager manager = MainActivity.INSTANCE.getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, currentFragment).commit();
        }
    }
}
