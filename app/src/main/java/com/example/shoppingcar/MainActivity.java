package com.example.shoppingcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    //模拟的购物车数据（实际开发中使用后台返回的数据）
    private String shoppingCarData = "{\n" +
            "    \"code\": 200,\n" +
            "    \"datas\": [\n" +
            "        {\n" +
            "            \"goods\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"111111\",\n" +
            "                    \"goods_image\": \"https://tenfei03.cfp.cn/creative/vcg/800/new/VCG41482783107.jpg\",\n" +
            "                    \"goods_name\": \"习近平谈治国理政\",\n" +
            "                    \"goods_num\": \"1\",\n" +
            "                    \"goods_price\": \"18.00\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"store_id\": \"1\",\n" +
            "            \"store_name\": \"一号小书店\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"goods\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"222221\",\n" +
            "                    \"goods_image\": \"https://tenfei03.cfp.cn/creative/vcg/800/new/VCG41482783107.jpg\",\n" +
            "                    \"goods_name\": \"马克思传 \",\n" +
            "                    \"goods_num\": \"1\",\n" +
            "                    \"goods_price\": \"28.00\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"222222\",\n" +
            "                    \"goods_image\": \"https://tenfei03.cfp.cn/creative/vcg/800/new/VCG41482783107.jpg\",\n" +
            "                    \"goods_name\": \"我和霍金的生活\",\n" +
            "                    \"goods_num\": \"1\",\n" +
            "                    \"goods_price\": \"58.00\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"store_id\": \"2\",\n" +
            "            \"store_name\": \"二号中书店\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"goods\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"333331\",\n" +
            "                    \"goods_image\": \"https://tenfei03.cfp.cn/creative/vcg/800/new/VCG41482783107.jpg\",\n" +
            "                    \"goods_name\": \"心的重建\",\n" +
            "                    \"goods_num\": \"1\",\n" +
            "                    \"goods_price\": \"38.00\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"333332\",\n" +
            "                    \"goods_image\": \"https://tenfei03.cfp.cn/creative/vcg/800/new/VCG41482783107.jpg\",\n" +
            "                    \"goods_name\": \"福尔摩斯\",\n" +
            "                    \"goods_num\": \"1\",\n" +
            "                    \"goods_price\": \"58.00\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"333333\",\n" +
            "                    \"goods_image\": \"https://tenfei03.cfp.cn/creative/vcg/800/new/VCG41482783107.jpg\",\n" +
            "                    \"goods_name\": \"书法常识\",\n" +
            "                    \"goods_num\": \"1\",\n" +
            "                    \"goods_price\": \"78.00\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"store_id\": \"3\",\n" +
            "            \"store_name\": \"三号大书店\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    private List<ShoppingCarDataBean.DatasBean> storeList;
    private ExpandableListView shoppingCar;
    private ImageView selectAll;
    private TextView allPrice;
    private Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        initExpandableListView();

//        btnOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this,payActivity.class);
//                startActivity(i);
//            }
//        });
    }

    private void initExpandableListView() {
        ShoppingCarAdapter adapter = new ShoppingCarAdapter(storeList, selectAll, allPrice,btnOrder);
        shoppingCar.setAdapter(adapter);

        //展开所有组
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            shoppingCar.expandGroup(i);
        }
        shoppingCar.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }

    private void initData() {
        //使用Gson解析数据
        Gson gson = new Gson();
        ShoppingCarDataBean shoppingCarDataBean = gson.fromJson(shoppingCarData, ShoppingCarDataBean.class);
        storeList = shoppingCarDataBean.getDatas();
    }

    private void initView() {
        shoppingCar = (ExpandableListView) findViewById(R.id.elv_shopping_car);
        selectAll = (ImageView) findViewById(R.id.iv_select_all);
        allPrice = (TextView) findViewById(R.id.tv_total_price);
        btnOrder = (Button) findViewById(R.id.btn_order);
    }



}