package cn.ucai.superwechat.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Element;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseImageUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.util.EasyUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.dao.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.db.UserDao;
import cn.ucai.superwechat.utils.CommonUtils;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;

public class UserProfileActivity extends BaseActivity implements OnClickListener {

    private static final int REQUESTCODE_PICK = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    @BindView(R.id.txt_name)
    TextView tvNickName;
    @BindView(R.id.txt_number)
    TextView tvUsername;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_sex)
    TextView txtSex;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.linkedln)
    TextView linkedln;
    @BindView(R.id.personal_view)
    ScrollView personalView;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.userAvatar)
    ImageView headAvatar;
    private ProgressDialog dialog;
    private RelativeLayout rlNickName;
    String TAG = "UserProfileActivity";
    User u;
    String username;
    Activity mContext;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_user_profile);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        initListener();
    }

    private void initView() {

    }

    private void initListener() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Log.e(TAG, username);
        boolean enableUpdate = intent.getBooleanExtra("setting", false);
        if (enableUpdate) {

            rlNickName.setOnClickListener(this);
            headAvatar.setOnClickListener(this);
        } else {

        }
        if (username != null) {
            if (username.equals(EMClient.getInstance().getCurrentUser())) {
                L.e(TAG, "进入有用户名的分支");
                tvUsername.setText(EMClient.getInstance().getCurrentUser());
                EaseUserUtils.setUserNick(username, tvNickName);
                EaseUserUtils.setUserAvatar(this, username, headAvatar);
            } else {
                L.e(TAG, "进入无用户名的分支");
                tvUsername.setText(username);
                EaseUserUtils.setUserNick(username, tvNickName);
                EaseUserUtils.setUserAvatar(this, username, headAvatar);
                asyncFetchUserInfo(username);
            }
        }
    }


    public void asyncFetchUserInfo(String username) {
        SuperWeChatHelper.getInstance().getUserProfileManager().asyncGetUserInfo(username, new EMValueCallBack<EaseUser>() {

            @Override
            public void onSuccess(EaseUser user) {
                if (user != null) {
                    SuperWeChatHelper.getInstance().saveContact(user);
                    if (isFinishing()) {
                        return;
                    }
                    tvNickName.setText(user.getNick());
                    if (!TextUtils.isEmpty(user.getAvatar())) {
                        Glide.with(UserProfileActivity.this).load(user.getAvatar()).placeholder(R.drawable.em_default_avatar).into(headAvatar);
                    } else {
                        Glide.with(UserProfileActivity.this).load(R.drawable.em_default_avatar).into(headAvatar);
                    }
                }
            }

            @Override
            public void onError(int error, String errorMsg) {
            }
        });
    }


    private void uploadHeadPhoto() {
        Builder builder = new Builder(this);
        builder.setTitle(R.string.dl_title_upload_photo);
        builder.setItems(new String[]{getString(R.string.dl_msg_take_photo), getString(R.string.dl_msg_local_upload)},
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                Toast.makeText(UserProfileActivity.this, getString(R.string.toast_no_support),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(pickIntent, REQUESTCODE_PICK);
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }


    private void updateRemoteNick(final String nickName) {
        dialog = ProgressDialog.show(this, getString(R.string.dl_update_nick), getString(R.string.dl_waiting));
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean updatenick = SuperWeChatHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(nickName);
                if (UserProfileActivity.this.isFinishing()) {
                    return;
                }
                if (!updatenick) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatenick_fail), Toast.LENGTH_SHORT)
                                    .show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            NetDao.updateNick(mContext, username, nickName, new OkHttpUtils.OnCompleteListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    Result result = ResultUtils.getResultFromJson(s, User.class);

                                    if (result.getRetCode() == I.MSG_SUCCESS) {
                                        dialog.dismiss();
                                        User user = (User) result.getRetData();
                                        updateLoclNick(user);
                                        Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatenick_success), Toast.LENGTH_SHORT)
                                                .show();
                                        tvNickName.setText(nickName);
                                        UserDao userdao = new UserDao(mContext);
                                        userdao.updateUser(user);
                                    }
                                }

                                @Override
                                public void onError(String error) {
                                    CommonUtils.showShortToast(error);
                                    dialog.dismiss();
                                }
                            });

                        }
                    });
                }
            }
        }).start();
    }

    private void updateLoclNick(User user) {
        u=user;
        SuperWeChatHelper.getInstance().saveAppContact(u);
        EaseUserUtils.setAppUserNick(tvNickName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_PICK:
                if (data == null || data.getData() == null) {
                    return;
                }
                startPhotoZoom(data.getData());
                break;
            case REQUESTCODE_CUTTING:
                if (data != null) {
                    updateUserAvatar(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUserAvatar(final Intent data) {
        dialog = ProgressDialog.show(this, getString(R.string.dl_update_photo), getString(R.string.dl_waiting));
        dialog.show();
        File file = saveBitmapFile(data);
        L.e(TAG, file.toString());
        NetDao.updateAvatar(mContext, username, I.AVATAR_TYPE_USER_PATH, file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null && result.isRetMsg()) {
                        setPicToView(data);
                    } else {
                        dialog.dismiss();
                        CommonUtils.showShortToast("错误代码" + result.getRetCode());
                    }
                } else {
                    dialog.dismiss();
                    CommonUtils.showShortToast(R.string.toast_updatephoto_fail);
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG, error);
                CommonUtils.showShortToast(R.string.toast_updatephoto_fail);
            }
        });
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * save the picture data
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(getResources(), photo);
            headAvatar.setImageDrawable(drawable);
            uploadUserAvatar(Bitmap2Bytes(photo));
        }

    }

    private void uploadUserAvatar(final byte[] data) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                final String avatarUrl = SuperWeChatHelper.getInstance().getUserProfileManager().uploadUser(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (avatarUrl != null) {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatephoto_success),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatephoto_fail),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        }).start();


    }


    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    @OnClick({R.id.iv_back, R.id.btn_name, R.id.btn_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MFGT.finish(this);
                break;
            case R.id.btn_name:
                updateNick();
                break;
            case R.id.btn_avatar:

                uploadHeadPhoto();
        }
    }

    private void updateNick() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle(R.string.setting_nickname).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickString = editText.getText().toString();
                        if (TextUtils.isEmpty(nickString)) {
                            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_nick_not_isnull), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        updateRemoteNick(nickString);
                    }
                }).setNegativeButton(R.string.dl_cancel, null).show();
    }

    public File saveBitmapFile(Intent data) {
        Bundle extra = data.getExtras();
        if (extra != null) {
            Bitmap bitmap = extra.getParcelable("data");
            long currenttime = System.currentTimeMillis();
            String path = EaseImageUtils.getImagePath(username + currenttime + I.AVATAR_SUFFIX_JPG);
            SharedPreferences sp = getSharedPreferences(username, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("avatar", path);
            editor.commit();
            L.e(TAG, path.toString());
            File file = new File(path);
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return file;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAavatar();
        initListener();
    }

    private void showAavatar() {
        String path = getSharedPreferences(username, MODE_PRIVATE).getString("avatar", "");
        L.e(TAG, "从首选项取得" + path);
        Bitmap bit = BitmapFactory.decodeFile(path);
        headAvatar.setImageBitmap(bit);
    }


}
