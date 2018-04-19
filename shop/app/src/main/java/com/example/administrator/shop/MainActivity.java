package com.example.administrator.shop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * main activity 包括了商品列表主页和购物车页面
 */

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRcyclerView;//商品列表主页的view
    private ListView shopListView; //商品购物车的view

    private HomeAdapter homeAdapter;  //recycler view 的适配器
    private ShopAdapter shopAdapter;   //list view  的适配器

    private List<item> mDatas;      //商店里的数据
    private List<item> shopDatas;   //购物车里面的数据

    private FloatingActionButton FAB_change;  //浮动图标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatas = new ArrayList<>();
        final String[] commodity_name = new String[] {
                "Enchated Forest",
                "BasfordArla Milk",
                "Devondale Milk",
                "Kindle Oasis",
                "waitrose 早餐麦片",
                "Mcvitie's 饼干",
                "Ferrero Rocher",
                "Maltesers",
                "Lindt",
                "Borggreve" };

        final String[] commodity_price = new String[] {
                "￥ 5.00", "￥ 59.00", "￥ 79.00", "￥ 2399.0", "￥ 179.00",
                "￥ 14.9", "¥ 132.59", "¥ 141.43", "¥ 139.43", "¥ 28.90"};

        final String[] commodity_belong = new String[] {
                "作者 Johanna Basford",
                "产地 德国",
                "产地 澳大利亚",
                "版本 8GB",
                "重量 2Kg",
                "产地 英国",
                "重量 300g",
                "重量 118g",
                "重量 249g",
                "重量 640g"};

        //把商品信息都累加到mDatas里面
        for(int i = 0; i < commodity_name.length ; i++){
            item shop = new item(commodity_name[i], commodity_price[i], commodity_belong[i]);
            mDatas.add(shop);
        }

        //获取这个recyclerview
        mRcyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);

        //线性显示这个recyclerview
        mRcyclerView.setLayoutManager(new LinearLayoutManager(this));

        //使用自己定义的Adapter为recyclerview传输数据
        mRcyclerView.setAdapter(  homeAdapter = new HomeAdapter(mDatas, this)  );

        //开始用自己定义的监听器接口函数监听
        homeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener(){

            //重写“点击”函数
            @Override
            public void onClick(int position){ //position是商品列表的下标
                Intent intent = new Intent(MainActivity.this, detail_Activity.class); //跳转到详情页
                intent.putExtra("name", mDatas.get(position).getName());  //Intent封装数据
                intent.putExtra("price",mDatas.get(position).getPrice());
                intent.putExtra("belong", mDatas.get(position).getBelong());
                startActivityForResult(intent, 0);
                /* 用 startActivityForResult 不用startActivity
                 * 是因为前者可以回传数据
                 * 0:请求码
                 */
            }

            //重写“长摁”函数
            @Override
            public void onLongClick(int position){
                Toast.makeText(MainActivity.this, "移除第"+position+"个商品", Toast.LENGTH_SHORT).show();
                homeAdapter.removeData(position);

            }
        });

        //创建购物车
        shopDatas = new ArrayList<>();

        //添加购物车标题
        item shop_car_title = new item("购物车","价格","");
        shopDatas.add(shop_car_title);

        //获取购物车的Listview
        shopListView = (ListView)findViewById(R.id.my_list_view);

        //为这个Listview设置自己定义的adapter
        shopListView.setAdapter( shopAdapter = new ShopAdapter(MainActivity.this, shopDatas) );

        //设置这个Listview暂时不可见
        shopListView.setVisibility(View.INVISIBLE);

        //对listview里面的商品设置 “点击” 监听器
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
                if(position == 0)  ;  //第0个商品是购物车标题，不管
                else{
                    //从购物车的页面回到详情页面
                    Intent intent = new Intent(MainActivity.this, detail_Activity.class);
                    intent.putExtra("name", shopDatas.get(position).getName());
                    intent.putExtra("price",shopDatas.get(position).getPrice());
                    intent.putExtra("belong", shopDatas.get(position).getBelong());
                    startActivityForResult(intent, 0);
                }
            }
        });

        //对listview 里面商品设置 “长摁” 监听器
        shopListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id){

                //get到商品的序号
                item p = (item) parent.getItemAtPosition(position);

                String name = "从购物车移除" + p.getName();

                //弹出对话框
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("移除商品")
                           .setMessage(name+"?")
                           .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i){
                                    try{
                                        Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                        field.setAccessible(true);
                                        field.set(dialogInterface, true);
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                           })
                           .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i){
                                     shopAdapter.removeData(position);
                                }})
                           .show();


                return true;
            }
        });

        FAB_change = (FloatingActionButton) findViewById(R.id.fab);
        FAB_change.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(mRcyclerView.getVisibility() == View.VISIBLE && shopListView.getVisibility() == View.INVISIBLE){
                    mRcyclerView.setVisibility(View.INVISIBLE);
                    shopListView.setVisibility(View.VISIBLE);
                    FAB_change.setImageResource(R.drawable.mainpage);
                }
                else if(mRcyclerView.getVisibility() == View.INVISIBLE && shopListView.getVisibility() == View.VISIBLE){
                    mRcyclerView.setVisibility(View.VISIBLE);
                    shopListView.setVisibility(View.INVISIBLE);
                    FAB_change.setImageResource(R.drawable.shoplist);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 0 && resultCode == 1){
            //添加到购物车
            Bundle extras = intent.getExtras();
            String name = extras.getString("name");
            String price = extras.getString("price");
            String belong = extras.getString("belong");
            item new_shop = new item(name, price, belong);
            shopDatas.add(new_shop);
            shopAdapter.notifyDataSetChanged();
        }
    }
}
