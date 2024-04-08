import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

//21136KB	248MS
public class Main {
	static int n;
	static int[][] map;
	static int[][] friends;
	static int[] order;
	static int[] dx = {1, 0, -1, 0};
	static int[] dy = {0, 1, 0, -1};
	static int result = 0;
	public static void main(String[] args) throws NumberFormatException, IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		map = new int[n][n];
		friends = new int[n*n+1][4];
		order = new int[n*n];
		for(int i = 0, size = n*n; i<size; i++) {
			StringTokenizer st= new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			order[i] = num;
			for(int j = 0; j<4; j++) {
				friends[num][j] = Integer.parseInt(st.nextToken()); 
			}
		}
		fill();
		count();

		System.out.println(result);
	}
	static void fill() {
		for(int i = 0, size = n * n; i < size; i++) {
			Result result = new Result(0, 0, 0, -1, -1);
			int num = order[i];
			for(int j = 0; j < n; j++) {
				for(int k = 0; k < n; k++) {
					if(map[j][k]==0)
						result = check(result, j, k, num);
				}
			}
			map[result.x][result.y] = num;
		}
	}
	static void count() {
		int count;
		for(int j = 0; j < n; j++) {
			for(int k = 0; k < n; k++) {
				int num = map[j][k];
				count = 0;
				for(int i = 0; i<4; i++) {
					int mx = j + dx[i];
					int my = k + dy[i];
					if(mx < 0 || mx >= n || my < 0 || my >= n) continue;
					for(int t = 0; t<4; t++) {
						if(friends[num][t]==map[mx][my]) {
							count++;
							break;
						}
					}
				}
				if(count==1) {
					result += 1;
				}else if(count==2) {
					result += 10;
				}else if(count==3) {
					result += 100;
				}else if(count==4) {
					result += 1000;
				}
			}
		}
	
	}
	static Result check(Result result, int x, int y, int num) {
		int friend = 0;
		int empty = 0;
		
		for(int i = 0; i<4; i++) {
			int mx = x + dx[i];
			int my = y + dy[i];
			if(mx < 0 || mx >= n || my < 0 || my >= n) continue;
			if(map[mx][my]==0) {
				empty++;
				continue;	
			}
			for(int j = 0; j<4; j++) {
				if(friends[num][j]==map[mx][my]) {
					friend++;
					continue;
				}
			}
			
		}
		if(friend>result.friend)
			return new Result(num, x, y, friend, empty);
		else if(friend==result.friend && empty>result.empty)
			return new Result(num, x, y, friend, empty);
		return result;
	}

	
	static class Result{
		int val;
		int x;
		int y;
		int friend;
		int empty;
		public Result(int val, int x, int y, int friend, int empty) {
			this.val = val;
			this.x = x;
			this.y = y;
			this.friend = friend;
			this.empty = empty;
		}
		
		
	}
}