import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


public class Main {
    static int L,N,Q;
    static Knight[] knights;
    static int[][]field;
    static int[][]knightsField;
    static Deque<Integer> moveKnights;
    static int[][]mv={{-1,0},{0,1},{1,0},{0,-1}};
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st=new StringTokenizer(br.readLine());
        L=Integer.parseInt(st.nextToken());
        N=Integer.parseInt(st.nextToken());
        Q=Integer.parseInt(st.nextToken());

        field=new int[L][L];
        knights=new Knight[N+1];
        knightsField=new int[L][L];

        for(int i=0;i<L;i++){
            st=new StringTokenizer(br.readLine());
            for(int j=0;j<L;j++){
                field[i][j]=Integer.parseInt(st.nextToken());
            }
        }

        for(int i=1;i<=N;i++){
            st=new StringTokenizer(br.readLine());
            int r=Integer.parseInt(st.nextToken())-1;
            int c=Integer.parseInt(st.nextToken())-1;
            int h=Integer.parseInt(st.nextToken());
            int w=Integer.parseInt(st.nextToken());
            int k=Integer.parseInt(st.nextToken());

            for(int x=0;x<h;x++){
                for(int y=0;y<w;y++){
                    knightsField[r+x][c+y]=i;
                }
            }

            knights[i]=new Knight(r,c,h,w,k);
        }

        for(int i=0;i<Q;i++){
            st=new StringTokenizer(br.readLine());
            int num=Integer.parseInt(st.nextToken());
            int d=Integer.parseInt(st.nextToken());

            if(knights[num].hp==0) continue;
            moveKnights=new ArrayDeque<>();
            if(canMove(num,d)){
                move(num,d);
            }
        }

        int result=0;
        for(int i=1;i<=N;i++){
            if(knights[i].hp==0) continue;
            result+=knights[i].k-knights[i].hp;
        }

        System.out.println(result);
    }

    //끝부터
    static void move(int fNum,int direction){
        //끝부터 실행
        while(!moveKnights.isEmpty()){
            int num=moveKnights.pollLast();
            Knight cur=knights[num];

            if(direction==0){
                for(int i=0,size=cur.w;i<size;i++){
                    knightsField[cur.r+cur.h-1][cur.c+i]=0;
                }

                for(int i=0,size=cur.w;i<size;i++){
                    knightsField[cur.r-1][cur.c+i]=num;
                }
                cur.r-=1;
            }
            else if(direction==1){
                for(int i=0,size=cur.h;i<size;i++){
                    knightsField[cur.r+i][cur.c]=0;
                }

                for(int i=0,size=cur.h;i<size;i++){
                    knightsField[cur.r+i][cur.c+cur.w]=num;
                }
                cur.c+=1;
            }
            else if(direction==2){
                for(int i=0,size=cur.w;i<size;i++){
                    knightsField[cur.r][cur.c+i]=0;
                }

                for(int i=0,size=cur.w;i<size;i++){
                    knightsField[cur.r+cur.h][cur.c+i]=num;
                }
                cur.r+=1;
            }
            else if(direction==3){
                for(int i=0,size=cur.h;i<size;i++){
                    knightsField[cur.r+i][cur.c+cur.w-1]=0;
                }

                for(int i=0,size=cur.h;i<size;i++){
                    knightsField[cur.r+i][cur.c-1]=num;
                }
                cur.c-=1;
            }

            //데미지 체크
            if(num==fNum) continue;
            int damage=0;
            for(int i=0,rSize=cur.h;i<rSize;i++){
                for(int j=0,cSize=cur.w;j<cSize;j++){
                    if(field[cur.r+i][cur.c+j]==1){
                        damage++;
                    }
                }
            }

            cur.hp=Math.max(cur.hp-damage,0);

            if(cur.hp==0){
                for(int i=0,rSize=cur.h;i<rSize;i++){
                    for(int j=0,cSize=cur.w;j<cSize;j++){
                        knightsField[cur.r+i][cur.c+j]=0;
                    }
                }
            }
        }

    }

    //bfs
    static boolean canMove(int num,int direction){
        Deque<Integer> curKnights=new ArrayDeque<>();
        curKnights.add(num);

        while(!curKnights.isEmpty()){
            int size=curKnights.size();

            for(int i=0;i<size;i++){
                int cur=curKnights.poll();
                moveKnights.add(cur);

                if(direction==0){
                    int nx=knights[cur].r-1;
                    if(nx<0) return false;

                    Set<Integer> nextKnights=new HashSet<>();

                    for(int j=0,sz=knights[cur].w;j<sz;j++){
                        int ny=knights[cur].c+j;
                        if(field[nx][ny]==2){
                            return false;
                        }
                        else if(knightsField[nx][ny]!=0){
                            nextKnights.add(knightsField[nx][ny]);
                        }
                    }

                    for(int next:nextKnights){
                        curKnights.add(next);
                    }
                }

                else if(direction==1){
                    int ny=knights[cur].c+knights[cur].w;
                    if(ny==L) return false;

                    Set<Integer> nextKnights=new HashSet<>();

                    for(int j=0,sz=knights[cur].h;j<sz;j++){
                        int nx=knights[cur].r+j;
                        if(field[nx][ny]==2){
                            return false;
                        }
                        else if(knightsField[nx][ny]!=0){
                            nextKnights.add(knightsField[nx][ny]);
                        }
                    }

                    for(int next:nextKnights){
                        curKnights.add(next);
                    }
                }

                else if(direction==2){
                    int nx=knights[cur].r+knights[cur].h;
                    if(nx==L) return false;

                    Set<Integer> nextKnights=new HashSet<>();

                    for(int j=0,sz=knights[cur].w;j<sz;j++){
                        int ny=knights[cur].c+j;
                        if(field[nx][ny]==2){
                            return false;
                        }
                        else if(knightsField[nx][ny]!=0){
                            nextKnights.add(knightsField[nx][ny]);
                        }
                    }

                    for(int next:nextKnights){
                        curKnights.add(next);
                    }
                }

                else if(direction==3){
                    int ny=knights[cur].c-1;
                    if(ny<0) return false;

                    Set<Integer> nextKnights=new HashSet<>();

                    for(int j=0,sz=knights[cur].h;j<sz;j++){
                        int nx=knights[cur].r+j;
                        if(field[nx][ny]==2){
                            return false;
                        }
                        else if(knightsField[nx][ny]!=0){
                            nextKnights.add(knightsField[nx][ny]);
                        }
                    }

                    for(int next:nextKnights){
                        curKnights.add(next);
                    }
                }
            }
        }

        return true;
    }

    static class Knight{
        int r,c,h,w,k,hp;

        public Knight(int r,int c,int h,int w,int k){
            this.r=r;
            this.c=c;
            this.h=h;
            this.w=w;
            this.k=k;
            this.hp=k;
        }
    }
}