package subwayMap;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Subway_0 m = new Subway_0();

        Scanner s = new Scanner(System.in);

        switch(s.next()) {
            case "-a":
                System.out.println("���������:");
                String start = s.next();
                System.out.println("�������յ�:");
                String end = s.next();
                System.out.println("���·������:");
                m.shortestPath(start, end);
                //System.out.print("���ɳɹ�");
                break;
            case "-b":
                System.out.println("��������·��");
                String stationname = s.next();
                m.PrintStation(stationname);
                //System.out.print("��ʾ·�߳ɹ�");
                break;
            default:
                System.out.print("��ע�������ʽ");
        }
    }
}
