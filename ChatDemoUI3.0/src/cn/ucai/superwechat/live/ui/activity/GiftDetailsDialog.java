package cn.ucai.superwechat.live.ui.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.dao.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.live.data.model.Gift;
import cn.ucai.superwechat.live.ui.adapter.GiftAdapter;
import cn.ucai.superwechat.utils.ResultUtils;

/**
 * Created by wei on 2016/7/25.
 */
public class GiftDetailsDialog extends DialogFragment {

    Unbinder unbinder;
    @BindView(R.id.iv_gift)
    RecyclerView ivGift;
    @BindView(R.id.bill)
    TextView bill;
    @BindView(R.id.btn_recharge)
    TextView btnRecharge;

    GridLayoutManager layoutManager;
    ArrayList<Gift> mList = new ArrayList<>();
    GiftAdapter adapter;
    public static GiftDetailsDialog newInstance() {
        GiftDetailsDialog dialog = new GiftDetailsDialog();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gift_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadGiftList();
    }

    private void initView() {
        layoutManager = new GridLayoutManager(getContext(), 4);
        ivGift.setLayoutManager(layoutManager);
        adapter = new GiftAdapter(mList, getContext());
        ivGift.setAdapter(adapter);
    }

    private void loadGiftList() {
        Map<String, Gift> giftList = SuperWeChatHelper.getInstance().getGiftList();
        if (giftList != null && !giftList.isEmpty()) {
            ArrayList<Gift> gifts=new ArrayList<>();
            for(Gift gift:giftList.values()){
                gifts.add(gift);
            }
            adapter.initData(gifts);
        } else {
            NetDao.addGiftList(getContext(), new OkHttpUtils.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if (s != null) {
                        Result result = ResultUtils.getListResultFromJson(s, Result.class);
                        if (result != null && result.isRetMsg()) {
                            ArrayList<Gift> gifts = (ArrayList<Gift>) result.getRetData();
                            if (gifts != null && gifts.size() > 0) {
                                adapter.initData(gifts);
                                SuperWeChatHelper.getInstance().updateAppGiftList(gifts);

                            }
                        }
                    }

                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }


    @OnClick(R.id.btn_follow)
    void onFollowBtnClick() {
    }


    public void setUserDetailsDialogListener( View.OnClickListener Listener) {
        adapter.setListener(Listener);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
        Dialog dialog = new Dialog(getActivity(), R.style.room_user_details_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(R.layout.fragment_room_user_details);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
