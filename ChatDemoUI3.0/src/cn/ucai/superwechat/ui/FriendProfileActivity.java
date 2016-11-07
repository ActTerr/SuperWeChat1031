package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.superwechat.R;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        u = (User) getIntent().getSerializableExtra("user");
        if (u==null){
            finish();
        }
        initView();
    }

    private void initView() {
        EaseUserUtils.setAppUserAvatar(this,u.getMUserName(),userAvatar);
        EaseUserUtils.setAppUserNick(u.getMUserName(),username);
        EaseUserUtils.setAppUserNameWithInfo(u.getMUserName(),wechatNumber);
    }
}
