package com.example.administrator.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 *这是一个继承于baseadapter的Listview适配器，用于配置详情页的最下面那个Listview的四行文字信息
 */

public class DetailAdapter extends BaseAdapter{

        private Context context;
        private List<String> text;

        /* 构造函数
         * 第一个参数为当前的activity的java文件名.this
         * 第二个参数为传进listview的内容
         */
        public DetailAdapter(Context context, List<String> text){
            this.context = context;
            this.text = text;
        }
        @Override
        public int getCount(){
            if(text == null){
                return 0;
            }
            return text.size();
        }
        @Override
        public Object getItem(int i){
            if(text == null){
                return 0;
            }
            return text.get(i);
        }
        @Override
        public long getItemId(int i){
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View convertView;
            DetailAdapter.ViewHolder viewHolder;
            if(view == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.more_information,null);
                viewHolder = new DetailAdapter.ViewHolder();
                viewHolder.a = (TextView)convertView.findViewById(R.id.more);
                convertView.setTag(viewHolder);
            } else {
                convertView = view;
                viewHolder = (DetailAdapter.ViewHolder)convertView.getTag();
            }
            viewHolder.a.setText(text.get(i));
            return convertView;
        }

        private class ViewHolder{
            public TextView a;
        }
    }

