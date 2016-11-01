package cn.ucai.superwechat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.utils.MFGT;

/**
 * Created by mac-yk on 2016/11/1.
 */

public class GuideActivity extends BaseActivity {
    Activity mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        mContext=this;
        setListener();
    }

    private void setListener() {
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("main","login被点击");
                MFGT.gotoLogin(mContext);
            }
        });
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("main","注册被点击");
                MFGT.gotoRegister(mContext);
            }
        });
    }



}
