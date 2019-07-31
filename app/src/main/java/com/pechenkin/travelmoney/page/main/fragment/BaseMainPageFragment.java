package com.pechenkin.travelmoney.page.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pechenkin.travelmoney.Help;

public abstract class BaseMainPageFragment extends Fragment {

    View fragmentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(getViewId(), container, false);
        return fragmentView;
    }


    @Override
    public void onStart() {
        super.onStart();

        int[] buttons = getButtons();
        for (int button : buttons) {
            Help.showFabWithAnimation(fragmentView.findViewById(button));
        }
        setListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        doAfterRender();
    }


    abstract int getViewId();
    abstract void setListeners();
    protected abstract void doAfterRender();
    abstract int[] getButtons();
}
