package com.pechenkin.travelmoney.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.text.Html;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.view.View;

/**
 * Created by pechenkin on 11.05.2018.
 * Класс для обработки ссылок в текствью
 */

public final class SafeURLSpan extends URLSpan {
    private SafeURLSpan(String url) {
        super(url);
    }

    @Override
    public void onClick(View widget) {
        try {
            final Uri uri = Uri.parse(getURL());
            final Context context = widget.getContext();
            final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (context != null) {
                intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                context.startActivity(intent);
            }
        } catch (Throwable ex) {
            // нет приложения для открытия ссылки
        }

    }

    public static CharSequence parseSafeHtml(CharSequence html) {
        return replaceURLSpans(Html.fromHtml(html.toString()));
    }

    private static CharSequence replaceURLSpans(CharSequence text) {
        if (text instanceof Spannable) {
            final Spannable s = (Spannable)text;
            final URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
            if (spans != null && spans.length > 0) {
                for (int i = spans.length - 1; i >= 0; i--) {
                    final URLSpan span = spans[i];
                    final int start = s.getSpanStart(span);
                    final int end = s.getSpanEnd(span);
                    final int flags = s.getSpanFlags(span);
                    s.removeSpan(span);
                    s.setSpan(new SafeURLSpan(span.getURL()), start, end, flags);
                }
            }
        }
        return text;
    }
}