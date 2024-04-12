import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


public class Main {
    static int N,M;
    static int[][]board;
    static int[][]mv={{0,1},{1,0},{0,-1},{-1,0}};
    static int x,y,dir,result;
    static int[]dice={1,3,2,4,5,6};//위,(오른부터 시계),아래
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st=new StringTokenizer(br.readLine());

        N=Integer.parseInt(st.nextToken());
        M=Integer.parseInt(st.nextToken());

        board=new int[N][N];

        for(int i=0;i<N;i++){
            st=new StringTokenizer(br.readLine());
            for(int j=0;j<N;j++){
                board[i][j]=Integer.parseInt(st.nextToken());
            }
        }

        for(int i=0;i<M;i++){
            move();
        }
        System.out.println(result);
    }

    static void move(){
        int nx=x+mv[dir][0];
        int ny=y+mv[dir][1];

        if(nx<0||nx==N||ny<0||ny==N){
            dir=(dir>1)?dir-2:dir+2;

            nx=x+mv[dir][0];
            ny=y+mv[dir][1];
        }

        x=nx;
        y=ny;

        bfs();

        dice();

        if(dice[5]>board[x][y]){
            dir=(++dir==4)?0:dir;
        }
        else if(dice[5]<board[x][y]){
            dir=(--dir==-1)?3:dir;
        }

    }

    static void bfs(){
        boolean[][]visited=new boolean[N][N];
        Deque<int[]> dq=new ArrayDeque<>();
        dq.add(new int[]{x,y});
        visited[x][y]=true;
        int cnt=0;

        while(!dq.isEmpty()){
            int[]cur=dq.poll();
            cnt++;

            for(int[]next:mv){
                int nx=cur[0]+next[0];
                int ny=cur[1]+next[1];

                if(nx<0||nx==N||ny<0||ny==N) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny]!=board[x][y]) continue;

                dq.add(new int[]{nx,ny});
                visited[nx][ny]=true;
            }
        }

        result+=board[x][y]*cnt;
    }

    static void dice(){
        int[]d=new int[6];
        for(int i=0;i<6;i++){
            d[i]=dice[i];
        }

        if(dir==0){//오른
            dice[0]=d[3];
            dice[1]=d[0];
            dice[3]=d[5];
            dice[5]=d[1];
        }
        else if(dir==1){//아래
            dice[0]=d[4];
            dice[2]=d[0];
            dice[4]=d[5];
            dice[5]=d[2];
        }
        else if(dir==2){//왼
            dice[0]=d[1];
            dice[1]=d[5];
            dice[3]=d[0];
            dice[5]=d[3];
        }
        else{//윗
            dice[0]=d[2];
            dice[2]=d[5];
            dice[4]=d[0];
            dice[5]=d[4];
        }
    }
}