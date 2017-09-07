package com.primeledger.demo1.trade;

import java.util.ArrayList;

/**
 * Created by jesse on 2017/9/4.
 */
public class LimitTrade extends Trade implements Comparable<LimitTrade>{

    private int price;
    //一个订单可能需要多次撮合才能完成，此数组保存撮合成功的部分交易
   // private ArrayList<MarketTrade> matched = new ArrayList<MarketTrade>();

    private ArrayList<LimitTrade> matched = new ArrayList<LimitTrade>();

    public LimitTrade(int price,int amount, boolean buysell) {
        super(amount, buysell);
        this.price = price;
    }
    //此方法添加撮合成功的交易数据
//    public void match(MarketTrade marketTrade){
//        matched.add(marketTrade);
//    }

    public void match(LimitTrade matchNode){
        matched.add(matchNode);
    }



    public ArrayList<Trade> splitTrade(LimitTrade trade, int amount)
    {
        ArrayList<Trade> ret = super.splitTrade(getPrice(),trade, amount);
        return ret;
    }

    @Override
    public int compareTo(LimitTrade o) {
        if(this.isBuysell()){
            return compareBuy(o);
        }
        else {
            return compareSell(o);
        }
    }

    private int compareBuy(LimitTrade o){
        if(this.price < o.getPrice()){
            return -1;
        }
        else if(this.price > o.getPrice()){
            return 1;
        }
        else {
            return 0;
        }
    }

    private int compareSell(LimitTrade o){
        if(this.price > o.getPrice()){
            return -1;
        }
        else if(this.price < o.getPrice()){
            return 1;
        }
        else {
            return 0;
        }
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
