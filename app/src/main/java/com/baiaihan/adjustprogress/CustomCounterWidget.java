package com.baiaihan.adjustprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.math.BigDecimal;

public class CustomCounterWidget extends LinearLayout implements View.OnTouchListener {
    private Context mContext;
    private CustomCounterWidget.IWatcherProgress mCallBack;
    private ImageView mTvMius;
    private ImageView mTvPlus;
    private boolean isOnLongClick;
    private boolean miusEnable;
    private boolean plusEnable;
    private Thread miusThread;
    private Thread plusThread;
    private int minvalue;
    private int maxvalue;
    private EditText mEtProgress;
    private boolean mIsEditVisibal;
    private int mProgressValue;

    public CustomCounterWidget(Context context) {
        this(context, null);
    }

    public CustomCounterWidget(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCounterWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomCounterWidget, defStyleAttr, 0);
        minvalue = array.getInt(
                R.styleable.CustomCounterWidget_minValue, 0);
        maxvalue = array.getInt(
                R.styleable.CustomCounterWidget_maxValue, 100);
        mIsEditVisibal = array.getBoolean(
                R.styleable.CustomCounterWidget_isEditVisibal, true);
        mProgressValue = array.getInt(
                R.styleable.CustomCounterWidget_progressValue, 50);
        LayoutInflater.from(context).inflate(R.layout.custom_compose, this);

        mTvMius = findViewById(R.id.tv_mius);
        mTvPlus = findViewById(R.id.tv_plus);
        mTvMius.setOnTouchListener(this);
        mTvPlus.setOnTouchListener(this);
        mEtProgress = findViewById(R.id.et_progress);
        mEtProgress.setText(mProgressValue + "");
        mEtProgress.setFocusable(mIsEditVisibal);

        mEtProgress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mCallBack != null) {
                    mCallBack.valueChage(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setBtnEnable();
    }

    private void onTouchChange(String methodName, int eventAction) {
        //按下松开分别对应启动停止减线程方法
        if ("mius".equals(methodName)) {
            if (eventAction == MotionEvent.ACTION_DOWN) {
                miusThread = new CustomCounterWidget.MiusThread();
                isOnLongClick = true;
                miusThread.start();
            } else if (eventAction == MotionEvent.ACTION_UP) {
                if (miusThread != null) {
                    isOnLongClick = false;
                }
            } else if (eventAction == MotionEvent.ACTION_MOVE) {
                if (miusThread != null) {
                    isOnLongClick = true;
                }
            }
        }
        //按下松开分别对应启动停止加线程方法
        else if ("plus".equals(methodName)) {
            if (eventAction == MotionEvent.ACTION_DOWN) {
                plusThread = new CustomCounterWidget.PlusThread();
                isOnLongClick = true;
                plusThread.start();
            } else if (eventAction == MotionEvent.ACTION_UP) {
                if (plusThread != null) {
                    isOnLongClick = false;
                }
            } else if (eventAction == MotionEvent.ACTION_MOVE) {
                if (plusThread != null) {
                    isOnLongClick = true;
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_mius:
                onTouchChange("mius", event.getAction());
                break;
            case R.id.tv_plus:
                onTouchChange("plus", event.getAction());
                break;
        }
        return true;
    }

    //减操作
    class MiusThread extends Thread {
        @Override
        public void run() {
            while (isOnLongClick) {
                try {
                    Thread.sleep(100);
                    myHandler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }
    }

    //加操作
    class PlusThread extends Thread {
        @Override
        public void run() {
            while (isOnLongClick) {
                try {
                    Thread.sleep(100);
                    myHandler.sendEmptyMessage(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }
    }

    //更新文本框的值
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (miusEnable) {
                        mEtProgress.setText((new BigDecimal(
                                mEtProgress.getText().toString())
                                .subtract(new BigDecimal("1")))
                                + "");
                    }
                    break;
                case 2:
                    if (plusEnable) {
                        mEtProgress.setText((new BigDecimal(
                                mEtProgress.getText().toString())
                                .add(new BigDecimal("1")))
                                + "");
                    }
                    break;
            }
            setBtnEnable();
        }


    };

    //超出最大最小值范围按钮的可能与不可用
    private void setBtnEnable() {
        if (new BigDecimal(mEtProgress.getText().toString())
                .compareTo(new BigDecimal(minvalue + "")) > 0) {
            miusEnable = true;
            mTvMius.setImageResource(R.drawable.ic_remove_normal);
        } else {
            miusEnable = false;
            mTvMius.setImageResource(R.drawable.ic_remove_unormal);
        }
        if (new BigDecimal(mEtProgress.getText().toString())
                .compareTo(new BigDecimal(maxvalue + "")) < 0) {
            plusEnable = true;
            mTvPlus.setImageResource(R.drawable.ic_add_normal);
        } else {
            plusEnable = false;
            mTvPlus.setImageResource(R.drawable.ic_add_unormal);
        }
    }

    public void setEditOnClick(OnClickListener listener) {
        if (mIsEditVisibal == false) {
            mEtProgress.setOnClickListener(listener);
        }
    }

    public void setProgressValue(int value) {
        mProgressValue = value;
        mEtProgress.setText(mProgressValue + "");
    }

    public int getProgressValue() {
        return mProgressValue;
    }

    public interface IWatcherProgress {
        void valueChage(String value);
    }

    public void addWatcherProgressListenr(CustomCounterWidget.IWatcherProgress callBack) {
        this.mCallBack = callBack;
    }
}
