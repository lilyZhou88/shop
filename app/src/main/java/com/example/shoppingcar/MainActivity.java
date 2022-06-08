package com.example.shoppingcar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<ShoppingCarDataBean.DatasBean> datasTemp;
    private int n = 0;

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
    private TextView edit;
    private ShoppingCarAdapter adapter;
    private TextView update;
    private RelativeLayout rl;
    private Button delete;
    private LinearLayout background;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        initExpandableListView();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
                initExpandableListView();
                rl.setVisibility(View.VISIBLE);
                delete.setVisibility(View.GONE);
                allPrice.setVisibility(View.VISIBLE);
                btnOrder.setVisibility(View.VISIBLE);
                shoppingCar.setVisibility(View.VISIBLE);
                background.setVisibility(View.GONE);
                edit.setText("编辑");
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editText = edit.getText().toString();
                if (editText.equals("编辑")) {
                    edit.setText("完成");

                    total.setVisibility(View.GONE);
                    allPrice.setVisibility(View.GONE);
                    btnOrder.setVisibility(View.GONE);
                    delete.setVisibility(View.VISIBLE);
                } else if (editText.equals("完成")) {

                    delete.setVisibility(View.GONE);
                    allPrice.setVisibility(View.VISIBLE);
                    btnOrder.setVisibility(View.VISIBLE);
                    edit.setText("编辑");
                }
            }
        });
//        btnOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this,payActivity.class);
//                startActivity(i);
//            }
//        });
    }

    private void initExpandableListView() {
        adapter = new ShoppingCarAdapter(storeList, selectAll, allPrice, btnOrder,delete);
        shoppingCar.setAdapter(adapter);

        adapter.setOnDeleteListener(new ShoppingCarAdapter.OnDeleteListener() {
            @Override
            public void delete() {

                //判断是否有店铺或商品被选中
                //true为有，则需要刷新数据；反之，则不需要；
                boolean hasSelect = false;
                //创建临时的List，用于存储没有被选中的购物车数据
                datasTemp = new ArrayList<>();

                for (int i = 0; i < storeList.size(); i++) {
                    List<ShoppingCarDataBean.DatasBean.GoodsBean> goods = storeList.get(i).getGoods();
                    boolean isSelect_shop = storeList.get(i).getIsSelect_shop();

                    if (isSelect_shop) {
                        hasSelect = true;
                        //跳出本次循环，继续下次循环。
                        continue;
                    } else {
                        try {
                            datasTemp.add(storeList.get(i).clone());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        datasTemp.get(datasTemp.size() - 1).setGoods(new ArrayList<ShoppingCarDataBean.DatasBean.GoodsBean>());
                    }

                    for (int y = 0; y < goods.size(); y++) {
                        ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = goods.get(y);
                        boolean isSelect = goodsBean.getIsSelect();

                        if (isSelect) {
                            hasSelect = true;
                        } else {
                            datasTemp.get(datasTemp.size() - 1).getGoods().add(goodsBean);
                        }
                        n = n + 1;



//                        if (datasTemp.size() == 0){
//                            ll.setVisibility(View.GONE);
//                        }
                    }
                }
                Log.d(TAG, "falseNum: " + n);


                if (hasSelect) {
//                    showDeleteDialog(datasTemp);
                    dialog();
                } else {
                    Toast.makeText(MainActivity.this, "请选择", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
        allPrice = (TextView) findViewById(R.id.price_id);
        btnOrder = (Button) findViewById(R.id.btn_order);
        edit = (TextView) findViewById(R.id.edit_id);
        update = (TextView) findViewById(R.id.update_id);
        rl = (RelativeLayout) findViewById(R.id.rl);
        delete = (Button) findViewById(R.id.btn_delete);
        background = (LinearLayout) findViewById(R.id.background_id);
        total = (TextView) findViewById(R.id.total);
    }


    public void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher_background)
                .setMessage("确定要删除商品吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        storeList = datasTemp;
                        adapter.setData(storeList);
                        if (n == 0){
                            rl.setVisibility(View.GONE);
                            shoppingCar.setVisibility(View.GONE);
                            background.setVisibility(View.VISIBLE);
                        }
                        n = 0;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }
}