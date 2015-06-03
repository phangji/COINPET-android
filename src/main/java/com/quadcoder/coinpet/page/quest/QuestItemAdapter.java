package com.quadcoder.coinpet.page.quest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.quadcoder.coinpet.model.ParentQuest;
import com.quadcoder.coinpet.model.Quest;
import com.quadcoder.coinpet.model.Quiz;
import com.quadcoder.coinpet.model.SystemQuest;

import java.util.ArrayList;

/**
 * Created by Phangji on 6/2/15.
 */
public class QuestItemAdapter extends BaseAdapter implements QuestItemView.OnButtonListener{

    Context mContext;
    ArrayList<Quest> mItems = new ArrayList<>();

    public QuestItemAdapter(Context context) {
        this.mContext = context;
    }

    public QuestItemAdapter(Context context, ArrayList<Quest> items) {
        this.mContext = context;
        this.mItems = items;

        addAll(items);
    }

    public void addAll(ArrayList<Quest> items){
        if(items != null){
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void addParentAll(ArrayList<ParentQuest> items){
        if(items != null){
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void addSystemAll(ArrayList<SystemQuest> items){
        if(items != null){
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestItemView itemView;

        if(convertView != null){
            itemView = (QuestItemView)convertView;
        }else{
            itemView = new QuestItemView(mContext);
        }
        itemView.setQuestItem(mItems.get(position));

        return itemView;
    }

    public interface OnAdapterItemClickListener{
        public void onAdapterItemClick(View v, Quest item);
    }
    OnAdapterItemClickListener mListener;

    public void setOnAdapterClickListener(OnAdapterItemClickListener onAdapterItemClickListener){
        mListener = onAdapterItemClickListener;
    }

    @Override
    public void onButtonListen(View v, Quest item) {
        if(mListener != null){
            mListener.onAdapterItemClick(v, item);
        }
    }
}
