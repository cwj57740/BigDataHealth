package untitled3;

/**
 * Created by novas on 16/4/25.
 */
public class linvector {
    public double[] array;
    public void setArray(double[] array)
    {
        this.array=array;
    }
    public String toArray()
    {
        String res="";
        for(int i=0;i<array.length;i++)
        {
            res=res+array[i]+";";
        }
        return res.substring(0,res.length()-1);
    }
    public boolean contansLoast()
    {
        for(int i=0;i<array.length;i++)
        {
            if(array[i]==-1)
            {
                return true;
            }
        }
        return false;
    }

    public double get(int position)
    {
        return array[position];
    }
    public void set(int position,double value)
    {
        this.array[position]=value;
    }

    @Override
    public String toString() {
        String res="";
        for(int i=0;i<array.length;i++)
        {
            res=res+" "+array[i];
        }
        return res;
    }
}
