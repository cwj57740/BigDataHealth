package untitled3;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by novas on 16/4/25.
 */
public class FileUtils {
    //获取元组在数据集个数
    public static int getCount(ArrayList<linvector> linvectorArrayList,yuanzu yuanzu)
    {
        int[] array=yuanzu.array;
        int count=0;
        int flag=0;
        for (int i=0;i<linvectorArrayList.size();i++)
        {
            linvector linvector=linvectorArrayList.get(i);
          //  count++;
            flag=0;
            for(int j=0;j<array.length;j++)
            {
                if(linvector.get(array[j])==-1)
                {
                   // count--;
                    flag=1;
                    break;
                }
            }
            if(flag==0)
            {
                yuanzu.add(i);
                count++;
            }
        }
        return count;
    }
    //count表示缺失值个数
    public static void makeLost(ArrayList<linvector> linvectorArrayList,int count)
    {
        int all=linvectorArrayList.size()*13;
        Random random=new Random();
        for(int i=0;i<count;)
        {
            int lostposition=random.nextInt(all);
           // System.out.println("position="+lostposition);
            int index=lostposition/13;
            int position=lostposition%13;
            linvector linvector=linvectorArrayList.get(index);
            if(linvector.get(position)!=-1)
            {
                linvector.set(position,-1);
                i++;
            }
        }
        File file=new File("lost.txt");
        FileWriter fw=null;
        try {
            fw=new FileWriter(file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        for(int i=0;i<linvectorArrayList.size();i++)
        {
            try {
                fw.write(linvectorArrayList.get(i).toArray());
                fw.write("\r\n");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try {
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static ArrayList<linvector> readData(String filename)throws IOException
    {
        ArrayList<linvector> linvectors=new ArrayList<>();
        File file=new File(filename);
        BufferedReader br=new BufferedReader(new FileReader(file));
        String line=br.readLine();
        while (line!=null)
        {
            String[] args=line.split(";");
            double[] array=new double[args.length];
            for(int i=0;i<args.length;i++)
            {
                array[i]=Double.parseDouble(args[i]);
            }
            linvector linvector=new linvector();
            linvector.setArray(array);
            linvectors.add(linvector);
            line=br.readLine();
        }
        return linvectors;
    }
}
