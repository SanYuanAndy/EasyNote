package com.sy.easynote.bean;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ASUS User on 2018/5/12.
 */
public class PageData {
    private static String widgetText = "顶顶顶顶顶大大的蛋糕顶顶顶顶顶顶顶强强强强谔谔谔谔荣荣荣荣粤语音乐咩咩咩" +
            "咩斤斤计较急急急铃铃铃令顶顶顶顶草泥马你你你你你你你就看见了解了解离开家" +
            "离开家离开家离开家快进来快进来快进来快进来发动机立刻解放对方的拉开距离开发" +
            "进度了飞机离开阿道夫离开家离开家放大手机看了看" +
            "空间可怜见立刻觉得浪费卡里克" +
            "就离开家离开家两节课立即离开" +
            "六角恐龙" +
            "看敬礼敬礼敬礼敬礼看见了" +
            "尽量靠近垃圾垃圾" +
            "空间链接" +
            "激发了看见了付款记录了几分将会尽快还款了顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶" +
            "顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶" +
            "斤斤计较急急急急急急急急急急急急急急急" +
            "靠靠靠靠靠靠靠靠靠靠靠靠靠靠靠靠靠靠靠" +
            "急急急急急急急急急急急急急急急急急急" +
            "顶顶顶么么么么么啪啪啪啪啪铃铃铃令顶顶顶顶顶" +
            "顶顶顶顶顶顶哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈大手" +
            "大脚发放巨大咩咩咩咩咩咩咩" +
            "噢噢噢噢哦哦哦" +
            "靠靠靠靠靠靠靠" +
            "啦啦啦啦啦啦啦" +
            "了了了了了了";

    private static int currPage = 0;
    private static final int PAGE_SIZE = 100;
    private static boolean isMorePaged = false;

    private static List<EasyNoteHeader> sEasyNoteHeaders = new LinkedList<EasyNoteHeader>();

    private static void init(){
        sEasyNoteHeaders.clear();
        sEasyNoteHeaders.add(new EasyNoteHeader().setTitle("2018年度计划").setEasyContent("1、体重减轻到110"));
        sEasyNoteHeaders.add(new EasyNoteHeader().setTitle("人生修行").setEasyContent("1、更多的耐心"));
        sEasyNoteHeaders.add(new EasyNoteHeader().setTitle("梦想").setEasyContent("1、牛皮"));
        sEasyNoteHeaders.add(new EasyNoteHeader().setTitle("爱情").setEasyContent("1、游戏一场"));
        sEasyNoteHeaders.add(new EasyNoteHeader().setTitle("工作").setEasyContent("1、一场修行"));
        sEasyNoteHeaders.add(new EasyNoteHeader().setTitle("家庭").setEasyContent("1、人生的意义"));
    }

    public  static List<EasyNoteHeader> getEasyNoteHeades(){
        init();
        return sEasyNoteHeaders;
    }

    public static String getCurrPage(){
        String currPageText = widgetText.substring(currPage*PAGE_SIZE);
        return currPageText;
    }

    public static void nextPage(){
        if (currPage*PAGE_SIZE + PAGE_SIZE >= widgetText.length()){
            return;
        }
        currPage++;
    }

    public static void prePage(){
        if (currPage > 0) {
            currPage--;
        }
    }

    public static void morePage(){
        isMorePaged = !isMorePaged;
    }

    public  static boolean isMorePage(){
        return  isMorePaged;
    }


}
