package com.jikexueyuan.caradviser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhaijun on 15/9/5.
 */
public class CarExpert {
    List<String> getCars(String type){
        List<String> cars = new ArrayList<>();
        if(type.equals("电动车")){
            cars.add("特斯拉-MODEL S");
            cars.add("比亚迪e6");
        } else if(type.equals("跑车")){
            cars.add("阿斯顿.马丁 V8 Vantage");
            cars.add("保时捷 918");
        } else if(type.equals("SUV")){
            cars.add("奥迪 Q5");
            cars.add("宝马 X6");
        }else if(type.equals("皮卡")){
            cars.add("GMC SIERRA");
            cars.add("Jeep J12");
        }
        return cars;
    }
}
