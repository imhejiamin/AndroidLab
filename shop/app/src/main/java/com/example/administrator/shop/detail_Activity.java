package com.example.administrator.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
* 商品详情页面
*
* */

public class detail_Activity extends Activity {

    private ListView my_list;
    private List<String> array_list;

    private TextView goodname;
    private TextView goodprice;
    private TextView goodBelong;

    private DetailAdapter detailAdapter;

    private ImageView pic;
    private ImageView car;
    private ImageView star;

    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //把当前的activity设置为详情页
        setContentView(R.layout.detail_page);


        //为详情页下方的list配置Listview内容
        my_list = (ListView) findViewById(R.id.list);
        String[] texts = new String[] {"一键下单","分享商品","不感兴趣","查看更多商品促销信息"};
        array_list = new ArrayList<>();
        for (int i=0; i < texts.length; i++){
            array_list.add(texts[i]);
            //依次向list中添加字符串文本内容
        }
        my_list.setAdapter(detailAdapter = new DetailAdapter(detail_Activity.this, array_list));


        //接受从MainActivity传来的信息
        goodname = (TextView) findViewById(R.id.name);
        goodprice = (TextView) findViewById(R.id.price);
        goodBelong = (TextView) findViewById(R.id.belong);
        pic = (ImageView) findViewById(R.id.pic);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String name = null;
        String price = null;
        String belong = null;
        if(extras != null){
            name = extras.getString("name");
            price = extras.getString("price");
            belong = extras.getString("belong");
        }
        goodname.setText(name); //依次向详情页传入名字，价格，和相属信息
        goodprice.setText(price);
        goodBelong.setText(belong);

        //按照传入的名字信息，来设置详情页的xml文件里上方显示的商品图片
        if(name.equals("Enchated Forest") ){
            pic.setImageResource(R.drawable.enchatedforest);
        }
        else if(name.equals("BasfordArla Milk")){
            pic.setImageResource(R.drawable.arla);
        }
        else if(name.equals("Devondale Milk")){
            pic.setImageResource(R.drawable.devondale);
        }
        else if(name.equals("Kindle Oasis")){
            pic.setImageResource(R.drawable.kindle);
        }
        else if(name.equals("waitrose 早餐麦片")){
            pic.setImageResource(R.drawable.waitrose);
        }
        else if(name.equals("Mcvitie's 饼干")){
            pic.setImageResource(R.drawable.mcvitie);
        }
        else if(name.equals("Ferrero Rocher")){
            pic.setImageResource(R.drawable.ferrero);
        }
        else if(name.equals("Maltesers")){
            pic.setImageResource(R.drawable.maltesers);
        }
        else if(name.equals("Lindt")){
            pic.setImageResource(R.drawable.lindt);
        }
        else if(name.equals("Borggreve")){
            pic.setImageResource(R.drawable.borggreve);
        }

        final Intent intent1 = new Intent();
        //setResult(0,intent1);

        // 点击返回键，结束当前的activity
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        car = (ImageView) findViewById(R.id.car);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "商品已添加到购物车", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();

                bundle.putString("name", goodname.getText().toString());
                bundle.putString("price",goodprice.getText().toString());
                bundle.putString("belong", goodBelong.getText().toString());

                intent1.putExtras(bundle);
                setResult(1, intent1);
            }
        });

        //星星点击的设置 用tag记录，然后更换星星位置的图片来源达到点击效果
        star = (ImageView) findViewById(R.id.star);
        star.setTag("0");
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = star.getTag();
                if(tag == "0"){
                    star.setImageResource(R.drawable.full_star);
                    star.setTag("1");
                }else {
                    star.setImageResource(R.drawable.empty_star);
                    star.setTag("0");
                }
            }
        });
    }
}
