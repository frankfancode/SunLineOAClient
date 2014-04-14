/*
 * Copyright 2012-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ff.sunlineoaclient.view;

/**
 * @author Frank Fan
 * @since 0.0.1
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import org.ff.sunlineoaclient.R;
import org.ff.sunlineoaclient.activity.EmployeeListActivity;

public class InsertProgressDialog extends ProgressDialog {

    private RelativeLayout relative;
    private ProgressDialog bar;
    private TextView rateView;
    private EmployeeListActivity context;
    private int rate = 0;
    private float fArcNum;
    String text;
    Paint mPaint;

    protected static final int STOP = 0;
    protected static final int NEXT = 1;

    public InsertProgressDialog(Context c) {
        super(c);

        context = (EmployeeListActivity) c;

        bar = new ProgressDialog(context);
        rateView = new TextView(context);
        relative = (RelativeLayout) context.findViewById(R.id.activity_employee_list);
//设置ProgressBar的高宽和显示位置
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.getRules()[RelativeLayout.CENTER_IN_PARENT] = RelativeLayout.TRUE;
        //bar.setLayoutParams(params);

        initText();
        setText(20);

    }

    public void insertBar() {
        bar.setProgress(20);
//当ProgressBar正在运行时就不再创建ProgressBar
        if (!bar.isShowing()) {
            bar.show();
//            relative.addView(bar);

        }

    }


    // 初始化，画笔
    private void initText() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.WHITE);

    }

    // 设置文字内容
    private void setText(int progress) {
        int i = (int) ((progress * 1.0f / this.getMax()) * 100);
        this.text = String.valueOf(i) + "%";
    }
}