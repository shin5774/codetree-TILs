import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


public class Main {
    static int N,M,K;
    static Map<Integer,int[]> tower;
    static int[][]mv={{0,1},{1,0},{0,-1},{-1,0}};
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st=new StringTokenizer(br.readLine());
        N=Integer.parseInt(st.nextToken());
        M=Integer.parseInt(st.nextToken());
        K=Integer.parseInt(st.nextToken());
        tower=new HashMap<>(N*M);

        for(int i=0;i<N;i++){
            st=new StringTokenizer(br.readLine());
            for(int j=0;j<M;j++){
                int cur=Integer.parseInt(st.nextToken());

                if(cur!=0){
                    tower.put(i*M+j,new int[]{cur,0});
                }
            }
        }

        end:
        for(int i=1;i<=K;i++){
            Set<Integer> containIndex=new HashSet<>();

            //공격자 선정
            int attacker=selectAttacker();
            int[] attackerStat=tower.get(attacker);
            attackerStat[1]=i;
            containIndex.add(attacker);

            //공격 대상 선정
            int target=selectTarget();
            containIndex.add(target);

            attackerStat[0]+=N+M;

            //공격 수단 선택
            List<Integer> path=checkAttackTool(attacker,target);

            //공격
            int halfAttack=tower.get(attacker)[0]/2;

            //포탄
            if(path==null){
                int tx=target/M;
                int ty=target%M;

                for(int mx=-1;mx<=1;mx++){
                    for(int my=-1;my<=1;my++){
                        if(mx==0&&my==0) continue;

                        int nx=tx+mx;
                        int ny=ty+my;

                        if(nx<0){
                            nx=N-1;
                        }
                        else if(nx==N){
                            nx=0;
                        }

                        if(ny<0){
                            ny=M-1;
                        }
                        else if(ny==M){
                            ny=0;
                        }

                        int nextIdx=nx*M+ny;

                        if(!tower.containsKey(nextIdx)) continue;
                        if(nextIdx==attacker) continue;

                        containIndex.add(nextIdx);
                        int[] twStat=tower.get(nextIdx);
                        twStat[0]=Math.max(0,twStat[0]-halfAttack);
                        if(twStat[0]==0) {
                            tower.remove(nextIdx);
                            if(tower.keySet().size()==1) break end;
                        }
                    }
                }
            }
            else{//레이저
                for(int pIdx:path){
                    containIndex.add(pIdx);
                    int[] twStat=tower.get(pIdx);
                    twStat[0]=Math.max(0,twStat[0]-halfAttack);
                    if(twStat[0]==0){
                        tower.remove(pIdx);
                        if(tower.keySet().size()==1) break end;
                    }
                }
            }

            int[] targetStat=tower.get(target);
            targetStat[0]=Math.max(0,targetStat[0]-tower.get(attacker)[0]);

            if(targetStat[0]==0){
                tower.remove(target);
            }

            //한개면 break;
            if(tower.keySet().size()==1) break;
            //정비
            for(int tIdx:tower.keySet()){
                if(!containIndex.contains(tIdx)){
                    tower.get(tIdx)[0]++;
                }
            }
        }


        //가장 강한 포탑 찾기
        System.out.println(tower.get(selectTarget())[0]);
    }

    static int selectAttacker(){
        List<Integer> curTowers=new ArrayList<>(tower.keySet());

        Collections.sort(curTowers,(t1,t2)->{
           if(tower.get(t1)[0]==tower.get(t2)[0]){
               if(tower.get(t1)[1]==tower.get(t2)[1]){
                   int t1Sum=tower.get(t1)[0]/M+tower.get(t1)[0]%M;
                   int t2Sum=tower.get(t2)[0]/M+tower.get(t2)[0]%M;

                   if(t1Sum==t2Sum){
                       return tower.get(t2)[0]%M-tower.get(t1)[0]%M;
                   }

                   return t2Sum-t1Sum;
               }

               return tower.get(t2)[1]-tower.get(t1)[1];
           }

           return tower.get(t1)[0]-tower.get(t2)[0];
        });

        return curTowers.get(0);
    }

    static int selectTarget(){
        List<Integer> curTowers=new ArrayList<>(tower.keySet());

        Collections.sort(curTowers,(t1,t2)->{
            if(tower.get(t1)[0]==tower.get(t2)[0]){
                if(tower.get(t1)[1]==tower.get(t2)[1]){
                    int t1Sum=tower.get(t1)[0]/M+tower.get(t1)[0]%M;
                    int t2Sum=tower.get(t2)[0]/M+tower.get(t2)[0]%M;

                    if(t1Sum==t2Sum){
                        return tower.get(t1)[0]%M-tower.get(t2)[0]%M;
                    }

                    return t1Sum-t2Sum;
                }

                return tower.get(t1)[1]-tower.get(t2)[1];
            }

            return tower.get(t2)[0]-tower.get(t1)[0];
        });

        return curTowers.get(0);
    }


    static List<Integer> checkAttackTool(int attacker,int target){
        Map<Integer,List<Integer>> paths=new HashMap<>();
        paths.put(attacker,new ArrayList<>());

        Deque<Integer> dq=new ArrayDeque<>();
        dq.add(attacker);

        while(!dq.isEmpty()){
            int size=dq.size();

            for(int i=0;i<size;i++){
                int cur=dq.poll();

                for(int[] next:mv){
                    int nx=cur/M+next[0];
                    int ny=cur%M+next[1];

                    if(nx<0){
                        nx=N-1;
                    }
                    else if(nx==N){
                        nx=0;
                    }

                    if(ny<0){
                        ny=M-1;
                    }
                    else if(ny==M){
                        ny=0;
                    }

                    int nextIdx=nx*M+ny;

                    //죽은 타워
                    if(!tower.containsKey(nextIdx)){
                        continue;
                    }

                    //이미 지나간 경로
                    if(paths.containsKey(nextIdx)){
                        continue;
                    }

                    if(nextIdx==target){
                        return paths.get(cur);
                    }

                    List<Integer> nextPath=new ArrayList<>();

                    for(int p:paths.get(cur)){
                        nextPath.add(p);
                    }

                    nextPath.add(nextIdx);

                    paths.put(nextIdx,nextPath);
                    dq.add(nextIdx);
                }
            }
        }

        return null;
    }
}