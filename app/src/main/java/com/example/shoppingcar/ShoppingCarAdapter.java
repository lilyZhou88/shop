package com.example.shoppingcar;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCarAdapter extends BaseExpandableListAdapter {
    private final List<ShoppingCarDataBean.DatasBean> dataBeans;
    private final ImageView selectAll;
    private final TextView allPrice;
    private Button btnOrder;
    private Boolean isSelectAll = false;
    private double all;

    public ShoppingCarAdapter(List<ShoppingCarDataBean.DatasBean> datasBeans, ImageView selectAll, TextView allPrice, Button btnOrder) {
        this.dataBeans = datasBeans;
        this.selectAll = selectAll;
        this.allPrice = allPrice;
        this.btnOrder = btnOrder;
        this.btnOrder = btnOrder;
    }

    @Override
    public int getGroupCount() {
        if (dataBeans == null) {
            return 0;
        }
        return dataBeans.size();
    }

    //刷新
//    public void setData(List<ShoppingCarDataBean.DatasBean> data) {
//        this.dataBeans = data;
//        notifyDataSetChanged();
//    }


    @Override
    public int getChildrenCount(int groupPosition) {
        if (dataBeans.get(groupPosition).getGoods() == null) {
            return 0;
        }
        return dataBeans.get(groupPosition).getGoods().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataBeans.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        ShoppingCarDataBean.DatasBean datasBean = dataBeans.get(groupPosition);
        String storeId = datasBean.getStore_id();
        String storeName = datasBean.getStore_name();

        //isSelect不可以在下面的点击事件用，因为这个值在下面用就是固定值

        ImageView selectIcon = itemView.findViewById(R.id.select_id);
        TextView store = itemView.findViewById(R.id.store_name);
        store.setText(storeName);

        //店铺内的商品都选中的时候，店铺的也要选中
        for (int i = 0; i < datasBean.getGoods().size(); i++) {
            ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = datasBean.getGoods().get(i);
            boolean Select = goodsBean.getIsSelect();
            if (Select) {
                datasBean.setIsSelect_shop(true);
            } else {
                datasBean.setIsSelect_shop(false);
                break;
            }
        }
        //因为set之后要重新get，所以这一块代码要放到一起执行
        //店铺是否在购物车中被选中
        final boolean isSelect_shop = datasBean.getIsSelect_shop();
        if (isSelect_shop) {
            selectIcon.setImageResource(R.drawable.select);
        } else {
            selectIcon.setImageResource(R.drawable.unselect);
        }

        //店铺选择框的点击事件
        selectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSelect = datasBean.getIsSelect_shop();
                datasBean.setIsSelect_shop(!isSelect);
                for (int i = 0; i < datasBean.getGoods().size(); i++) {
                    datasBean.getGoods().get(i).setIsSelect(!isSelect);
                }
                notifyDataSetChanged();
//                if (!isSelect){
//                    selectIcon.setImageResource(R.drawable.select);
//                    datasBean.setIsSelect_shop(true);
//                    return;
//                }
//                selectIcon.setImageResource(R.drawable.unselect);
//                datasBean.setIsSelect_shop(false);
            }
        });

        //当所有的选择框都是选中的时候，全选也要选中
        w:
        for (int i = 0; i < dataBeans.size(); i++) {
            List<ShoppingCarDataBean.DatasBean.GoodsBean> goods = dataBeans.get(i).getGoods();
            for (int j = 0; j < goods.size(); j++) {
                ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = goods.get(j);
                boolean isSelect = goodsBean.getIsSelect();
                if (isSelect) {
                    isSelectAll = true;
                } else {
                    isSelectAll = false;
                    break w;//根据标记，跳出嵌套循环
                }
            }
        }
        if (isSelectAll) {
            selectAll.setImageResource(R.drawable.select);
        } else {
            selectAll.setImageResource(R.drawable.unselect);
        }


        //全选的点击事件
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelectAll = !isSelectAll;
                for (int i = 0; i < dataBeans.size(); i++) {
                    List<ShoppingCarDataBean.DatasBean.GoodsBean> goods = dataBeans.get(i).getGoods();
                    for (int j = 0; j < goods.size(); j++) {
                        ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = goods.get(j);
                        goodsBean.setIsSelect(isSelectAll);
                    }
                }
                notifyDataSetChanged();
            }
        });

        //去结算的点击事件
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建临时的List，用于存储有商品被选中的店铺
                List<ShoppingCarDataBean.DatasBean> tempStores = new ArrayList<>();
                for (int i = 0; i < dataBeans.size(); i++) {
                    //店铺中是否有商品被选中
                    boolean hasGoodsSelect = false;
                    //创建临时的List，用于存储被选中的商品
                    List<ShoppingCarDataBean.DatasBean.GoodsBean> tempGoods = new ArrayList<>();

                    ShoppingCarDataBean.DatasBean storesBean = dataBeans.get(i);
                    List<ShoppingCarDataBean.DatasBean.GoodsBean> goods = storesBean.getGoods();
                    for (int y = 0; y < goods.size(); y++) {
                        ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = goods.get(y);
                        boolean isSelect = goodsBean.getIsSelect();
                        if (isSelect) {
                            hasGoodsSelect = true;
                            tempGoods.add(goodsBean);
                        }
                    }

                    if (hasGoodsSelect) {
                        ShoppingCarDataBean.DatasBean storeBean = new ShoppingCarDataBean.DatasBean();
                        storeBean.setStore_id(storesBean.getStore_id());
                        storeBean.setStore_name(storesBean.getStore_name());
                        storeBean.setGoods(tempGoods);

                        tempStores.add(storeBean);
                    }
                }

                if (tempStores != null && tempStores.size() > 0) {//如果有被选中的
                    /**
                     * 实际开发中，如果有被选中的商品，
                     * 则跳转到确认订单页面，完成后续订单流程。
                     */
                }
            }
        });

        return itemView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
        ShoppingCarDataBean.DatasBean datasBean = dataBeans.get(groupPosition);
        ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = datasBean.getGoods().get(childPosition);

        ImageView select = itemView.findViewById(R.id.select_id1);
        TextView goodName = itemView.findViewById(R.id.tv_name);
        TextView goodPrice = itemView.findViewById(R.id.tv_price);
        TextView goodNum = itemView.findViewById(R.id.tv_buy_number);
        ImageView minus = itemView.findViewById(R.id.minus_id);
        ImageView plus = itemView.findViewById(R.id.plus_id);


        goodNum.setText(datasBean.getGoods().get(childPosition).getGoods_num());
        goodName.setText(goodsBean.getGoods_name());
        goodPrice.setText("￥" + goodsBean.getGoods_price());

        //减号的点击事件
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(goodNum.getText().toString());
                if (num > 1) {
                    num = num - 1;
                    datasBean.getGoods().get(childPosition).setGoods_num(num + "");
                    goodNum.setText(String.valueOf(num));
                }
                notifyDataSetChanged();
            }
        });

        //加号的点击事件
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(goodNum.getText().toString());
                num = num + 1;
                datasBean.getGoods().get(childPosition).setGoods_num(num + "");
                goodNum.setText(String.valueOf(num));
                notifyDataSetChanged();
            }
        });

