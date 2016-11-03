package cn.ucai.superwechat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.utils.MFGT;

/**
 * Created by mac-yk on 2016/11/3.
 */

public class PersonalCenterFragment extends Fragment {
    Activity mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        mContext= (Activity) this.getContext();
        return view;
    }

    @OnClick(R.id.txt_set)
    public void onClick() {
        MFGT.startActivity(mContext,new Intent(getContext(),SettingActivity.class));
    }
}
