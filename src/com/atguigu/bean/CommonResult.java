package com.atguigu.bean;

/**
 * 封装响应结果的一个Javabean
 * 封装一个javabean用来存储服务器端要相应给客户端的数据
 * 把这个类的对象转换成json是这样的：
 *
 */
public class CommonResult {

    /**
     * 服务器端处理请求的标示
     */
    private boolean flag;

    /**
     * 当服务器端处理请求成功的时候要显示给客户端的数据
     */
    private Object resultData;
    /**
     * 当服务器端处理请求失败的时候要响应给客户端的错误信息
     */
    private String message;

    /**
     * 处理请求成功
     * @return
     */
    public static CommonResult ok(){
        return new CommonResult().setFlag(true);
    }

    /**
     * 处理请求失败
     * @return
     */
    public static CommonResult error(){
        return new CommonResult().setFlag(false);
    }

    public boolean isFlag() {
        return flag;
    }

    private CommonResult setFlag(boolean flag) {
        this.flag = flag;
        return this;
    }

    public Object getResultData() {
        return resultData;
    }

    public CommonResult setResultData(Object resultData) {
        this.resultData = resultData;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommonResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public static void main(String[] args) {
        //如果处理请求成功
        /*CommonResult commonResult = CommonResult.ok().setResultData("成功时候的数据");
        System.out.println(commonResult);*/

        //如果处理请求失败
        System.out.println(CommonResult.error().setMessage("失败时候的信息..."));
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "flag=" + flag +
                ", resultData=" + resultData +
                ", message='" + message + '\'' +
                '}';
    }
}
