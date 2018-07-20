import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Scalping03 {

    /**
     * 6.20 更新  只刷2个店铺，4个链接
     */
    public static void main(String[] args) {

        // 海参数组大小
        int cucumber_num = 4;

        // 链接价格
        int[] price = new int[cucumber_num];

        // 链接人数
        int[] number = new int[cucumber_num];

        // 佣金
        int[] charge = new int[cucumber_num];

        // 金额
        int[] money = new int[cucumber_num];

        // 链接总价
        int[] links = new int[cucumber_num];

        // 计算单日总价
        int Sum = 0;
        int i = 0;

        // 计算两日总价
        int SumTotal = 0;

        // 计算日期
        Date date01 = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date01);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天
        date01 = calendar.getTime();
        calendar.add(calendar.DATE, 1);//把日期往后增加一天
        Date date02 = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        String dateString01 = formatter.format(date01);
        String dateString02 = formatter.format(date02);
        System.out.println(dateString01 + " 人数 ");

        Scanner sc = new Scanner(System.in);

        //计算第一天
        System.out.print("老刘刺参人数:");
        number[0] = sc.nextInt();
        price[0] = 888;
        number[2] = 0;
        price[2] = 2480;
        if (number[0] <= number[2]) {
            number[2] = number[0];
            number[0] = 0;
        }

        System.out.print("小河蟹/华参堂人数:");
        number[1] = sc.nextInt();
        price[1] = 990;
        //华参堂人数价格
        number[3] = 2;
        price[3] = 249*20;
        if (number[1] <= number[3]) {
            number[3] = number[1];
            number[1] = 0;
        }

        System.out.println("");
        System.out.println("");

        System.out.println(dateString01 + " 概况 ");
        // 老刘刺参
        charge[0] = number[0] / 15 * 25 + 10 * number[0];

        number[0] = number[0] - number[2];
        money[0] = price[0] * number[0];
        money[2] = price[2] * number[2];
        links[0] = money[0] + money[2] + charge[0];

        System.out.println("老刘刺参(" + price[0] + "元) " + number[0] + "人");
        System.out.println("老刘刺参(" + price[2] + "元) " + number[2] + "人");
        System.out.println("===     " + (money[0] + money[2]) + " + " + charge[0] + " = " + links[0]);

        // 小河蟹
        charge[1] = number[1] / 15 * 25 + 10 * number[1];

        number[1] = number[1] - number[3];
        money[1] = price[1] * number[1];
        money[3] = price[3] * number[3];
        links[1] = money[1] + money[3] + charge[1];

        System.out.println("小河蟹(" + price[1] + "元) " + number[1] + "人");
        System.out.println("华参堂(" + price[3] + "元) " + number[3] + "人");
        System.out.println("===     " + (money[1] + money[3]) + " + " + charge[1] + " = " + links[1]);

        for (i = 0, Sum=0; i < 2; i++) {
            Sum = Sum + links[i];
        }
        System.out.println("合计：" + Sum);
        SumTotal = SumTotal + Sum;
        System.out.println("");
        System.out.println("");



        //计算第二天
        System.out.println(dateString02 + " 人数 ");
        System.out.print("老刘刺参人数:");
        number[0] = sc.nextInt();

        if (number[0] <= number[2]) {
            number[2] = number[0];
            number[0] = 0;
        }

        System.out.print("小河蟹/华参堂人数:");
        number[1] = sc.nextInt();

        if (number[1] <= number[3]) {
            number[3] = number[1];
            number[1] = 0;
        }

        System.out.println("");
        System.out.println("");

        System.out.println(dateString02 + " 概况 ");
        // 老刘刺参
        charge[0] = number[0] / 15 * 25 + 10 * number[0];

        number[0] = number[0] - number[2];
        money[0] = price[0] * number[0];
        money[2] = price[2] * number[2];
        links[0] = money[0] + money[2] + charge[0];

        System.out.println("老刘刺参(" + price[0] + "元) " + number[0] + "人");
        System.out.println("老刘刺参(" + price[2] + "元) " + number[2] + "人");
        System.out.println("===     " + (money[0] + money[2]) + " + " + charge[0] + " = " + links[0]);

        // 小河蟹
        charge[1] = number[1] / 15 * 25 + 10 * number[1];

        number[1] = number[1] - number[3];
        money[1] = price[1] * number[1];
        money[3] = price[3] * number[3];
        links[1] = money[1] + money[3] + charge[1];

        System.out.println("小河蟹(" + price[1] + "元) " + number[1] + "人");
        System.out.println("华参堂(" + price[3] + "元) " + number[3] + "人");
        System.out.println("===     " + (money[1] + money[3]) + " + " + charge[1] + " = " + links[1]);

        for (i = 0, Sum=0; i < 2; i++) {
            Sum = Sum + links[i];
        }
        System.out.println("合计：" + Sum);
        SumTotal = SumTotal + Sum;
        System.out.println("");

        System.out.println("共需转账：" + SumTotal);
        System.out.println("");
        System.out.println("");

    }

}
