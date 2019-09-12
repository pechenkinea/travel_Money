package com.pechenkin.travelmoney.utils;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;


public class CustomSelectionActionModeCallback implements ActionMode.Callback {

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        try {
            MenuItem copyItem = menu.findItem(android.R.id.copy);
            CharSequence title = copyItem.getTitle();
            menu.clear();
            menu.add(0, android.R.id.copy, 0, title);
        }
        catch (Exception e) {
            // ignored
        }
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }
}