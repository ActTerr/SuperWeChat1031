package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.adapter.MainTabAdpter;
import cn.ucai.superwechat.widget.MFViewPager;

/**
 * Created by mac-yk on 2016/11/3.
 */

public class SettingActivity extends BaseActivity {
    MainTabAdpter mAdapter;
    @BindView(R.id.layout_viewpager)
    MFViewPager layoutViewpager;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
//        mAdapter = new MainTabAdpter(getSupportFragmentManager());
//        layoutViewpager.setAdapter(mAdapter);
//        layoutViewpager.setOffscreenPageLimit(1);
//        mAdapter.addFragment(new SettingsFragment(), "setting");
//        mAdapter.notifyDataSetChanged();
        SettingsFragment sf=new SettingsFragment();
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().add(R.id.activity_setting,sf).commit();

    }

}
