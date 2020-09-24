package com.baiaihan.adjustprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private int settingValue;
    private CustomCounterWidget mainProgressValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainProgressValue = findViewById(R.id.main_progress_value);
        settingValue = mainProgressValue.getProgressValue();
        mainProgressValue.setEditOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow();
            }
        });
        mainProgressValue.addWatcherProgressListenr(new CustomCounterWidget.IWatcherProgress() {
            @Override
            public void valueChage(String value) {
                settingValue = Integer.valueOf(value);
                Log.i("ooo", "==========value==========" + value);
            }
        });
    }

    public void popwindow() {
        SettingProgressDialog popupDialog = new SettingProgressDialog(this, settingValue);
        popupDialog.setDialogProgressValueListener(new SettingProgressDialog.DialogProgressValueListener() {
            @Override
            public void setProgressValue(int value) {
                mainProgressValue.setProgressValue(value);
            }
        });
        popupDialog.show();
    }
}
