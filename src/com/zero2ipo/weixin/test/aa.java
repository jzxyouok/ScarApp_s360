package com.zero2ipo.weixin.test;
import java.util.HashMap;
import java.util.Map;
public class aa {
    /**
     * @param args
     */
    public static void main(String[] args) {
        Map<Integer,Double> map = new HashMap<Integer,Double>();
        map.put(0,55555.63);
        map.put(1,3555.22);
        map.put(2,4666.33);
        map.put(3,266.33);
        map.put(4,699.66);
        double k=0;
        for(int i =0;i<map.size();i++)
        {
            if(i==0)
            {
                k=map.get(i);
            }
            else
            {
                if(k>=map.get(i))
                {
                    k=map.get(i);
                }
            }
        }
        System.out.println("map中值最小的:" + k);
    }
}