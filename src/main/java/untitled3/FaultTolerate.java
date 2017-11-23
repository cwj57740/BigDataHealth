package untitled3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by novas on 15/4/25.
 */
public class FaultTolerate {
    //最原始数据
    static ArrayList<linvector> orilinvectorArrayList=null;
    static ArrayList<linvector> linvectorArrayList=null;
    //寰得到的视图
    static ArrayList<yuanzu> F=new ArrayList<>();
    //0.3-0.5
    static double apriori=0.4;
    //0.1-0.3
    static double lost=0.3;
    //0.6-1
    static double setcover=0.8;

    public int FT(String filename)throws IOException
    {
        //long start = System.currentTimeMillis();
        //System.out.println(""+start);
        //String filename="data.txt";
        //filename = "/Library/apache-tomcat-8.5.4/bin/"+filename;
        linvectorArrayList=FileUtils.readData(filename);
        //orilinvectorArrayList=untitled3.FileUtils.readData(filename);
        //untitled3.FileUtils.makeLost(linvectorArrayList, (int) (linvectorArrayList.size() * 15 * lost - 1012));
        //String lostname="lost.txt";
        //linvectorArrayList=untitled3.FileUtils.readData(lostname);

        int length=linvectorArrayList.size();
        System.out.println("length="+length);
        HashMap<Integer,Integer> map=new HashMap<>();
        for(int j=0;j<15;j++)
        {
            map.put(j,0);
        }
        for(int j=0;j<15;j++)
        {
            for(int i=0;i<linvectorArrayList.size();i++)
            {
                //System.out.println(linvectorArrayList.get(i));
                if(linvectorArrayList.get(i).get(j)!=-1)
                {
                    map.put(j,map.get(j)+1);
                }
            }
        }
        System.out.println(map);
        ArrayList<yuanzu> initList=new ArrayList<>();
        ArrayList<yuanzu> nextList=new ArrayList<>();
        //表示所有特征值
        int[] all=new int[15];
        for(int i=0;i<all.length;i++)
        {
            all[i]=i;
        }
        for(int i=0;i<15;i++)
        {
            nextList.add(new yuanzu(i));
        }
        //apriori得到频繁集合
        while (compare(initList,nextList)==-1&&nextList.size()>0&&nextList.get(0).getLength()<15)
        {
            initList.clear();
            initList.addAll(nextList);
            Set<yuanzu> yuanzuSet=new HashSet<>();
            for(int i=0;i<initList.size();i++)
            {
                // System.out.println("*****");
                yuanzu yuanzu=initList.get(i);
                //   System.out.println("untitled3.yuanzu="+untitled3.yuanzu);
                int[] left=getLeft(yuanzu,all);
                for(int j=0;j<left.length;j++)
                {
                    //  System.out.println("left[j]="+left[j]);
                    yuanzu newyuanzu=new yuanzu(yuanzu,left[j]);
                    if(FileUtils.getCount(linvectorArrayList,newyuanzu)>apriori*linvectorArrayList.size())
                    {
                        yuanzuSet.add(newyuanzu);
                    }
                }

            }
            //  System.out.println("========***************"+yuanzuSet.size()+"  "+F.size());
            for(yuanzu yuanzu:yuanzuSet)
            {
                //System.out.println(untitled3.yuanzu);
                // F.add(untitled3.yuanzu);
                addToF(yuanzu);
            }
            nextList.clear();
            nextList.addAll(yuanzuSet);
        }
        //greedy-set-cover
        ArrayList<yuanzu> L=greedysetcover(all,linvectorArrayList.size(),setcover,F);

        System.out.println(L.size()+"!!");
        for(yuanzu yuanzu:L) {
            int[] tezheng = yuanzu.array;
            for (int j = 0; j < tezheng.length; j++) {
                System.out.print(tezheng[j]+"  ");
            }
            System.out.println("NEXT");
        }
        //利用knn分类
        int errorcount=0;
        linvector linvector=new linvector();
        try{
            File file=new File("temp.txt");
            BufferedReader br=new BufferedReader(new FileReader(file));
            String line=br.readLine();
            while (line!=null)
            {
                String[] argss=line.split(";");
                double[] array=new double[argss.length];
                for(int i=0;i<argss.length;i++)
                {
                    array[i]=Double.parseDouble(argss[i]);
                    //System.out.println(array[i]);
                }
                //untitled3.linvector untitled3.linvector=new untitled3.linvector();
                linvector.setArray(array);
                line=br.readLine();
            }
        }catch(Exception e){
            System.out.println("test");
        }
        //for(int i=0;i<orilinvectorArrayList.size();i++)
        //{
        int[] array=new int[L.size()];
        int j=0;
        //untitled3.linvector untitled3.linvector=orilinvectorArrayList.get(i);*/

        for(yuanzu yuanzu:L)
        {
            //System.out.println("i="+i+"VI="+untitled3.yuanzu);
            int leibie = knn(yuanzu,linvectorArrayList,linvector,1);
            array[j]=leibie;
            j++;
            //   System.out.println(untitled3.linvector);
        }
        int leibie=vote(array);
        System.out.println("类别"+leibie);

        return leibie;
                /*if(leibie!=untitled3.linvector.get(15))
                {
                    System.out.println("index="+i+"预测结果:"+leibie+" 实际结果:"+untitled3.linvector.get(15));
                    errorcount++;
                }*/
        //}
        //System.out.println("错误个数:"+errorcount);
        //System.out.println("alllength="+orilinvectorArrayList.size());

        //long end = System.currentTimeMillis();
        //System.out.println("" + (end-start)+"ms");

    }
    public static int vote(int[] array)
    {
        int one=0;
        int zero=0;
        for(int i=0;i<array.length;i++)
        {
            double m=array[i];
            if(m<0.5)
            {
                zero++;
            }
            else
            {
                one++;
            }
        }
        if(one>zero)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
    public static int knn(yuanzu knownyuanzu,ArrayList<linvector> data,linvector yuce,int k)
    {
        int[] tezheng=knownyuanzu.array;
        for(int j = 0;j<tezheng.length;j++){
            System.out.println(tezheng[j]);
        }
        System.out.println("next");

        for(int j = 0;j<yuce.array.length;j++){
            System.out.println(yuce.array[j]);
        }
        int[] lines=knownyuanzu.getLineArray();
        //System.out.println("next");
        //预测的向量
        double[] yucevector=new double[tezheng.length];
        //System.out.println("next");
        String vector1str1="";
        for(int i=0;i<yucevector.length;i++)
        {
            yucevector[i]=yuce.get(tezheng[i]);
            //System.out.println(yucevector[i]);
            vector1str1=vector1str1+" "+yucevector[i];
            System.out.println(vector1str1);
        }
        System.out.println(vector1str1);
        System.out.println("yucevector="+vector1str1);
        /*for(int j =0;j<15;j++){
            System.out.println(yuce.get(j));
        }*/
        //System.out.println(yucevector.length);
        ArrayList<Double>  distanceArray=new ArrayList<>();
        for(int i=0;i<lines.length;i++)
        {
            linvector linvector=data.get(lines[i]);
            //样本中向量
            double[] vector1=new double[tezheng.length];
            String vector1str="";
            for(int j=0;j<vector1.length;j++)
            {
                vector1[j]=linvector.get(tezheng[j]);
                vector1str=vector1str+" "+vector1[j];
            }
            //计算欧式距离
            double distance=0;
            for(int j=0;j<vector1.length;j++)
            {
                distance=distance+(vector1[j]-yucevector[j])*(vector1[j]-yucevector[j]);
            }
            distance=Math.sqrt(distance);
            distanceArray.add(distance);
            // System.out.println("vector1=" + vector1str+"  "+distance+"  "+i+"   "+untitled3.linvector.get(13));
        }
//System.out.println("distance="+distanceArray.size());
        //找出最小距离的k个位置，就是行数
        ArrayList<Integer> indexarray=new ArrayList<>();
        for(int j=0;j<k;j++)
        {
            int index=-1;
            double min=Double.MAX_VALUE;
            for(int i=0;i<distanceArray.size();i++)
            {
                if(distanceArray.get(i)<min)
                {
                    index=i;
                    min=distanceArray.get(i);
                }
            }
            indexarray.add(index);
            //  System.out.println("index=" + index + "  " + min+"  "+data.get(index).get(13));
            distanceArray.set(index, Double.MAX_VALUE);
        }
        int vote=vote(knownyuanzu,indexarray,data);
        System.out.println("votew="+vote);
        return vote;
    }
    public static int vote(yuanzu knownyuanzu,ArrayList<Integer> index,ArrayList<linvector> data)
    {
        int one=0;
        int zero=0;
        int[] lines=knownyuanzu.getLineArray();
        for(int i=0;i<index.size();i++)
        {
            double m=data.get(lines[index.get(i)]).get(15);
            if(m<0.5)
            {
                zero++;
            }
            else
            {
                one++;
            }
        }
        if(one>zero)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
    public static void addToF(yuanzu yuanzu)
    {
        if(F.size()==0)
        {
            F.add(yuanzu);
            return;
        }
        for(int i=0;i<F.size();i++)
        {
            yuanzu oldyuan=F.get(i);
            if(oldyuan.hasSameLines(yuanzu))
            {
                if(oldyuan.array.length>yuanzu.array.length)
                {
                    if(!oldyuan.containArray(yuanzu.array))
                    {
                        F.add(yuanzu);
                        break;
                    }
                    else
                    {
                        F.add(yuanzu);
                        F.remove(i);
                        break;
                    }

                }
                else
                {
                    if(!yuanzu.containArray(oldyuan.array))
                    {
                        F.add(yuanzu);
                        break;
                    }
                }
            }
            else
            {
                F.add(yuanzu);
                break;
            }
        }
    }
    //all表示所有特征值，指的是0-12，length样本数,p是比例,F是试图集合
    public static ArrayList<yuanzu> greedysetcover(int[] all,int length,double p,ArrayList<yuanzu> F)
    {
        System.out.println("p*length="+p*length+"  "+F.size());
        int[] U=all;
        int[] T=new int[length];
        for(int i=0;i<T.length;i++)
        {
            T[i]=i;
        }
        ArrayList<yuanzu> L=new ArrayList<>();
        int[] M=new int[0];
        while (U.length>0)
        {
            int maxindex=-1;
            int max=0;
            if(M.length>p*length)
            {
                for(int i=0;i<F.size();i++)
                {
                    yuanzu yuanzu=F.get(i);
                    if(yuanzu.choosed==-1)
                    {
                        int SIU=0;
                        //SI和U的交
                        for(int j=0;j<U.length;j++)
                        {
                            if(yuanzu.contains(U[j]))
                            {
                                SIU++;
                            }
                        }
                        if(SIU>max)
                        {
                            max=SIU;
                            maxindex=i;
                        }
                    }

                }

            }
            else
            {
                for(int i=0;i<F.size();i++)
                {
                    yuanzu yuanzu=F.get(i);
                    if(yuanzu.choosed==-1)
                    {
                        int SIU=0;
                        //SI和U的交
                        for(int j=0;j<U.length;j++)
                        {
                            if(yuanzu.contains(U[j]))
                            {
                                SIU++;
                            }
                        }
                        //System.out.println("SIU="+SIU);
                        //RI 和Ｍ
                        ArrayList<Integer> list=new ArrayList<>();
                        yuanzu temp=new yuanzu(M);
                        for(int j=0;j<yuanzu.list.size();j++)
                        {
                            if(!temp.contains(yuanzu.list.get(j)))
                            {
                                list.add(yuanzu.list.get(j));
                            }
                        }
                        int RIM=list.size();
                        if(RIM*SIU>max)
                        {
                            max=RIM*SIU;
                            maxindex=i;
                        }
                    }

                }
            }
            System.out.println("maxindex=" + maxindex + "  " + F.size());
            if(maxindex!=-1)
            {
                F.get(maxindex).choosed=1;
                U=reduce(U,F.get(maxindex).array);
                T=reduce(T,F.get(maxindex).getLineArray());
                M=add(M,F.get(maxindex).getLineArray());
                L.add(F.get(maxindex));
            }
            else
            {
                break;
            }

        }
        return L;
    }
    public static  int[] add(int[] array1,int [] array2)
    {
        Set<Integer> set=new HashSet<>();
        for(int i=0;i<array1.length;i++)
        {
            set.add(array1[i]);
        }
        for(int i=0;i<array2.length;i++)
        {
            set.add(array2[i]);
        }
        int[] res=new int[set.size()];
        int i=0;
        for(Integer integer:set)
        {
            res[i]=integer;
            i++;
        }
        return res;
    }
    public static int[] reduce(int[] array1,int[] array2)
    {
        yuanzu yuanzu=new yuanzu(array2);
        ArrayList<Integer> list=new ArrayList<>();
        for(int i=0;i<array1.length;i++)
        {
            if(!yuanzu.contains(array1[i]))
            {
                list.add(array1[i]);
            }
        }
        int[] res=new int[list.size()];
        for(int i=0;i<res.length;i++)
        {
            res[i]=list.get(i);
        }
        return res;
    }
    public static int[] getLeft(yuanzu yuanzu,int[] all)
    {
        int[] alltemp=new int[all.length];
        System.arraycopy(all,0,alltemp,0,all.length);
        int[] left=new int[alltemp.length-yuanzu.getLength()];
        for(int i=0;i<yuanzu.getLength();i++)
        {
            alltemp[yuanzu.get(i)]=-1;
        }
        int j=0;
        for(int i=0;i<all.length;i++)
        {
            if(alltemp[i]!=-1)
            {
                left[j]=alltemp[i];
                j++;
            }
        }
        return left;
    }
    public static int compare(ArrayList<yuanzu> init,ArrayList<yuanzu> next)
    {
        if(next==null||next.size()==0||init.size()==0||init.get(0).getLength()!=next.get(0).getLength())
        {
            return -1;
        }
        return 1;
    }
}
