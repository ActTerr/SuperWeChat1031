package cn.ucai.superwechat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.redpacketui.utils.RedPacketUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.Constant;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.utils.MFGT;

import static cn.ucai.superwechat.R.id.username;

/**
 * Created by mac-yk on 2016/11/3.
 */

public class PersonalCenterFragment extends Fragment {
    Activity mContext;
    @BindView(R.id.userAvatar)
    ImageView userAvatar;
    @BindView(username)
    TextView usernick;
    @BindView(R.id.wechatNumber)
    TextView wechatNumber;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        mContext = (Activity) this.getContext();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null&&savedInstanceState.getBoolean("isConflict",false))
            return;
        initView();

    }

    private void initView() {
        EaseUserUtils.setCurrentAppUserAvatar(getActivity(),userAvatar);
        EaseUserUtils.setCurrentAppUserNick(usernick);
        EaseUserUtils.setCurrentUserName(wechatNumber);
    }


    @OnClick({R.id.txt_package, R.id.txt_set, R.id.enter_personal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_package:
                MFGT.gotoChange(getActivity());
                RedPacketUtil.startChangeActivity(mContext);
                break;
            case R.id.txt_set:
                MFGT.startActivity(mContext,SettingsActivity.class);
                break;
            case R.id.enter_personal:
                Intent intent=new Intent();
                intent.setClass(mContext,UserProfileActivity.class);
                String username= EMClient.getInstance().getCurrentUser();
                intent.putExtra("username",username);
                MFGT.startActivity(mContext,intent);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(((MainActivity)getActivity()).isConflict){
            outState.putBoolean("isConflict", true);
        }else if(((MainActivity)getActivity()).getCurrentAccountRemoved()){
            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }



}
