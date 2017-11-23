package untitled3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ming Sun 实现相关系数计算,调用分子计算和分母计算
 *
 */

public class CalculateCORR{

    public double CORRcal(int left,int right,String filename){
        double CORR = 0.0;

        try{
            File f = new File(filename);

            FileInputStream fstreamf = new FileInputStream(f);

            DataInputStream inf = new DataInputStream(fstreamf);

            BufferedReader brf = new BufferedReader(new InputStreamReader(inf));

            //double CORR = 0.0;
            List<String> xList = new ArrayList<String>();;
            List<String> yList = new ArrayList<String>();

            String str = null;

            int count = 0;

            while((str=brf.readLine()) != null){

                String[] vStr = str.split(";");

                //判断是否含有缺失值
                if(vStr[left-1].equals("?")||vStr[right-1].equals("?")){
                    continue;
                }

                xList.add(count, vStr[left-1]);

                yList.add(count, vStr[right-1]);

                count++;
            }

            //System.out.println(count);

            /*for(int j = 0;j < xList.size();j++){
                System.out.println(xList.get(j)+" "+yList.get(j));
            }*/

            NumeratorCalculate nc = new NumeratorCalculate(xList,yList);
            double numerator = nc.calcuteNumerator();
            DenominatorCalculate dc = new DenominatorCalculate();
            double denominator = dc.calculateDenominator(xList, yList);
            CORR = numerator/denominator;
            //System.out.println("We got the result by Calculating:");
            //System.out.println("CORR = "+CORR);

            inf.close();

        }catch(Exception e){

            System.err.println("CORRError: " + e.getMessage());

        }

        return CORR;
    }
}