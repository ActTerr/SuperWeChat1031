package cn.ucai.superwechat.live.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.dao.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.live.data.model.Wallet;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;

/**
 * Created by mac-yk on 2016/12/20.
 */

public class changeActivity extends cn.ucai.superwechat.ui.BaseActivity {
    @BindView(R.id.left_image)
    ImageView leftImage;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subtitle)
    TextView subtitle;
    @BindView(R.id.tv_change_balance)
    TextView tvChangeBalance;

    View loadingView;
    @BindView(R.id.target_layout)
    LinearLayout targetLayout;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fragment_change);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        loadingView = LayoutInflater.from(this).inflate(R.layout.rp_loading, null);
        targetLayout.addView(loadingView);
        NetDao.loadChange(this, EMClient.getInstance().getCurrentUser(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, Wallet.class);
                    if (result != null && result.isRetMsg()) {
                        Wallet wallet = (Wallet) result.getRetData();
                        if (wallet != null) {
                            tvChangeBalance.setText("¥" + String.valueOf(Float.valueOf(wallet.getBalance())));
                            SuperWeChatHelper.getInstance().setCurrentUserChange(String.valueOf(wallet.getBalance()));
                        } else {
                            tvChangeBalance.setText("¥ 0.00");
                        }


                    }
                }

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void initView() {
        leftImage.setImageResource(R.drawable.rp_back_arrow_yellow);
        title.setText("零钱");
        subtitle.setText("云账户红包服务");
        tvChangeBalance.setText("¥ 0.00");
    }

    @OnClick({R.id.left_layout, R.id.tv_change_recharge, R.id.tv_change_withdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_layout:
                MFGT.finish(this);
                break;
            case R.id.tv_change_recharge:

                break;
            case R.id.tv_change_withdraw:
                break;
        }
    }
}
