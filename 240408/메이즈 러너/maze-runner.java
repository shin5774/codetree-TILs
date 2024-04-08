import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


public class Main {
    static int N,K,M,finishCount;
    static int[][] field;
    static int[][]mv={{1,0},{-1,0},{0,1},{0,-1}};
    static int[]people;
    static int[] moveCount;
    static boolean[] isFinish;
    static int eIdx;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st=new StringTokenizer(br.readLine());
        N=Integer.parseInt(st.nextToken());
        M=Integer.parseInt(st.nextToken());
        K=Integer.parseInt(st.nextToken());

        field=new int[N][N];
        people=new int[M];
        moveCount=new int[M];
        isFinish=new boolean[M];

        for(int i=0;i<N;i++){
            st=new StringTokenizer(br.readLine());
            for(int j=0;j<N;j++){
                field[i][j]=Integer.parseInt(st.nextToken());
            }
        }

        for(int i=0;i<M;i++){
            st=new StringTokenizer(br.readLine());
            int x=Integer.parseInt(st.nextToken())-1;
            int y=Integer.parseInt(st.nextToken())-1;
            people[i]=x*N+y;
        }

        st=new StringTokenizer(br.readLine());
        int ex=Integer.parseInt(st.nextToken())-1;
        int ey=Integer.parseInt(st.nextToken())-1;
        eIdx=ex*N+ey;

        for(int i=0;i<K;i++){
            run();

            if(finishCount==M) break;

            rotate();
        }
        int result=0;

        for(int i=0;i<M;i++){
            result+=moveCount[i];
        }

        System.out.println(result);
        System.out.println(eIdx/N+1+" "+(eIdx%N+1));
    }

    static void run(){
        int ex=eIdx/N;
        int ey=eIdx%N;
        next:
        for(int i=0;i<M;i++){
            if(isFinish[i]) continue;
            //4방 탐색
            int x=people[i]/N;
            int y=people[i]%N;
            int curDist=Math.abs(x-ex)+Math.abs(y-ey);

            for(int[] next:mv){
                int nx=x+next[0];
                int ny=y+next[1];

                if(nx<0||nx==N||ny<0||ny==N) continue;
                if(field[nx][ny]>0) continue;

                int nextDist=Math.abs(nx-ex)+Math.abs(ny-ey);
                if(curDist>nextDist){
                    people[i]=nx*N+ny;
                    moveCount[i]++;

                    if(people[i]==eIdx){
                        isFinish[i]=true;
                        finishCount++;
                    }

                    continue next;
                }
            }
        }
    }

    static void rotate(){
        //4방을 돌면서 사람 찾기
        int ex=eIdx/N;
        int ey=eIdx%N;

        for(int block_size=2;block_size<=N;block_size++){
            next:
            for(int pos=block_size*block_size-1;pos>=0;pos--){
                //범위 벗어나면 다음으로 넘어가야함
                //따라서 한번 다 돌고 체크해야함.
                int bEx=pos/block_size;
                int bEy=pos%block_size;

                //block 좌상단
                int bIx=ex-bEx;
                int bIy=ey-bEy;

                //block 우하단
                int bLx=bIx+(block_size-1);
                int bLy=bIy+(block_size-1);

                //좌상단과 우하단이 범위를 벗어났는가?
                if(oor(bIx)||oor(bIy)||oor(bLx)||oor(bLy)) continue next;


                List<Integer> containPeople=new ArrayList<>();

                for(int pNum=0;pNum<M;pNum++){
                    if(isFinish[pNum]) continue;

                    int px=people[pNum]/N;
                    int py=people[pNum]%N;

                    if(px>=bIx&&px<=bLx&&py>=bIy&&py<=bLy){
                        containPeople.add(pNum);
                    }
                }

                //돌려봐요(출구랑 리스트 사람들이랑)
                if(!containPeople.isEmpty()){
                    //돌린 결과
                    int[][]rotBlock=new int[block_size][block_size];

                    for(int x=0;x<block_size;x++){
                        for(int y=0;y<block_size;y++){
                            if(field[bIx+x][bIy+y]>0) field[bIx+x][bIy+y]--;

                            rotBlock[y][block_size-1-x]=field[bIx+x][bIy+y];
                        }
                    }

                    //다시 넣어주기
                    for(int x=0;x<block_size;x++){
                        for(int y=0;y<block_size;y++){
                            field[bIx+x][bIy+y]=rotBlock[x][y];
                        }
                    }

                    //출구 좌표 변경
                    int eNx=bIx+bEy;
                    int eNy=bIy+(block_size-1-bEx);

                    eIdx=eNx*N+eNy;

                    //있는 좌표 변경
                    for(int pNum:containPeople){
                        int px=people[pNum]/N-bIx;
                        int py=people[pNum]%N-bIy;

                        int pNx=bIx+py;
                        int pNy=bIy+(block_size-1-px);

                        people[pNum]=pNx*N+pNy;
                    }

                    return;
                }
            }
        }
    }

    static boolean oor(int x){
        return x<0||x>=N;
    }
}