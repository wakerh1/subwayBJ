package subwayMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Subway_0 {
	public String[][] str = new String[20][100];
	private class Station{
		String stationName;
		boolean visited;
		
		//以下两个变量用于迪杰斯特拉算法
		int dist;
		Station path;
		String trackName;
		int SetTname=0;
		String pretrackname;

		public Station(String stationName) {
			super();
			this.stationName = stationName;
			this.visited = false;
			this.dist = Integer.MAX_VALUE;//类似于无穷了
			this.path = null;
			this.trackName = null;
			this.pretrackname = null;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((stationName == null) ? 0 : stationName.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Station other = (Station) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (stationName == null) {
				if (other.stationName != null)
					return false;
			} else if (!stationName.equals(other.stationName))
				return false;
			return true;
		}

		public String toString() {
			return "Station [stationName=" + stationName + "]";
		}

		private Subway_0 getOuterType() {
			return Subway_0.this;
		}
	}
	
	private class Edge{
		//String name;
		Station station;
		String lineName;
		int distance;
		
		public Edge(Station station, String lineName, int distance) {
			super();
			this.station = station;
			this.lineName = lineName;
			this.distance = distance;
		}
		
		@Override
		public String toString() {
			return "Edge [station=" + station + ", lineName=" + lineName + ", distance=" + distance + "]";
		}
	}
	
	private Map<Station,List<Edge>> map = new HashMap<>();
	
	/*
	 * 构造函数 
	 * 		直接调用fileReader函数读取文件来初始化
	 */
	public Subway_0(){
		fileReader();
	}
	
	/*
	 * 初始化线路图
	 * 		地铁图是一个有权双向图(看成有权无向图即可)
	 * 		所以两个站节点间存在边时要在两个站节点都记录此边信息
	 */
	private void fileReader(){
		File f = new File("mapdata/subwayInfo_bj.txt");
		if(f.exists()){
			
			try {
				FileReader out = new FileReader(f);
				BufferedReader br = new BufferedReader(out);
				
				String line = null;//用于按行读取文件
				int nums = Integer.parseInt(br.readLine());//记录一共多少条地铁线
				
				for(int i = 0;i < nums;i++){
					br.readLine();//空白行(文件中 每个线路之间有空白行间隔 不需要处理)
					
					String trackInfo = br.readLine();//线路信息行(每个线路第一行记录线路名和站数 空白符分隔)
					String[] track = trackInfo.split("[\\s]");
					String trackName = track[0];//记录当前线路名
					int trackStationsNum = Integer.parseInt(track[1]);//记录当前线路总站数

					str[i][0]=trackName;
					//System.out.println(trackName + "+" +trackStationsNum);
					
					String[] station;//记录当前站信息的数组
					String stationName;//记录当前站的站名
					int nextDistance;//记录当前站到下一站的距离
					
					Station head = null;//记录始发站
					Station preSta = null;//记录上一站
					Edge pre = null;//记录上一站的边节点
					
					Station s;
					
					for (int j = 0; j < trackStationsNum; j++) {
						line = br.readLine();//一行站信息
						
						station = line.split("[\\s]");//获取当前站信息 并拆分
						stationName = station[0];
						nextDistance = Integer.parseInt(station[1]);
						
						s = new Station(stationName);
						s.trackName=trackName;//记录线路名
						str[i][j+1] = stationName;//将站名存入数组中

						//一 将当前站以及上一站的信息补全
						//1 将当前站存储进map(只有map中没有此站时才加入 否则只是完善边信息)
						if(!map.containsKey(s)){
							map.put(s, new ArrayList<Edge>());

						}else if(map.containsKey(s)){
							//****map中有当前站名的key时  要保证之后新建的Edge指向同一个key
							//所有从keyset中拿出这个内部key复制给s 以便后面的使用
							for (Station sta : map.keySet()) {
								if(s.stationName.equals(sta.stationName)){
									s = sta;
//									if(s.trackName==null )
//										s.trackName = trackName;
									break;
								}
							}
							
						}
						
						//2 当前站非始发站时(若是始发站 暂时没有边信息)
						if(pre != null)
							map.get(s).add(pre);
						
						//3 为当前站的上一站  存储到达本站 的边信息(补全上一站的边信息)
						if(preSta != null)
						{
							map.get(preSta).add(new Edge(s,trackName,pre.distance));
							s.pretrackname = preSta.trackName;
						}
						
						//二 为下一站的加入做准备
						//1 将当前站作为下一站的前一站存储起来
						preSta = s;
						//2 新建当前站到下一站的边信息  用pre存储
						pre = new Edge(s,trackName, nextDistance);
						
						//三 处理特殊站节点
						//1 始发站问题
						if(j == 0){
							head = s;
						}
						
						//2 终点站问题(环路则处理 非环路不处理)
						if(j == trackStationsNum-1 && nextDistance > 0){
							//环路要在始发站 存储到达终点站的边信息
							map.get(head).add(pre);
							//对终点站存储始发站的边信息
							map.get(s).add(new Edge(head,trackName,nextDistance));
						}
						
						//System.out.println(stationName + "->" + nextDistance);
					}
					
				}

				br.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	public void PrintStation(String stationname){
		int index = 0;
		int flag = 0;
		//System.out.print(stationname);
		for(int i = 0; i < str.length; i++){ //遍历二维数组，遍历出来的每一个元素是一个一维数组
				if(str[i][0]!=null){
				if(str[i][0].equals(stationname)){
					 index = i;
					 flag = 1;
					 break;
				}

			}
				}
		if (flag == 1 ) {
			for (int j = 1; j < str.length; j++) {
				System.out.println(str[index][j]);
			}
		}
		else if (flag == 0){
			System.out.println("不存在该路线");
			System.exit(0);
		}
	}
	
	public void shortestPath(String s1,String s2){
		
		Station sta1 = new Station(s1);
		Station sta2 = new Station(s2);
		if(!map.containsKey(sta1)){
			System.out.println("不存在的起点");
			System.exit(0);
		}

		if(!map.containsKey(sta2))
		{
			System.out.println("不存在的目的地");
			System.exit(0);
		}
		if(s1.equals(s2))
		{
			System.out.println("起点与目的地重合");
			System.exit(0);
		}
		
		for (Station s : map.keySet()) {
			if(s.stationName.equals(s1))
				sta1 = s;
			if(s.stationName.equals(s2))
				sta2 = s;
		}
		dijkstraTravel(sta1);
		
		shortestPath(sta2);
		
		//System.out.println("\n" + "总距离约为" + sta2.dist);
		
	}
	
	private void shortestPath(Station end){
		
		if(end.path != null){
			shortestPath(end.path);
			//System.out.print(" -> ");
		}
		if(!(end.trackName == null))
			System.out.println("乘"+end.trackName+"  ");
		System.out.println(end.stationName);
	}
	
	/*
	 * 使用迪杰斯特拉算法求最短路径
	 * 		它会未每个站节点分配一个dist和path  表示到起点的距离和路径
	 */
	private void dijkstraTravel(Station s){
		Set<Station> set = map.keySet();

		for (Station station : set) {
			
			station.visited = false;
			station.dist = Integer.MAX_VALUE;
		}
		
		s.dist = 0;//先令起点距离为0
		
		boolean flag = true;//控制循环开始
		while(flag){
			Station v = null;//表示当前站节点
			Station w;//表示当前站节点的邻接节点
			
			//for循环找出站节点中未被访问  且总站数最小的路径
			for (Station station : map.keySet()) {
				if(station.visited == true)
					continue;
				if(v == null || station.dist < v.dist)
					v = station;
			}
			
			//访问当前节点
			v.visited = true;
			
			//遍历当前节点的邻接节点
			List<Edge> list = map.get(v);
			for (int i = 0; i < list.size(); i++) {
				w = list.get(i).station;
				if(!w.visited){
					int d = list.get(i).distance;
					
					//修改邻接节点的距离和路径
					if(v.dist + d < w.dist){
						w.dist = v.dist + d;
						w.path = v;
					}
				}
			}
			
			//遍历节点集  有未遍历的节点则while循环继续
			Iterator<Station> iterator = map.keySet().iterator();
			while(iterator.hasNext()){
				if(!iterator.next().visited){
					flag = true;
					break;
				}
			}
			if(!iterator.hasNext())
				flag = false;
		}
	}
	

	
}
