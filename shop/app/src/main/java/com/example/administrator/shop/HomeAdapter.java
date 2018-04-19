package com.example.administrator.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 一共定义了两个class，第二个类包含在第一个类中。
 *
 *
 * Homeadapter-------->主页的recyclerview 的adapter
 * 继承于 RecyclerView.Adapter<HomeAdapter.MyViewHolder> 类
 *
 *
 * MyViewHolder ------> 自定义的RecyclerView.ViewHolder
 * 继承于 RecyclerView.ViewHolder 类
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
{
    private Context context; //抽象基类，用于提供当前上下文
    private List<item> mDatas;  //商品列表数据
    private OnItemClickListener mOnItemClickListener;

    //自定义的viewholder类

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        public  TextView name;
        public  TextView first_letter;
        //MyViewHolder 类的构造函数
        public MyViewHolder( View view )
        {
            super(view);
            name = (TextView)view.findViewById(R.id.name);
            first_letter = (TextView)view.findViewById(R.id.first_letter);
        }
    }

    //构造函数
    public HomeAdapter(List<item> mDatas, Context context){
        this.mDatas = mDatas;
        this.context = context;
    }

    //移除数据函数
    public void removeData(int position)
    {
        mDatas.remove(position);
        notifyDataSetChanged();
    }
    //声明一个主页item点击的接口函数（监听器），包括了点击和长摁
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }


    //对商品设置监听器
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    // ******************************开始必须的函数重载*************************

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        //创建viewholder对象，目标是home_item.xml文件
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.home_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position){
        //设置名字
        holder.name.setText(mDatas.get(position).getName());
        //设置名字的首字母
        holder.first_letter.setText(mDatas.get(position).getName().substring(0,1));

        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount(){
        return mDatas.size();
    }


}
