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
		
		//���������������ڵϽ�˹�����㷨
		int dist;
		Station path;
		String trackName;
		int SetTname=0;
		String pretrackname;

		public Station(String stationName) {
			super();
			this.stationName = stationName;
			this.visited = false;
			this.dist = Integer.MAX_VALUE;//������������
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
	 * ���캯�� 
	 * 		ֱ�ӵ���fileReader������ȡ�ļ�����ʼ��
	 */
	public Subway_0(){
		fileReader();
	}
	
	/*
	 * ��ʼ����·ͼ
	 * 		����ͼ��һ����Ȩ˫��ͼ(������Ȩ����ͼ����)
	 * 		��������վ�ڵ����ڱ�ʱҪ������վ�ڵ㶼��¼�˱���Ϣ
	 */
	private void fileReader(){
		File f = new File("mapdata/subwayInfo_bj.txt");
		if(f.exists()){
			
			try {
				FileReader out = new FileReader(f);
				BufferedReader br = new BufferedReader(out);
				
				String line = null;//���ڰ��ж�ȡ�ļ�
				int nums = Integer.parseInt(br.readLine());//��¼һ��������������
				
				for(int i = 0;i < nums;i++){
					br.readLine();//�հ���(�ļ��� ÿ����·֮���пհ��м�� ����Ҫ����)
					
					String trackInfo = br.readLine();//��·��Ϣ��(ÿ����·��һ�м�¼��·����վ�� �հ׷��ָ�)
					String[] track = trackInfo.split("[\\s]");
					String trackName = track[0];//��¼��ǰ��·��
					int trackStationsNum = Integer.parseInt(track[1]);//��¼��ǰ��·��վ��

					str[i][0]=trackName;
					//System.out.println(trackName + "+" +trackStationsNum);
					
					String[] station;//��¼��ǰվ��Ϣ������
					String stationName;//��¼��ǰվ��վ��
					int nextDistance;//��¼��ǰվ����һվ�ľ���
					
					Station head = null;//��¼ʼ��վ
					Station preSta = null;//��¼��һվ
					Edge pre = null;//��¼��һվ�ı߽ڵ�
					
					Station s;
					
					for (int j = 0; j < trackStationsNum; j++) {
						line = br.readLine();//һ��վ��Ϣ
						
						station = line.split("[\\s]");//��ȡ��ǰվ��Ϣ �����
						stationName = station[0];
						nextDistance = Integer.parseInt(station[1]);
						
						s = new Station(stationName);
						s.trackName=trackName;//��¼��·��
						str[i][j+1] = stationName;//��վ������������

						//һ ����ǰվ�Լ���һվ����Ϣ��ȫ
						//1 ����ǰվ�洢��map(ֻ��map��û�д�վʱ�ż��� ����ֻ�����Ʊ���Ϣ)
						if(!map.containsKey(s)){
							map.put(s, new ArrayList<Edge>());

						}else if(map.containsKey(s)){
							//****map���е�ǰվ����keyʱ  Ҫ��֤֮���½���Edgeָ��ͬһ��key
							//���д�keyset���ó�����ڲ�key���Ƹ�s �Ա�����ʹ��
							for (Station sta : map.keySet()) {
								if(s.stationName.equals(sta.stationName)){
									s = sta;
//									if(s.trackName==null )
//										s.trackName = trackName;
									break;
								}
							}
							
						}
						
						//2 ��ǰվ��ʼ��վʱ(����ʼ��վ ��ʱû�б���Ϣ)
						if(pre != null)
							map.get(s).add(pre);
						
						//3 Ϊ��ǰվ����һվ  �洢���ﱾվ �ı���Ϣ(��ȫ��һվ�ı���Ϣ)
						if(preSta != null)
						{
							map.get(preSta).add(new Edge(s,trackName,pre.distance));
							s.pretrackname = preSta.trackName;
						}
						
						//�� Ϊ��һվ�ļ�����׼��
						//1 ����ǰվ��Ϊ��һվ��ǰһվ�洢����
						preSta = s;
						//2 �½���ǰվ����һվ�ı���Ϣ  ��pre�洢
						pre = new Edge(s,trackName, nextDistance);
						
						//�� ��������վ�ڵ�
						//1 ʼ��վ����
						if(j == 0){
							head = s;
						}
						
						//2 �յ�վ����(��·���� �ǻ�·������)
						if(j == trackStationsNum-1 && nextDistance > 0){
							//��·Ҫ��ʼ��վ �洢�����յ�վ�ı���Ϣ
							map.get(head).add(pre);
							//���յ�վ�洢ʼ��վ�ı���Ϣ
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
		for(int i = 0; i < str.length; i++){ //������ά���飬����������ÿһ��Ԫ����һ��һά����
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
			System.out.println("�����ڸ�·��");
			System.exit(0);
		}
	}
	
	public void shortestPath(String s1,String s2){
		
		Station sta1 = new Station(s1);
		Station sta2 = new Station(s2);
		if(!map.containsKey(sta1)){
			System.out.println("�����ڵ����");
			System.exit(0);
		}

		if(!map.containsKey(sta2))
		{
			System.out.println("�����ڵ�Ŀ�ĵ�");
			System.exit(0);
		}
		if(s1.equals(s2))
		{
			System.out.println("�����Ŀ�ĵ��غ�");
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
		
		//System.out.println("\n" + "�ܾ���ԼΪ" + sta2.dist);
		
	}
	
	private void shortestPath(Station end){
		
		if(end.path != null){
			shortestPath(end.path);
			//System.out.print(" -> ");
		}
		if(!(end.trackName == null))
			System.out.println("��"+end.trackName+"  ");
		System.out.println(end.stationName);
	}
	
	/*
	 * ʹ�õϽ�˹�����㷨�����·��
	 * 		����δÿ��վ�ڵ����һ��dist��path  ��ʾ�����ľ����·��
	 */
	private void dijkstraTravel(Station s){
		Set<Station> set = map.keySet();

		for (Station station : set) {
			
			station.visited = false;
			station.dist = Integer.MAX_VALUE;
		}
		
		s.dist = 0;//����������Ϊ0
		
		boolean flag = true;//����ѭ����ʼ
		while(flag){
			Station v = null;//��ʾ��ǰվ�ڵ�
			Station w;//��ʾ��ǰվ�ڵ���ڽӽڵ�
			
			//forѭ���ҳ�վ�ڵ���δ������  ����վ����С��·��
			for (Station station : map.keySet()) {
				if(station.visited == true)
					continue;
				if(v == null || station.dist < v.dist)
					v = station;
			}
			
			//���ʵ�ǰ�ڵ�
			v.visited = true;
			
			//������ǰ�ڵ���ڽӽڵ�
			List<Edge> list = map.get(v);
			for (int i = 0; i < list.size(); i++) {
				w = list.get(i).station;
				if(!w.visited){
					int d = list.get(i).distance;
					
					//�޸��ڽӽڵ�ľ����·��
					if(v.dist + d < w.dist){
						w.dist = v.dist + d;
						w.path = v;
					}
				}
			}
			
			//�����ڵ㼯  ��δ�����Ľڵ���whileѭ������
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
