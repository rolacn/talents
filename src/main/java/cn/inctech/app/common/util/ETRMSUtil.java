package cn.inctech.app.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ETRMSUtil {

    private static final int NUM = 10; //分页时,每页的个数

    /**
     * 判断是否是邮箱
     *
     * @param str 指定的字符串
     * @return 是否是邮箱:是为true，否则false
     */
    public static Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})$";
        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

    /**
     * 判断是否是手机号
     *
     * @param phone 手机号
     * @return 是否是手机号:是为true，否则false
     */
    public static boolean isPhone(String phone) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 生成sessionId
     *
     * @param length 想生成sessionId的长度
     * @return 生成好的指定长度的sessionId
     */
    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成ID
     *
     * @param type ID标识(S->学生,T->教师,C->企业,P->职位)
     * @return
     */
    public static long generateUID(String type) {
        String str = "";
        String time = String.valueOf(System.currentTimeMillis()).substring(9);
        String salt = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        str = String.valueOf(type.hashCode() + crossMerge(time, salt));
        if (str.charAt(0) == '-') {
            str = str.substring(1);
        }
        if (str.length() == 11) {
            str = str.substring(0, 10);
        }
        return Long.parseLong(str);
    }

    /**
     * 将两个字符串交叉混合
     *
     * @param a 字符串a
     * @param b 字符串b
     * @return 混合后的字符串
     */
    private static String crossMerge(String a, String b) {
        char[] result = new char[a.length() + b.length()];
        if (a.length() > b.length()) {
            for (int i = 0; i < a.length(); i++) {
                result[i * 2] = a.charAt(i);
                if (i < b.length()) {
                    result[i * 2 + 1] = b.charAt(i);
                }
            }
        } else {
            for (int i = 0; i < b.length(); i++) {
                result[i * 2] = b.charAt(i);
                if (i < a.length()) {
                    result[i * 2 + 1] = a.charAt(i);
                }
            }
        }
        return String.valueOf(result);
    }

    /**
     * 生成用SimpleDateFormat格式化过的Date对象
     *
     * @return Date对象
     * @see Date
     * @see SimpleDateFormat
     */
    public static Date generateFormattedDate() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date formatDate = null;
        try {
            formatDate = df.parse(df.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate;
    }

//    public static void main(String[] args) {
//        System.out.println(isPhone("17628663291"));
//    }

    /**
     * 分页实现
     *
     * @param unHandleList 待处理的List
     * @param page         页码
     * @return 分页后List
     */
    public static <T> List<T> splitResultByPage(List<T> unHandleList, int page) {
        List<T> result = new ArrayList<T>();
        if (unHandleList.size() - ((page - 1) * NUM) < NUM) {
            for (int i = 0; i < unHandleList.size(); i++) {
                result.add(unHandleList.get(i));
            }
        } else {
            for (int i = (page - 1) * NUM; i < ((page - 1) * NUM) + NUM; i++) {
                result.add(unHandleList.get(i));
            }
        }
        return result;
    }

    /**
     * 将字符数组转化为Long[]
     *
     * @param strs 字符数组
     * @return
     */
    public static long[] stringArrToLongArr(String[] strs) {
        long[] arr = new long[strs.length];
        int count = 0;
        for (String s : strs) {
            arr[count] = Long.parseLong(s);
            count++;
        }
        return arr;
    }

    public static int getAgeByBirth(String birthday) {
        // 先截取到字符串中的年、月、日
        String strs[] = birthday.trim().split("-");
        int selectYear = Integer.parseInt(strs[0]);
        int selectMonth = Integer.parseInt(strs[1]);
        int selectDay = Integer.parseInt(strs[2]);
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;

        int age = yearMinus;// 先大致赋值
        if (yearMinus < 0) {// 选了未来的年份
            age = 0;
        } else if (yearMinus == 0) {// 同年的，要么为1，要么为0
            if (monthMinus < 0) {// 选了未来的月份
                age = 0;
            } else if (monthMinus == 0) {// 同月份的
                if (dayMinus < 0) {// 选了未来的日期
                    age = 0;
                } else if (dayMinus >= 0) {
                    age = 1;
                }
            } else if (monthMinus > 0) {
                age = 1;
            }
        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                } else if (dayMinus >= 0) {
                    age = age + 1;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        return age;
    }
    
    public static void main(String[] args) {
    	System.out.println(getRandomString(32));
    }
}
