package com.quadcoder.coinpet.page.quest;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.quadcoder.coinpet.model.ParentQuest;
import com.quadcoder.coinpet.model.Quest;
import com.quadcoder.coinpet.model.SystemQuest;

import java.util.ArrayList;

/**
 * Created by Phangji on 6/2/15.
 */
public class QuestItemAdapter extends BaseAdapter implements QuestItemView.OnButtonListener {

    ArrayList<Quest> mItems = new ArrayList<>();

    public QuestItemAdapter() {
    }

    public QuestItemAdapter(ArrayList<Quest> items) {
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

    public void addActiveQuest(SystemQuest item){
        if(item != null){
            mItems.add(item);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyDataSetChanged();
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

        if(convertView == null){
            itemView = new QuestItemView(parent.getContext());
            itemView.setOnButtonListener(QuestItemAdapter.this);

        }else{
            itemView = (QuestItemView)convertView;
        }

        itemView.setQuestItem(mItems.get(position));

        return itemView;
    }

    public interface OnAdapterClickListener {
        void onAdapterClick(View v, Quest item);
    }

    private OnAdapterClickListener mAdapterListener;

    public void setOnAdapterClickListener(OnAdapterClickListener onAdapterClickListener){
        mAdapterListener = onAdapterClickListener;
    }

    @Override
    public void onButtonListen(View v, Quest item) {
        if(mAdapterListener != null){
            mAdapterListener.onAdapterClick(v, item);
        }
    }
}
