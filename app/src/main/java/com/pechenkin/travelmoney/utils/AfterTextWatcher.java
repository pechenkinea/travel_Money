package com.pechenkin.travelmoney.utils;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

public class AfterTextWatcher implements TextWatcher {

    private final AfterTextChanged afterTextChanged;

    public AfterTextWatcher(@NonNull AfterTextChanged afterTextChanged) {
        this.afterTextChanged = afterTextChanged;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        afterTextChanged.afterTextChanged(editable);
    }

    public interface AfterTextChanged {
        void afterTextChanged(Editable editable);
    }
}
