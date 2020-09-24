package com.baiaihan.adjustprogress;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;


public class SettingProgressDialog extends Dialog {

    private Context mContext;
    private Button mBtnPositive;
    private Button mBtnNegative;
    private int mProgressValue;
    private DialogProgressValueListener mDialogProgressValueListener;

    public SettingProgressDialog(Context context, int progressValue) {
        super(context, R.style.DialogStyle);
        this.mContext = context;
        setContentView(R.layout.dialog_set_progress);
        CustomCounterWidget mPprogressValue = findViewById(R.id.progress_value);
        mPprogressValue.setProgressValue(progressValue);
        mBtnPositive = (Button) findViewById(R.id.dialog_comfirm);
        mBtnNegative = (Button) findViewById(R.id.dialog_cancel);
        mPprogressValue.addWatcherProgressListenr(new CustomCounterWidget.IWatcherProgress() {
            @Override
            public void valueChage(String value) {
                mProgressValue = Integer.valueOf(value);
            }
        });
        mBtnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogProgressValueListener.setProgressValue(mProgressValue);
                dismiss();
            }
        });
        mBtnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void setDialogProgressValueListener(DialogProgressValueListener listener) {
        mDialogProgressValueListener = listener;
    }

    interface DialogProgressValueListener {
        void setProgressValue(int value);
    }
}
