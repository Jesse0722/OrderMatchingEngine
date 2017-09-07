package com.primeledger.demo1.listNode;

import com.primeledger.demo1.trade.LimitTrade;
import com.primeledger.demo1.trade.MarketTrade;
import com.primeledger.demo1.trade.Trade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by jesse on 2017/9/4.
 */
public class OrderBook {

    //委托买入队列，按照价格从低到高进行排列，相同价格时间安装从最近到久进行排序
    //（12.0,14:20）->(12.0,14:10)->(12.1,14:21)->(12.2,14:19)
    private LinkedList<LimitTrade> orderBuyList = new LinkedList<LimitTrade>();
    //委托卖出队列，按照价格从高到低进行排列，相同价格时间安装从最近到久进行排序
    //（13.0,14:20）->(12.0,14:10)->(11.9,14:21)->(11.9,14:19)
    private LinkedList<LimitTrade> orderSellList = new LinkedList<LimitTrade>();

    //撮合成功的队列，成交的部分
    private ArrayList<LimitTrade> matchedTrades = new ArrayList<LimitTrade>();
    private boolean buysell;//队列类型，买入或卖出

    public OrderBook(boolean bs){
        this.buysell = bs;
    }

    public OrderBook() {
    }

    public void addTrade(LimitTrade newTrade) {
        if(newTrade.isBuysell()){
            matchSell(newTrade);
        }
        else{
            matchBuy(newTrade);
        }

    }


//    public void addTrade(LimitTrade newTrade){
//        ListIterator<LimitTrade> itr = orderList.listIterator();
//        if( orderList.size() == 0) {orderList.add(newTrade);}//如果委托队列为空，则直接入队
//        else {
//            //否则需要按照Price/Time构造委托队列
//            //这里省去时间维度，以入队的顺序模拟委托的时间先后。
//            while(itr.hasNext())
//            {
//                LimitTrade nxt = itr.next();
//                if(nxt.getPrice() >= newTrade.getPrice())//如果当前节点价格大于等于委托价，则插入到当前节点前面
//                {
//                    itr.add(newTrade);
//                    break;
//                }
//                //如果当前节点价格小于委托价格，则需要将委托价格插入到相应的价格位置
//                if(!itr.hasNext())//如果下个节点为空
//                {
//                    itr.add(newTrade);//则插入到最后节点
//                    break;
//                }
//            }
//        }
//    }


    private void matchSell(LimitTrade trade) {
//        LinkedList<LimitTrade> orderBook;
//        orderBook = trade.isBuysell()?orderBuyList:orderSellList;
        //LimitTrade firstNode = orderBook.peekLast(); //队列的队首元素
        ArrayList<Trade> split;
        if(orderSellList.size()==0||orderSellList.peekLast().getPrice()>trade.getPrice()){
            insertBuyOrderBook(trade);
        }
        else{//如果买入价大于等于卖一价，则直接撮合
            LimitTrade firstNode = orderSellList.peekLast(); //队列的队首元素
            if(trade.getAmount()>= firstNode.getAmount()) {//如果买入量大于等于卖一第一个节点的量
                //分割买入委托
                split = trade.splitTrade(trade,firstNode.getAmount());
                matchedTrades.add(0,firstNode);//记录撮合成功的部分
                orderSellList.pollLast();//删除最后一个节点
                trade.match((LimitTrade) split.get(0));//记录新交易撮合成功的部分
                addTrade((LimitTrade) split.get(1));//继续撮合剩余部分
            }
            else{//如果，买入委托量小于等于卖一第一个节点的量
                //分割第一个节点
                split = firstNode.splitTrade(firstNode,trade.getAmount());
                //分割的部分成交
                matchedTrades.add(0,trade);//记录成分部分
                orderSellList.pollLast();//
                orderSellList.addLast((LimitTrade) split.get(1));//将剩余部分挂到卖一
                firstNode.match((LimitTrade) split.get(0));//记录卖一节点撮合成功的部分
            }
        }

    }

    private void matchBuy(LimitTrade trade) {
//        LinkedList<LimitTrade> orderBook;
//        orderBook = trade.isBuysell()?orderBuyList:orderSellList;
        //LimitTrade firstNode = orderBook.peekLast(); //队列的队首元素
        ArrayList<Trade> split;
        if(orderBuyList==null||orderBuyList.peekLast().getPrice()<trade.getPrice()){
            insertSellOrderBook(trade);
        }
        else{//如果卖出价小于等于卖一价，则直接撮合
            LimitTrade firstNode = orderSellList.peekLast(); //队列的队首元素
            if(trade.getAmount()>= firstNode.getAmount()) {//如果卖出量大于等于买一第一个节点的量
                //分割卖出委托
                split = trade.splitTrade(trade,firstNode.getAmount());
                matchedTrades.add(0,firstNode);//记录撮合成功的部分
                orderSellList.pollLast();//删除最后一个节点
                trade.match((LimitTrade) split.get(0));//记录新交易撮合成功的部分
                addTrade((LimitTrade) split.get(1));//继续撮合剩余部分
            }
            else{//如果，买入委托量小于等于卖一第一个节点的量
                //分割第一个节点
                split = firstNode.splitTrade(firstNode,trade.getAmount());
                //分割的部分成交
                matchedTrades.add(0,trade);//记录成分部分
                orderSellList.pollLast();//
                orderSellList.addLast((LimitTrade) split.get(1));//将剩余部分挂到卖一
                firstNode.match((LimitTrade) split.get(0));//记录卖一节点撮合成功的部分
            }
        }

    }

