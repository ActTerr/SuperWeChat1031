package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.utils.MFGT;

/**
 * Created by mac-yk on 2016/11/8.
 */

public class AddFriendActivity extends BaseActivity {
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.title_btn)
    Button titleBtn;
    @BindView(R.id.edit_msg)
    EditText editMsg;
    private ProgressDialog progressDialog;
    String username;
    String msg;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);

        username=getIntent().getStringExtra("name");
        if (username==null) {
            finish();
        }
        initView();
    }

    private void initView() {
        tvTitleCenter.setVisibility(View.VISIBLE);
        tvTitleCenter.setText("加好友");
        titleBtn.setVisibility(View.VISIBLE);
        msg="我是"+ SuperWeChatHelper.getInstance().getCurrentUsernName();
        editMsg.setText(msg);
        progressDialog=new ProgressDialog(this);
    }

    @OnClick({R.id.iv_back, R.id.title_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MFGT.finish(this);
                break;
            case R.id.title_btn:
                send();
                break;
        }
    }

    private void send() {
        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(username, msg);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();

                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                            MFGT.finish(AddFriendActivity.this);
                        }
                    });
                }
            }
        }).start();
    }
}
