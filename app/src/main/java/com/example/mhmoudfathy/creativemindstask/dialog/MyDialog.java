package com.example.mhmoudfathy.creativemindstask.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.mhmoudfathy.creativemindstask.R;

/**
 * Created by mhmoud fathy on 11/4/2017.
 */

public class MyDialog extends Dialog implements android.view.View.OnClickListener {

        public Activity activity;
        public MyDialog dialog;
        public Button yes, no;

        public MyDialog(Activity activity) {
            super(activity);

            this.activity = activity;
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog);
            yes = (Button) findViewById(R.id.btn_yes);
            no = (Button) findViewById(R.id.btn_no);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_yes:
                    dismiss();
                    break;
                case R.id.btn_no:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

