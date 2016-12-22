package cn.ucai.superwechat.live.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.superwechat.live.data.model.Gift;

/**
 * Created by mac-yk on 2016/10/19.
 */

public class GiftAdapter extends RecyclerView.Adapter{
    List<Gift> mList;
    Context context;

    View.OnClickListener mListener;
    public GiftAdapter(List<Gift> mlist, Context context) {
        this.mList = new ArrayList<>();
        this.mList.addAll(mlist);
        this.context = context;
    }

    public void setListener( View.OnClickListener listener){
        mListener=listener;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = new BoutiqueViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gift, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Gift boutique = mList.get(position);
        BoutiqueViewHolder bvh = (BoutiqueViewHolder) holder;
//            ImageLoader.context,bvh.bouIv,boutique.getGurl());
//        EaseUserUtils.setAvatarforPath(context,boutique.getGurl(),bvh.ivGoodsThumb);
        bvh.tvGoodsPrice.setText(boutique.getGprice()+"");
        bvh.tvGoodsName.setText(boutique.getGname());
        bvh.layoutGoods.setTag(boutique);
        bvh.ivGoodsThumb.setImageResource(setGiftImage(boutique.getId()));
        bvh.itemView.setOnClickListener(mListener);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void initData(List<Gift> list) {
        if (mList != null) {
            mList.clear();
        }
        Collections.sort(list, new Comparator<Gift>() {
            @Override
            public int compare(Gift lhs, Gift rhs) {
                return lhs.getGprice()-rhs.getGprice();
            }
        });
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Gift> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }



    class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.layout_goods)
        LinearLayout layoutGoods;


        public BoutiqueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.layout_goods)
        public void onGoodsItemClick() {
            Gift bean = (Gift) layoutGoods.getTag();
//            MFGT.gotoBoutiqueChildActivity(context, bean);
        }

    }
    private int setGiftImage(int id){
        Context context = SuperWeChatApplication.getInstance().getApplicationContext();
        String name = "hani_gift_"+id;
        int resId = context.getResources().getIdentifier(name,"drawable",context.getPackageName());
        return resId;
    }

}
