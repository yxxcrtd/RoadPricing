package com.igoosd.rpss.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 2018/4/18.
 */
@ToString
@Data
public class PrintInfo {

    private int align; // 范围：0,1  0： 向左对齐 1：居中对齐 2：向右对齐

    private int fontSize;// 范围：0-255，  0到3位设定字符高度，4-7位用来设定字符宽度 0,0000,000 设置0-255fanwe

    private  int fontFamily;//范围： 0，1，48，49；0， 48标准字体；1，49压缩字体

    private  String content;

    private int bold; //范围： 0,1 是否加粗 1：选择加粗 0 取消加粗

    private int type; // 0:文本 1:二维码 2条码


    public PrintInfo(){}

    //二维码 构造器
    public PrintInfo(int align,int type,String qrOrBarCode){
        this.align = align;
        this.type = type;
        this.content = qrOrBarCode;
    }

    public PrintInfo(int align, int fontSize,int fontFamily, int bold, String content){
        this.type = 0;
        this.fontSize = fontSize;
        this.bold = bold;
        this.content = content;
        this.align = align;
        this.fontFamily  = fontFamily;
    }


}