//        商品是否被选中
        if (dataBeans.get(groupPosition).getGoods().get(childPosition).getIsSelect()) {
            select.setImageResource(R.drawable.select);
        } else {
            select.setImageResource(R.drawable.unselect);
        }

        //商品选择框的点击事件
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSelect = goodsBean.getIsSelect();
                goodsBean.setIsSelect(!isSelect);
                if (goodsBean.getIsSelect()) {
                    select.setImageResource(R.drawable.select);
                } else {
                    select.setImageResource(R.drawable.unselect);
                }
                notifyDataSetChanged();
//                if (!isSelect){
//                    select.setImageResource(R.drawable.select);
//                    goodsBean.setIsSelect(true);
//                    return;
//                }
//                select.setImageResource(R.drawable.unselect);
//                goodsBean.setIsSelect(false);
            }
        });

        //价格结算
        allPrice.setText("￥0.00");
        all = 0.0;
        for (int x = 0; x < dataBeans.size(); x++) {
            List<ShoppingCarDataBean.DatasBean.GoodsBean> goods = dataBeans.get(x).getGoods();
            for (int y = 0; y < goods.size(); y++) {
                ShoppingCarDataBean.DatasBean.GoodsBean good = goods.get(y);
                boolean isSelect = good.getIsSelect();
                if (isSelect) {
                    String num = good.getGoods_num();
                    String price = good.getGoods_price();

                    double v = Double.parseDouble(num);
                    double v1 = Double.parseDouble(price);

                    all = all + v * v1;

                    @SuppressLint("DefaultLocale") String finalPrice = String.format("%.2f", all);
                    allPrice.setText("￥" + finalPrice);
                }
            }
        }

        return itemView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
