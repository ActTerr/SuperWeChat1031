/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superwechat.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.dao.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.utils.CommonUtils;
import cn.ucai.superwechat.utils.MD5;
import cn.ucai.superwechat.utils.MFGT;

/**
 * register screen
 */
public class RegisterActivity extends BaseActivity {

    Activity mContext;
    String userName;
    String pwd;
    String confirm_pwd;
    String nick;
    ProgressDialog pd;
    @BindView(R.id.username)
    EditText etUsername;
    @BindView(R.id.usernick)
    EditText etUsernick;
    @BindView(R.id.password)
    EditText etPassword;
    @BindView(R.id.confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.title_btn)
    Button titleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);
        ButterKnife.bind(this);
        mContext = this;
        titleBtn.setVisibility(View.GONE);

    }


    private void registerAppServer() {
        NetDao.UserRegister(mContext, userName, pwd, nick, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if (result == null) {
                    pd.dismiss();
                } else if (result != null && result.isRetMsg()) {
                    registerEMserver();
                } else if (result.getRetCode() == I.MSG_REGISTER_USERNAME_EXISTS) {
                    CommonUtils.showShortToast(result.getRetCode());
                } else {
                    pd.dismiss();
                    unRegisterAppServer();
                }

            }

            @Override
            public void onError(String error) {
                pd.dismiss();
            }
        });
    }

    private void unRegisterAppServer() {
        NetDao.UnRegister(mContext, userName, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void registerEMserver() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(userName, MD5.getMessageDigest(pwd));
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                pd.dismiss();
                            // save current user
                            SuperWeChatHelper.getInstance().setCurrentUserName(userName);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                pd.dismiss();
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NETWORK_ERROR) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                            }
                            pd.dismiss();
                        }
                    });
                }
            }
        }).start();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MFGT.finish(mContext);
    }

    @OnClick({R.id.iv_back, R.id.btn_onRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MFGT.finish(mContext);
                break;
            case R.id.btn_onRegister:
                userName = etUsername.getText().toString().trim();
                Log.e("main", userName);
                pwd = etPassword.getText().toString().trim();
                confirm_pwd = etConfirmPassword.getText().toString().trim();
                nick = etUsernick.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
                    etUsername.requestFocus();
                    return;
                } else if (!userName.matches("[a-zA-Z]\\w{5,15}")) {
                    Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_match), Toast.LENGTH_SHORT).show();
                    etUsername.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(nick)) {
                    Toast.makeText(this, getResources().getString(R.string.User_nick_cannot_be_empty), Toast.LENGTH_SHORT).show();
                    etUsername.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(confirm_pwd)) {
                    Toast.makeText(this, getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                    return;
                } else if (!pwd.equals(confirm_pwd)) {
                    Toast.makeText(this, getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(pwd)) {
                    pd = new ProgressDialog(this);
                    pd.setMessage(getResources().getString(R.string.Is_the_registered));
                    pd.show();
                    registerAppServer();
                }
                break;
        }
    }
}
