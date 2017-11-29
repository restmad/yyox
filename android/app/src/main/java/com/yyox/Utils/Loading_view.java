package com.yyox.Utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.yyox.R;
//自定义loading(加载圈)
/**
 * 加载提醒对话框
 */
public class Loading_view extends ProgressDialog
{
    public Loading_view(Context context)
    {
        super(context);
    }

    public Loading_view(Context context, int theme)
    {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private void init(Context context)
    {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.loading);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show()
    {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

