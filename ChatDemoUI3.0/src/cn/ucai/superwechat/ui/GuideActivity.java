package cn.ucai.superwechat.ui;

import android.app.Activity;
import android.os.Bundle;
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
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                MFGT.gotoLogin(mContext);
                break;
            case R.id.btn_register:
                MFGT.gotoRegister(mContext);
                break;
        }
    }
}
