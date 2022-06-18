package com.atguigu.bean;

import java.math.BigDecimal;

/**
 * 包名:com.atguigu.bean
 *
 * @author Leevi
 * 日期2021-05-17  09:06
 */
public class CartItem {
    private Integer bookId;
    private String bookName;
    private String imgPath;
    /**
     * 商品的单价
     */
    private Double price;
    private Integer count;
    /**
     * 购物项的金额
     */
    private Double amount;

    public CartItem() {
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", amount=" + amount +
                '}';
    }

    public CartItem(Integer bookId, String bookName, String imgPath, Double price, Integer count, Double amount) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.imgPath = imgPath;
        this.price = price;
        this.count = count;
        this.amount = amount;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 这个方法获取总价:要通过计算才能获取
     * @return
     */
    public Double getAmount() {
        //1. 将price和count封装成BigDecimal类型
        BigDecimal bigDecimalPrice = new BigDecimal(price + "");
        BigDecimal bigDecimalCount = new BigDecimal(count + "");

        //2. 使用bigDecimal的方法进行乘法
        this.amount = bigDecimalCount.multiply(bigDecimalPrice).doubleValue();
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * 数量+1
     */
    public void countIncrease(){
        this.count ++;
    }

    /**
     * 数量-1
     */
    public void countDecrease(){
        this.count --;
    }
}