    public void insertBuyOrderBook(LimitTrade newTrade){
        ListIterator<LimitTrade> itr = orderBuyList.listIterator();
        if( orderBuyList.size() == 0) {orderBuyList.add(newTrade);}//如果委托队列为空，则直接入队
        else {
            //否则需要按照Price/Time构造委托队列
            //这里省去时间维度，以入队的顺序模拟委托的时间先后。
            while(itr.hasNext())
            {
                LimitTrade nxt = itr.next();
                if(nxt.getPrice() >= newTrade.getPrice())//如果当前节点价格大于等于委托价，则插入到当前节点前面
                {
                    itr.add(newTrade);
                    break;
                }
                //如果当前节点价格小于委托价格，则需要将委托价格插入到相应的价格位置
                if(!itr.hasNext())//如果下个节点为空
                {
                    itr.add(newTrade);//则插入到最后节点
                    break;
                }
            }
        }
    }

    public void insertSellOrderBook(LimitTrade newTrade){
        ListIterator<LimitTrade> itr = orderSellList.listIterator();
        if( orderSellList.size() == 0) {orderSellList.add(newTrade);}//如果委托队列为空，则直接入队
        else {
            //否则需要按照Price/Time构造委托队列
            //这里省去时间维度，以入队的顺序模拟委托的时间先后。
            while(itr.hasNext())
            {
                LimitTrade nxt = itr.next();
                if(nxt.getPrice() <= newTrade.getPrice())//如果当前节点价格小于等于委托价，则插入到当前节点前面
                {
                    itr.add(newTrade);
                    break;
                }
                //如果当前节点价格大于委托价格，则需要将委托价格插入到相应的价格位置
                if(!itr.hasNext())//如果下个节点为空
                {
                    itr.add(newTrade);//则插入到最后节点
                    break;
                }
            }
        }
    }

    /**
     * 市价委托
     * @param marketTrade
     */
    public void addTrade(MarketTrade marketTrade)
    {
        int tradeSize = marketTrade.getAmount(); //获取委托数量
        ArrayList<Trade> split;
//        if (tradeSize > orderList.get(0).getAmount())	// 如果市价委托数量大于买一/买一的数量
//        {
//            split = marketTrade.splitTrade(marketTrade, orderList.get(0).getAmount());//从市价委托量分割买一/卖一委托量进行撮合
//            matchedTrades.add(0,orderList.get(0));//记录撮合成功的交易
//            orderList.remove(0);//将成交部分的委托从队列中删除
//            matchedTrades.get(0).match((MarketTrade)split.get(0));//记录限价委托的成交部分
//            addTrade((MarketTrade)split.get(1));//剩余未成交的市价委托部分，递归调用
//        }
//        else if (tradeSize < orderList.get(0).getAmount())	//如果市价委托量小于买一/卖一的数量
//        {
//            split = orderList.get(0).splitTrade(orderList.get(0), tradeSize);//从买一/卖一分割市价相应委托量进行撮合
//            matchedTrades.add(0,(LimitTrade)split.get(0));//记录撮合成功的交易
//            orderList.remove(0);//将成交部分的委托从队列中删除
//            orderList.add((LimitTrade)split.get(1));//将市价委托剩余部分挂为买一/卖一
//            matchedTrades.get(0).match(marketTrade);//
//
//        }
//        else	// Market Order matches the size of the best bid/ask
//        {
//            matchedTrades.add(0,orderList.get(0));
//            orderList.remove(0);
//            matchedTrades.get(0).match(marketTrade);//记录限价委托的成交部分
//        }
    }


    public boolean isBuysell() {
        return buysell;
    }


    public String toStringBuyOrderBook()
    {
        String ret = "";

        for( int i = 0; i < orderBuyList.size(); i++)
        {
            ret = ret + "[Price = " + orderBuyList.get(i).getPrice() + ", Amount = " + orderBuyList.get(i).getAmount() + "]";
        }

        return ret;
    }

    public String toStringSellOrderBook()
    {
        String ret = "";

        for( int i = 0; i < orderSellList.size(); i++)
        {
            ret = ret + "[Price = " + orderSellList.get(i).getPrice() + ", Amount = " + orderSellList.get(i).getAmount() + "]";
        }

        return ret;
    }

    public String toStringMatchedTradeList()
    {
        String ret = "";

        for( int i = 0; i < matchedTrades.size(); i++)
        {
            ret = ret + "[Price = " + matchedTrades.get(i).getPrice() + ", Amount = " + matchedTrades.get(i).getAmount() + "]";
        }

        return ret;
    }

    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        //OrderBook sellOrderBook = new OrderBook(false);
        //OrderBook buyOrderBook = new OrderBook(true);
        List<Trade> trades = new Communications().buildTrades();

        for(Trade trade:trades){

            if(trade instanceof LimitTrade) {
                System.out.println(" NewTrade[price = "+((LimitTrade) trade).getPrice()+",amount = "+trade.getAmount()+",type:"+trade.isBuysell()+"]");
//                //判断交易类型
//                if(trade.isBuysell()){   //如果是buy，去sellOrder撮合
//                    sellOrderBook.addTrade((LimitTrade) trade);
//                }else {
//                    buyOrderBook.addTrade((LimitTrade) trade);
//                }
//
//                //如果是sell，去buyOrder撮合

                orderBook.addTrade((LimitTrade) trade);
            }
            else if(trade instanceof MarketTrade){
                orderBook.addTrade((MarketTrade) trade);
            }
            System.out.println("[OrderBook]");
            System.out.println(" Buy Book" + orderBook.toStringBuyOrderBook());
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("Sell Book" + orderBook.toStringSellOrderBook());
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("Matched Book" + orderBook.toStringMatchedTradeList());
            System.out.println();
        }

    }



}
