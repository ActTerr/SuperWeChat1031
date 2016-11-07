package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.utils.MFGT;

/**
 * Created by mac-yk on 2016/11/8.
 */

public class FriendProfileActivity extends BaseActivity {
    User u;
    @BindView(R.id.userAvatar)
    ImageView userAvatar;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.wechatNumber)
    TextView wechatNumber;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_sendMSG)
    Button btnSendMSG;
    @BindView(R.id.btn_video)
    Button btnVideo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        u = (User) getIntent().getSerializableExtra("user");
        if (u == null) {
            finish();
        }
        initView();
    }

    private void initView() {
        setUserInfo();
        isFriend();
    }

    private void isFriend() {
        if (SuperWeChatHelper.getInstance().getAppContactList().containsKey(u.getMUserName())) {
            btnSendMSG.setVisibility(View.VISIBLE);
            btnVideo.setVisibility(View.VISIBLE);
        } else {
           btnAdd.setVisibility(View.VISIBLE);
        }
    }

    private void setUserInfo() {
        EaseUserUtils.setAppUserAvatar(this, u.getMUserName(), userAvatar);
        EaseUserUtils.setAppUserNick(u.getMUserNick(), username);
        EaseUserUtils.setAppUserNameWithInfo(u.getMUserName(), wechatNumber);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        MFGT.finish(this);
    }


    @OnClick({R.id.btn_add, R.id.btn_sendMSG, R.id.btn_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                MFGT.gotoAddFriendMSG(this,u.getMUserName());
                break;
            case R.id.btn_sendMSG:
                break;
            case R.id.btn_video:
                break;
        }
    }
}
