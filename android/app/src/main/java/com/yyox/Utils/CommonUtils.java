package com.yyox.Utils;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dadaniu on 2017-01-17.
 */

public class CommonUtils {

    public static String getBaseUrl(){
        /*SharedPreferences apiSharedPreferences = WEApplication.getContext().getSharedPreferences("api", Context.MODE_PRIVATE);
        int withinApi = apiSharedPreferences.getInt("withinApi", 0);
        if (withinApi == 1){
            return  "http://10.0.0.19:8080/";
        }else if (withinApi == 2){
            return "http://app.yyox.com/";
//            return "http://52.69.220.34:80";
//            return "http://52.199.12.84:80/";
        }else if (withinApi == 3){
            return "http://6d089bc4.ngrok.4kb.cn/";
        }else if (withinApi == 4){
            return "http://192.168.4.224:8080/";
        }
//        return "http://10.0.0.19:8080/";*/
        return "http://app.yyox.com/";
    }

    /**
     * 汉字验证
     * @param chinese 字符串
     * @return boolean
     */
    public static boolean isChinese(String chinese){
        String regex = "[\\u4e00-\\u9fa5]{2,10}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(chinese);
        return m.matches();
    }

    public static boolean isChineseValue(String chinese){
        String regex = "[\\u4e00-\\u9fa5]{1,50}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(chinese);
        return m.matches();
    }

    /**
     * 整数或小数点两位
     */
    public static boolean isChineseNumber(String chinese){
        String regex = "^[0-9]{1,6}+(\\.[0-9]{1,2}+)?$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(chinese);
        return m.matches();
    }
    /**
     * 非中文
     * @param chinese
     * @return
     */
    public static boolean isChinesepackage(String chinese){
        String regex = "([a-zA-Z0-9]{0,200})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(chinese);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }

    public static boolean isZipCode(String zipcode) {
        String regex = "^[0-9]{6}$";
        return Pattern.compile(regex).matcher(zipcode).matches();
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhoneNumber(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 验证密码
     * @param password 用户名
     * @return boolean
     */
    public static boolean checkPassword(String password){
        //String regex = "([a-zA-Z0-9]{6,12})";//6~12数字或字母
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static String doubleFormat(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }

    public static String doubleFormat(String value) {
        BigDecimal bd = new BigDecimal(Double.parseDouble(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }

    public static String currencySymbol(String currency) {
        String symbol = "CNY";
        switch (currency){
            case "CNY":    symbol = "¥";    break;
            case "USD":    symbol = "$US";    break;
            case "EUR":    symbol = "€";    break;
            case "JPY":    symbol = "￥";    break;
            case "AUD":    symbol = "$A";    break;
            case "KRW":    symbol = "₩";    break;
            case "NZD":    symbol = "$N";    break;
            case "CAD":    symbol = "C$";    break;
        }
        return symbol;
    }

}
