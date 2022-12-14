/******************************************************************
 *
 *    Java Lib For Android, Powered By Shenzhen Jiuzhou.
 *
 *    Copyright (c) 2001-2014 Digital Telemedia Co.,Ltd
 *    http://www.d-telemedia.com/
 *
 *    Package:     com.ukang.clinicaltrial.view
 *
 *    Filename:    Emial_EditText.java
 *
 *    Description: TODO(用一句话描述该文件做什么)
 *
 *    Copyright:   Copyright (c) 2001-2014
 *
 *    Company:     Digital Telemedia Co.,Ltd
 *
 *    @author:     Administrator
 *
 *    @version:    1.0.0
 *
 *    Create at:   2016年4月16日 下午5:00:41
 *
 *    Revision:
 *
 *    2016年4月16日 下午5:00:41
 *        - first revision
 *
 *****************************************************************/
package com.ukang.clinicaltrial.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @ClassName Emial_EditText
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @Date 2016年4月16日 下午5:00:41
 * @version 1.0.0
 */

public class Emial_EditText extends EditText {

    EditText textMessage;
    Context paramContext;

    public Emial_EditText(Context paramContext) {
        super(paramContext);
        this.paramContext = paramContext;
    }

    public Emial_EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Emial_EditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 初始化edittext 控件
    public void initEditText() {
        addTextChangedListener(new TextWatcher() { // 对文本内容改变进行监听

            @Override
            public void afterTextChanged(Editable s) {
                if (Emial_EditText.this.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") && s.length() > 0) {
                    //Toast.makeText(paramContext, "valid email", Toast.LENGTH_SHORT).show();
                    System.out.println("valid email");
                } else {
                    //Toast.makeText(paramContext, "invalid email", Toast.LENGTH_SHORT).show();
                    System.out.println("invalid email");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
            }

            @Override
            public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
            }
        });
    }
    
}
