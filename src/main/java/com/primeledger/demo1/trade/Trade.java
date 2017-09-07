package com.primeledger.demo1.trade;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jesse on 2017/9/4.
 */
public class Trade {

    private int amount;
    private String txId;
    private boolean buysell;//buy(1),sell(0)
    private boolean split;//此交易是否被分割

    public Trade(int amount, boolean buysell) {
        this.amount = amount;
        this.buysell = buysell;

    }

    /***
     * 市价撮合
     * 传入交易项，需要撮合的交易金额，返回一个数组，数组第一个元素包含传入的交易
     * 第二个元素是撮合成功的哪部分成交量，两个trade是同一个txId
     */
    public ArrayList<Trade> splitTrade(Trade trade,int amount){
        Trade trade1 = new MarketTrade(amount,trade.isBuysell());
        Trade trade2 = new MarketTrade(trade.getAmount()-amount,trade.isBuysell());
        trade1.setTxId(trade.getTxId());
        trade2.setTxId(trade.getTxId());
        ArrayList<Trade> ret = new ArrayList<Trade>();
        ret.add(trade1);
        ret.add(trade2);

        return ret;
    }

    /***
     * 限价撮合
     *
     *
     */
    public ArrayList<Trade> splitTrade(int price,LimitTrade trade,int amount){
        Trade trade1 = new LimitTrade(price,amount,trade.isBuysell());
        Trade trade2 = new LimitTrade(price,trade.getAmount()-amount,trade.isBuysell());
        trade1.setTxId(trade.getTxId());
        trade2.setTxId(trade.getTxId());
        ArrayList<Trade> ret = new ArrayList<Trade>();
        ret.add(trade1);
        ret.add(trade2);
        return ret;
    }

    private void settxID(int Amount)
    {
        txId = String.valueOf(Amount);

        Random rand = new Random();
        while (txId.length() < 100)
        {
            String buff = String.valueOf(rand.nextInt(9));
            txId = txId + buff;
        }
    }

    private void settxID(String newID)
    {
        txId = newID;
        split = true;

    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public boolean isBuysell() {
        return buysell;
    }

    public void setBuysell(boolean buysell) {
        this.buysell = buysell;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }


}
