package com.pechenkin.travelmoney.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilter implements InputFilter {


    private final Pattern mPattern;

    public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
        String regexp = "^\\d{1," + (digitsBeforeZero) + "}((\\.\\d{0," + digitsAfterZero + "})?|(\\.)?)";
        mPattern = Pattern.compile(regexp);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        String futureText = dest.toString().substring(0, dstart) + source + dest.toString().substring(dend);

        if (futureText.length() == 0){
            return null;
        }

        Matcher matcher = mPattern.matcher(futureText);
        if (!matcher.matches()) {
            if (source.toString().length() == 0) {
                return dest.toString().substring(dstart, dend);
            } else {
                return "";
            }
        }

        return null;
    }

}

