package com.example.administrator.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 一共定义了两个class，第二个类包含在第一个类中。
 *
 * ShopAdapter-------->购物车的listview 的adapter
 * 继承于BaseAdapter 类
 *
 *
 * ViewHolder ------>用于装载封装一些数据
 */

public class ShopAdapter extends BaseAdapter {
    private Context context;  //当前上下文
    private List<item> mDatas; //购物车里面的商品列表

    public class ViewHolder {
        TextView first_letter;
        TextView name;
        TextView price;

    }
    //构造函数
    public ShopAdapter(Context context, List<item> mDatas){
        this.context = context;
        this.mDatas = mDatas;
    }


    public void removeData(int position)
    {
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        if(mDatas == null){
            return 0;
        }
        else
        return mDatas.size();
    }
    @Override
    public Object getItem(int i){
        if(mDatas == null){
            return 0;
        }
        else
        return mDatas.get(i);
    }
    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        //新声明一个View变量和ViewHolder变量
        View convertView;
        ViewHolder viewHolder;

        //当view为空时才加载布局，并且创建一个ViewHolder，获得布局中的3个控件。
        if(view == null){

            //通过inflate方法加载布局，context这个参数需要使用adapter的Activity传入
            convertView = LayoutInflater.from(context).inflate(R.layout.car_item , null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.first_letter = (TextView)convertView.findViewById(R.id.first_letter);
            viewHolder.price = (TextView)convertView.findViewById(R.id.price);

            //用setTag方法将处理好的viewHolder放入view中
            convertView.setTag(viewHolder);
        }
        else{
            //否则，让convertView等于view，然后从中取出ViewHolder即可。
            convertView = view;
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //从ViewHolder中取出对应的对象，然后赋值
        viewHolder.name.setText(mDatas.get(i).getName());
        if(i == 0){
            viewHolder.first_letter.setText("*");
        }
        else{
            viewHolder.first_letter.setText(mDatas.get(i).getName().substring(0,1));
        }
        viewHolder.price.setText(mDatas.get(i).getPrice());
        return convertView;
    }


}
