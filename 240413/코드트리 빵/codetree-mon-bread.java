import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


public class Main {
    static int N,M;
    static int[][]map; //1은 baseCamp,0은 길,-1은 못지나감
    static int[][] goals;
    static int[][] people;
    static int finishCount;
    static int [][]mv={{-1,0},{0,-1},{0,1},{1,0}};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N=Integer.parseInt(st.nextToken());
        M=Integer.parseInt(st.nextToken());
        map=new int[N][N];

        for(int i=0;i<N;i++){
            st=new StringTokenizer(br.readLine());

            for(int j=0;j<N;j++){
                map[i][j]=Integer.parseInt(st.nextToken());
            }
        }

        people=new int[M][2];
        goals=new int[M][2];

        for(int i=0;i<M;i++){
            st=new StringTokenizer(br.readLine());
            goals[i][0]=Integer.parseInt(st.nextToken())-1;
            goals[i][1]=Integer.parseInt(st.nextToken())-1;
        }

        int result=0;
        while(true){

            if(result<M){
                settingPerson(result);
            }

            move(result);

            result++;


            if(finishCount==M){
                break;
            }
            
        }

        System.out.println(result);
    }


    static void settingPerson(int time){
        boolean[][]visited=new boolean[N][N];
        int[] goal=goals[time];

        Deque<int[]> dq=new ArrayDeque<>();
        dq.add(new int[]{goal[0],goal[1]});
        visited[goal[0]][goal[1]]=true;

        while(!dq.isEmpty()){
            int size=dq.size();
            List<Integer> camps=new ArrayList<>();

            for(int i=0;i<size;i++){
                int[]cur=dq.poll();

                for(int[]next:mv){
                    int nx=cur[0]+next[0];
                    int ny=cur[1]+next[1];

                    if(nx<0||nx==N||ny<0||ny==N) continue;

                    if(visited[nx][ny]||map[nx][ny]==-1) continue;

                    if(map[nx][ny]==1){
                        camps.add(nx*N+ny);
                    }
                    else{
                        dq.add(new int[]{nx,ny});
                        visited[nx][ny]=true;

                    }
                }
            }

            if(!camps.isEmpty()){
                Collections.sort(camps);
                int find=camps.get(0);
                people[time][0]=find/N;
                people[time][1]=find%N;

                map[find/N][find%N]=-1;

                return;
            }

        }


    }


    static void move(int time){
        next:
        for(int i=0,size=Math.min(time,M);i<size;i++){
            int[] person=people[i];
            int[] goal=goals[i];
            if(person[0]==goals[i][0]&&person[1]==goals[i][1]){ //이미 골인 한 경우
                continue;
            }

            int[][]visited=new int[N][N];

            Deque<int[]> dq=new ArrayDeque<>();
            dq.add(new int[]{person[0],person[1]});
            visited[person[0]][person[1]]=-1;
            while(!dq.isEmpty()){
                int[]cur=dq.poll();

                for(int[]next:mv){
                    int nx=cur[0]+next[0];
                    int ny=cur[1]+next[1];

                    if(nx<0||nx==N||ny<0||ny==N) continue;
                    if(visited[nx][ny]!=0 || map[nx][ny]==-1) continue;

                    dq.add(new int[]{nx,ny});
                    visited[nx][ny]=cur[0]*N+cur[1];

                    if(nx==goal[0]&&ny==goal[1]){ //발견
                        while(true){
                            int prev=visited[nx][ny];

                            int px=prev/N;
                            int py=prev%N;
                            if(px==person[0]&&py==person[1]){
                                person[0]=nx;
                                person[1]=ny;

                                if(goal[0]==nx&&goal[1]==ny){
                                    finishCount++;
                                    map[nx][ny]=-1;
                                }

                                continue next;
                            }
                            nx=px;
                            ny=py;
                        }

                    }


                }
            }
        }

    }
}