package subwayMap;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Subway_0 m = new Subway_0();

        Scanner s = new Scanner(System.in);

        switch(s.next()) {
            case "-a":
                System.out.println("请输入起点:");
                String start = s.next();
                System.out.println("请输入终点:");
                String end = s.next();
                System.out.println("最短路径生成:");
                m.shortestPath(start, end);
                //System.out.print("生成成功");
                break;
            case "-b":
                System.out.println("请输入线路名");
                String stationname = s.next();
                m.PrintStation(stationname);
                //System.out.print("显示路线成功");
                break;
            default:
                System.out.print("请注意命令格式");
        }
    }
}
