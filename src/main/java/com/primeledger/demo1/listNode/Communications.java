package com.primeledger.demo1.listNode;

import com.primeledger.demo1.trade.LimitTrade;
import com.primeledger.demo1.trade.MarketTrade;
import com.primeledger.demo1.trade.Trade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jesse on 2017/9/4.
 */
public class Communications {

    private ArrayList<Trade> devTrade = new ArrayList<Trade>();

    public Communications()
    {
        devTrade.add(new LimitTrade(100,2, true));
        devTrade.add(new LimitTrade(180,3, true));
        devTrade.add(new LimitTrade(100,1, true));
        devTrade.add(new LimitTrade(110,1, true));
        devTrade.add(new MarketTrade(4,false));
        devTrade.add(new LimitTrade(20,20, true));
        devTrade.add(new LimitTrade(80,10, true));
    }

    public List<Trade> buildTrades(){
        List<Trade> buyTrades = new ArrayList<Trade>();
        buyTrades.add(new LimitTrade(110,2,true));
        buyTrades.add(new LimitTrade(111,3,false));
        buyTrades.add(new LimitTrade(112,5,true));
        buyTrades.add(new LimitTrade(113,6,false));
        buyTrades.add(new LimitTrade(110,2,true));
        buyTrades.add(new LimitTrade(113,2,true));

        return buyTrades;
    }

    public List<Trade> buildSellTrades(){
        List<Trade> buyTrades = new ArrayList<Trade>();
        buyTrades.add(new LimitTrade(100,2,false));
        buyTrades.add(new LimitTrade(109,1,false));
        buyTrades.add(new LimitTrade(110,3,false));
        buyTrades.add(new LimitTrade(112,2,false));
        buyTrades.add(new LimitTrade(171,2,false));

        return buyTrades;
    }

    public void getBuffer(ArrayList<Trade> ret)
    {
        if (devTrade.size() > 0)
        {
            ret.add(devTrade.get(0));
            devTrade.remove(0);
        }
    }

    public ArrayList<Trade> getDevTrade() {
        return devTrade;
    }

    public void setDevTrade(ArrayList<Trade> devTrade) {
        this.devTrade = devTrade;
    }
}
