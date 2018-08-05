package com.news.android.nytimes.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.news.android.nytimes.listener.DialogClickListener;

public class CommonMethods {
    public static void showDialog(Context context, final String requestTag, String title, String content, String positiveTxt, final DialogClickListener dialogClickListener) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveTxt).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialogClickListener.onPositiveClick(requestTag, dialog);
                    }
                })
                .show();
    }
}
