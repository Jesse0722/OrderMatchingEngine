package com.primeledger.demo1.listNode;

import com.primeledger.demo1.trade.LimitTrade;
import com.primeledger.demo1.trade.MarketTrade;
import com.primeledger.demo1.trade.Trade;

import java.util.ArrayList;

/**
 * Created by jesse on 2017/9/4.
 */
public class OrderBookManager {
    private static Communications com = new Communications();
    private static ArrayList<Trade> buff = new ArrayList<Trade>();

    public static void main(String[] args) throws InterruptedException {
        OrderBook buybook = new OrderBook(true);
        //OrderBook sellbook = new OrderBook(false);
        boolean t = true;
//        while(t){
//            com.getBuffer(buff);
//
//            for(int i = 0;i<buff.size();i++){
//                if(buff.get(i) instanceof LimitTrade){
//                    buybook.addTrade((LimitTrade)buff.get(i));
//                }
//                else if(buff.get(i) instanceof MarketTrade){
//                    buybook.addTrade((MarketTrade) buff.get(i));
//                }
//                buff.remove(i);
//            }
//            //System.out.println("Buy Book" + buybook.toStringTemp());
//            Thread.sleep(2000);
//        }
    }
}
