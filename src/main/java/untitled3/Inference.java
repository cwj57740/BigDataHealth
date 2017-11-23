package untitled3;

import java.io.*;
import java.util.ArrayList;

public class Inference {

    double certaintythreshold = 0.8;

    public double disimpute(double[] value, ArrayList<Integer> temp, int targetnode,String filename) {

        double result = 0.0;

        //System.out.println(targetnode+"=================");

        //用来存储相关变量对应的值
        ArrayList<Double> target = new ArrayList<Double>();

        for(int j = 0;j < temp.size();j++){
            //System.out.print(temp.get(j)+"!");
            target.add(value[temp.get(j)]);
        }
        //System.out.print("\n");

        /*for (int j = 0; j < target.size(); j++) {
            System.out.print(target.get(j) + " ");
        }*/

        //二值
        int truth = 0;
        int fake = 0;

        //说明是top变量
        if(temp.size() == 0){
            try{

                File f = new File(filename);

                FileInputStream fstreamf = new FileInputStream(f);

                DataInputStream inf = new DataInputStream(fstreamf);

                BufferedReader brf = new BufferedReader(new InputStreamReader(inf));

                String strLine;

                int count = 0;

                while ((strLine = brf.readLine()) != null) {

                    String[] values = strLine.split(";");

                    if(values[targetnode-1].equals("?")) continue;

                    double res = Double.valueOf(values[targetnode-1]);

                    if(res==1) truth++;
                    else fake++;

                    count++;
                }

                //System.out.println(targetnode+":"+truth+" "+count+" "+truth/count);

                if(truth > fake){
                    if((truth/Double.valueOf(count)) > certaintythreshold){
                        result = 1.0;
                    }
                    else result = -1;
                }
                else{
                    if((fake/Double.valueOf(count)) > certaintythreshold){
                        result = 0.0;
                    }
                    else result = -1;
                }

                inf.close();

            }catch(Exception e){

                System.err.println("InfIFError: " + e.getMessage());

            }
        }
        //不是top变量,有父亲属性
        else{
            try{

                File f = new File(filename);

                FileInputStream fstreamf = new FileInputStream(f);

                DataInputStream inf = new DataInputStream(fstreamf);

                BufferedReader brf = new BufferedReader(new InputStreamReader(inf));

                String strLine;

                int count = 0;

                while ((strLine = brf.readLine()) != null) {

                    String[] values = strLine.split(";");

                    boolean flag1 = true;
                    //首先找到可以用来推理的元组
                    for(int j = 0;j < temp.size();j++){
                        if(values[temp.get(j)-1].equals("?")) flag1 = false;//continue;
                    }
                    if(!flag1) continue;

                    if(values[targetnode-1].equals("?")) continue;

                    count++;

                    //System.out.println(count);

                    boolean flag2 = true;
                    for(int j = 0;j < target.size();j++){
                        if((target.get(j)+"").equals(values[temp.get(j)-1])) flag2 = false;//continue;
                    }
                    if(!flag2) continue;

                    double res = Double.valueOf(values[targetnode-1]);

                    //System.out.println(res);

                    if(res==1) truth++;
                    else fake++;

                }

                //System.out.println(targetnode + ":" +truth+" "+count+" "+truth/count);

                if(truth > fake){
                    if((truth/Double.valueOf(count)) > certaintythreshold){
                        result = 1.0;
                    }
                    else result = -1;
                }
                else{
                    if((fake/Double.valueOf(count)) > certaintythreshold){
                        result = 0.0;
                    }
                    else result = -1;
                }

                inf.close();

            }catch(Exception e){

                System.err.println("InfELSEError: " + e.getMessage());

            }
        }

        return result;

    }

    public double conimpute(double[] value, ArrayList<Integer> temp, int targetnode,String filename) {

        double result = 0.0;

        //用来存储相关变量对应的值
        ArrayList<Double> target = new ArrayList<Double>();

        for(int j = 0;j < temp.size();j++){
            //System.out.print(temp.get(j)+" ");
            target.add(value[temp.get(j)]);
        }

        ArrayList<ArrayList<Double>> spttempX = new ArrayList<ArrayList<Double>>();

        ArrayList<Double> spttempY = new ArrayList<Double>();

        try{

            File f = new File(filename);

            FileInputStream fstreamf = new FileInputStream(f);

            DataInputStream inf = new DataInputStream(fstreamf);

            BufferedReader brf = new BufferedReader(new InputStreamReader(inf));

            String strLine;

            int count = 0;

            double sum = 0.0;

            while ((strLine = brf.readLine()) != null) {

                String[] values = strLine.split(";");

                boolean flag1 = true;
                //首先找到可以用来推理的元组
                for (int j = 0; j < temp.size(); j++) {
                    if (values[temp.get(j) - 1].equals("?")) flag1 = false;
                }
                if (!flag1) continue;

                if (values[targetnode - 1].equals("?")) continue;

                boolean flag2 = true;
                for (int j = 0; j < target.size(); j++) {
                    if ((target.get(j) + "").equals(values[temp.get(j) - 1])) flag2 = false;//continue;
                }
                if (!flag2) continue;

                count++;

                sum += Double.valueOf(values[targetnode - 1]);
            }

                /*ArrayList<Double> sptSub = new ArrayList<Double>();

                for(int j = 0;j < temp.size();j++){
                    sptSub.add(Double.valueOf(values[temp.get(j)-1]));
                }

                if (sptSub != null) {
                    spttempX.add(sptSub);
                }

                spttempY.add(Double.valueOf(values[targetnode-1]));*/

                System.out.println(sum+"   "+count);

                //inf.close();

            /*double[][] sptX = new double[temp.size()][spttempX.size()];
            double[] sptY = new double[spttempX.size()];

            for(int j = 0;j < spttempY.size();j++){
                for(int i = 0;i < temp.size();i++){
                    sptX[i][j] = spttempX.get(j).get(i);
                }
            }

            for(int i = 0;i < spttempY.size();i++){
                sptY[i] = spttempY.get(i);
            }*/

            /*untitled3.SPT spt = new untitled3.SPT();
            double[] a = new double[temp.size()+1];
            double[] dt = new double[4];
            double[] v = new double[temp.size()];
            spt.sqt2(sptX, sptY, temp.size(), spttempY.size(), a, dt, v);

            double res = a[0];
            for(int i = 1;i < a.length;i++){
                res += a[i]*target.get(i-1);
            }

            if(targetnode == 6) System.out.println(res);*/

            double res = sum/count;

            result = res;

                inf.close();

        }catch(Exception e){

            System.err.println("InfCONError: " + e.toString());

        }

        return result;

    }

}