package com.atguigu.bean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 包名:com.atguigu.bean
 * 购物车实体类
 * @author Leevi
 * 日期2021-05-17  09:09
 */
public class Cart {
    private Map<Integer,CartItem> cartItemMap = new HashMap<>();

    /**
     * 将书添加进购物车
     * @param book
     */
    public void addBookToCart(Book book){
        //1. 判断购物车中是否已经有这件商品
        if (cartItemMap.containsKey(book.getBookId())) {
            //2. 如果购物车中已经有这件商品了，那么就对其数量+1
            itemCountIncrease(book.getBookId());
        }else {
            //3. 如果购物车中还没有这件商品，那么我们就新增一个购物项
            //第一次添加商品进购物车，那么数量count肯定是1，那么总价就是单价
            CartItem cartItem = new CartItem(book.getBookId(),book.getBookName(),book.getImgPath(),book.getPrice(),1,book.getPrice());
            cartItemMap.put(cartItem.getBookId(),cartItem);
        }
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartItemMap=" + cartItemMap +
                '}';
    }

    /**
     * 显示购物车信息
     * @return
     */
    public Map<Integer,CartItem> getCartItemMap(){
        return cartItemMap;
    }

    /**
     * 将购物车中的某个购物项的数量+1
     * @param bookId
     */
    public void itemCountIncrease(Integer bookId){
        cartItemMap.get(bookId).countIncrease();
    }

    /**
     * 将购物车中的某个购物项的数量-1
     * @param bookId
     */
    public void itemCountDecrease(Integer bookId){
        CartItem cartItem = cartItemMap.get(bookId);
        //1.先将当前购物项的数量-1
        cartItem.countDecrease();
        //2.判断当前购物项的count是否等于0
        if (cartItem.getCount() == 0) {
            //说明要将当前购物项从购物车中移除
            removeCartItem(bookId);
        }
    }

    /**
     * 从购物车中移除购物项
     * @param bookId
     */
    public void removeCartItem(Integer bookId){
        cartItemMap.remove(bookId);
    }

    /**
     * 修改某个购物项的数量
     * @param bookId
     */
    public void updateItemCount(Integer bookId,Integer newCount){
        cartItemMap.get(bookId).setCount(newCount);
    }

    /**
     * 获取购物车中的购物项的总数
     * @return
     */
    public Integer getTotalCount(){
        //遍历出每一个CartItem的count然后累加
        //方式一: 采用JDK1.8的新特性
        /*AtomicReference<Integer> totalCount = new AtomicReference<>(0);
        cartItemMap.forEach((k,cartItem) -> {
            totalCount.updateAndGet(v -> v + cartItem.getCount());
        });

        return totalCount.get();*/

        //方式二: 使用原生的entrySet遍历Map
        Integer totalCount = 0;
        for (Map.Entry<Integer, CartItem> cartItemEntry : cartItemMap.entrySet()) {
            totalCount += cartItemEntry.getValue().getCount();
        }
        return totalCount;
    }

    /**
     * 获取总金额
     * @return
     */
    public Double getTotalAmount(){
        //解决精度问题的核心: 就是将要进行运算的数据转成BigDecimal类型之后再计算
        //声明一个总金额
        BigDecimal bigDecimalTotalAmount = new BigDecimal("0.0");
        for (Map.Entry<Integer, CartItem> cartItemEntry : cartItemMap.entrySet()) {
            //cartItemEntry.getValue().getAmount()是获取每一个购物项的金额

            //使用总金额加上遍历出来的购物项的金额
            bigDecimalTotalAmount = bigDecimalTotalAmount.add(new BigDecimal(cartItemEntry.getValue().getAmount() + ""));
        }
        return bigDecimalTotalAmount.doubleValue();
    }
}
