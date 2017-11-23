package untitled3;

import java.util.ArrayList;

/**
 * Created by novas on 16/4/25.
 */
public class yuanzu {
    public int[] array;
    //包含出现的行数
    public ArrayList<Integer> list=new ArrayList<>();
    public int choosed=-1;
    public void add(int lineindex)
    {
        list.add(lineindex);
    }
    public boolean hasSameLines(yuanzu yuanzu)
    {
        if(this.linetoString().equals(yuanzu.linetoString()))
        {
            return true;
        }
        return false;
    }
    public boolean containArray(int[] array)
    {
        for(int i=0;i<array.length;i++)
        {
            if(!contains(array[i]))
            {
                return false;
            }
        }
        return true;
    }
    public int[] getLineArray()
    {
        int[] res=new int[list.size()];
        for(int i=0;i<res.length;i++)
        {
            res[i]=list.get(i);
        }
        return res;
    }
    public yuanzu()
    {

    }
    public yuanzu(int[] array)
    {
        this.array=array;
    }
    public yuanzu(int i)
    {
        array=new int[1];
        array[0]=i;
    }
    public yuanzu(yuanzu yuanzu,int i)
    {
        array=new int[yuanzu.getLength()+1];
        System.arraycopy(yuanzu.array, 0, array, 0, yuanzu.getLength());
        array[array.length-1]=i;
    }
    public void setArray(int[] array)
    {
        this.array=array;
    }
    public int getLength()
    {
        return array.length;
    }
    public int get(int position)
    {
        return array[position];
    }

    @Override
    public String toString() {
        String res="";
        for(int i=0;i<array.length;i++)
        {
            res=res+" "+array[i];
        }
        res=res+"  length="+list.size()+" line:={ ";
        for(int i=0;i<list.size();i++)
        {
            res=res+" "+list.get(i);
        }
        res=res+"}";
        return res;
    }
    public String linetoString()
    {
        String res="";
        for(int i=0;i<list.size();i++)
        {
            res=res+list.get(i);
        }
        return res;
    }
    public boolean contains(int i)
    {
        for(int j=0;j<array.length;j++)
        {
            if(array[j]==i)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hascode=0;
        for(int i=0;i<array.length;i++)
        {
            hascode=hascode+array[i];
        }
        return hascode;
    }

    @Override
    public boolean equals(Object obj) {
        yuanzu yuanzu=(yuanzu)obj;
        if(yuanzu.getLength()!=array.length)
        {
            return false;
        }
        for(int i=0;i<array.length;i++)
        {
           if(!yuanzu.contains(array[i]))
           {
               return false;
           }
        }
        return true;
    }
}
