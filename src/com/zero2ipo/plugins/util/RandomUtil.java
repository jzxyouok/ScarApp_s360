package com.zero2ipo.plugins.util;
/*
我想在{0，1，2，3}中随机产生一个数，产生0的概率为50%，1为20%，2为20%，3为10%，请问怎样的才能高效地实现呢？
*/
import java.util.Random;

public class RandomUtil{
    //统计出现概率的计数变量
    int count_0, count_1, count_2, count_3; // 4种选择结果

    Random r = new Random();    //随机数生成器

    //评估函数: 计算运行不同的次数 , 每种结果的出现概率
    public void calc(int count) {
        int num;
        for(int i=0; i<count; i++) {
            num = r.nextInt(100) + 1;    //让随机数在1~100间产生随机数
            System.out.println("随机数====="+num);
            if(num <= 55) {        //55%
                count_0++;
            } else if(num <= 75) {        //20%
                count_1++;
            } else if(num <= 95) {        //20%
                count_2++;
            } else if(num <= 100) {        //5%
                count_3++;
            }
        }
        System.out.println("运行 " + count + " 次的结果为:");
        System.out.println("0 出现概率: " + ((float)count_0/count*100) + "%");
        System.out.println("1 出现概率: " + ((float)count_1/count*100) + "%");
        System.out.println("2 出现概率: " + ((float)count_2/count*100) + "%");
        System.out.println("3 出现概率: " + ((float)count_3/count*100) + "%");

        //计数器清零
        count_0 = count_1 = count_2 = count_3 = 0;
    }

    public static void main(String[] args) {
        RandomUtil t = new RandomUtil();
        t.calc(100);
        t.calc(200);
        t.calc(500);
        t.calc(1000);
    }
}
