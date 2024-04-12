import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


public class Main {
    static int N,M,H,K;
    static int[][]mv={{1,0},{-1,0},{0,1},{0,-1}};
    static int[] finderDirIdx={1,2,0,3};
    static int[] finderDIrReverseIdx={0,2,1,3};
    static Runner[] runners;

    static boolean[]tree;
    static boolean[][]visited;
    static int finderX,finderY,finderIdx;
    static int finderMoveCount=0;
    static int finderMovesSetCount=0;
    static int finderMoveLimit=1;
    static boolean isReverse;
    static int result;

    static int remainRunner;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st=new StringTokenizer(br.readLine());
        N=Integer.parseInt(st.nextToken());
        M=Integer.parseInt(st.nextToken());
        H=Integer.parseInt(st.nextToken());
        K=Integer.parseInt(st.nextToken());

        runners=new Runner[M];
        tree=new boolean[N*N];
        visited=new boolean[N][N];
        finderX=finderY=N/2;
        remainRunner=M;

        for(int i=0;i<M;i++){
            st=new StringTokenizer(br.readLine());
            int x=Integer.parseInt(st.nextToken())-1;
            int y=Integer.parseInt(st.nextToken())-1;
            int dir=Integer.parseInt(st.nextToken());
            runners[i]=new Runner(x,y,((dir&1)==1)?2:0);
        }

        for(int i=0;i<H;i++){
            st=new StringTokenizer(br.readLine());
            int x=Integer.parseInt(st.nextToken())-1;
            int y=Integer.parseInt(st.nextToken())-1;
            tree[x*N+y]=true;
        }

        for(int i=1;i<=K;i++){
            runnersMove();
            finderMove(i);
            if(remainRunner==0) break;
        }

        System.out.println(result);
    }

    static void runnersMove(){
        for(int i=0;i<M;i++){
            if(!runners[i].alive) continue;
            Runner runner=runners[i];

            if(Math.abs(finderX-runner.x)+Math.abs(finderY-runner.y)>3) continue;

            int nx=runner.x+mv[runner.dir][0];
            int ny=runner.y+mv[runner.dir][1];

            if(nx<0||nx==N||ny<0||ny==N){
                runner.dir=((runner.dir&1)!=0)? runner.dir-1:runner.dir+1;
                nx=runner.x+mv[runner.dir][0];
                ny=runner.y+mv[runner.dir][1];
            }

            if(nx==finderX&&ny==finderY){
                continue;
            }
            runner.x=nx;
            runner.y=ny;

        }
    }

    static void finderMove(int turn){
        int nx=finderX;
        int ny=finderY;

        if(isReverse){
            nx+=mv[finderDIrReverseIdx[finderIdx]][0];
            ny+=mv[finderDIrReverseIdx[finderIdx]][1];

            int px=nx+mv[finderDIrReverseIdx[finderIdx]][0];
            int py=ny+mv[finderDIrReverseIdx[finderIdx]][1];

            if(px<0||px==N||py<0||py==N||visited[px][py]){
                finderIdx = (++finderIdx == 4) ? 0 : finderIdx;
            }
            visited[nx][ny]=true;
        }
        else{
            nx+=mv[finderDirIdx[finderIdx]][0];
            ny+=mv[finderDirIdx[finderIdx]][1];
            if(++finderMoveCount==finderMoveLimit){
                finderMoveCount=0;
                if(++finderMovesSetCount==2) {
                    finderMoveLimit++;
                    finderMovesSetCount = 0;
                }
                finderIdx = (++finderIdx == 4) ? 0 : finderIdx;

            }
        }

        finderX=nx;
        finderY=ny;

        if(finderX==0&&finderY==0){
            isReverse=true;
            finderIdx=0;
            visited[0][0]=true;
        }
        else if(finderX==N/2&&finderY==N/2){
            isReverse=false;
            finderIdx=0;
            finderMoveCount=0;
            finderMoveLimit=1;
            finderMovesSetCount=0;
            visited=new boolean[N][N];
        }

        //술래 찾기
        int fx=finderX;
        int fy=finderY;

        int cnt=0;
        do{
            if(fx<0||fx>=N||fy<0||fy>=N) break;

            if(!tree[fx*N+fy]){
                for(Runner runner:runners){
                    if(!runner.alive) continue;
                    if(runner.x==fx&&runner.y==fy){
                        runner.alive=false;
                        result+=turn;
                        remainRunner++;
                    }
                }
            }


            if(isReverse){
                fx+=mv[finderDIrReverseIdx[finderIdx]][0];
                fy+=mv[finderDIrReverseIdx[finderIdx]][1];
            }
            else{
                fx+=mv[finderDirIdx[finderIdx]][0];
                fy+=mv[finderDirIdx[finderIdx]][1];
            }
        }while(++cnt<3);

    }

    static class Runner{
        int x,y;
        int dir;
        boolean alive;

        public Runner(int x,int y,int dir){
            this.x=x;
            this.y=y;
            this.dir=dir;
            alive=true;
        }

        @Override
        public String toString() {
            return "x: "+x+" y:"+y+" dir:"+dir+" alive:"+alive;
        }
    }

}