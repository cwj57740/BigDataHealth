package untitled3;

import java.io.*;
import java.util.*;

public class BYS {

    public void bys(String filename) {

        //System.out.println("Hello World!");

        //每一列的缺失值个数先存到Hashmap里
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        //用于存储依赖度降低的顺序
        int[] order = new int[15];

        //存储每个属性的parent属性
        ArrayList<ArrayList<Integer>> parent = new ArrayList<ArrayList<Integer>>();


        try {
            File f = new File(filename);

            FileInputStream fstreamf = new FileInputStream(f);

            DataInputStream inf = new DataInputStream(fstreamf);

            BufferedReader brf = new BufferedReader(new InputStreamReader(inf));

            String strLine;

            int[] count = new int[15];

            while ((strLine = brf.readLine()) != null) {

                String[] values = strLine.split(";");

                for (int j = 0; j < 15; j++) {
                    if (values[j].equals("?")) {
                        count[j]++;
                    }
                }
            }

            for (int j = 0; j < 15; j++) {
                map.put(j + 1, count[j]);
            }

            List<Map.Entry<Integer, Integer>> infoIds =
                    new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());

            //排序
            Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Integer>>() {
                public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            for (int i = 0; i < infoIds.size(); i++) {
                String id = infoIds.get(i).getKey().toString() + " " + infoIds.get(i).getValue().toString();
                order[i] = infoIds.get(i).getKey();
                System.out.println(id);
            }

            /*for(int j = 0;j < 15;j++){
                //map.put(j+1,count[j]);
                System.out.println(order[j]);
            }*/

            inf.close();

        } catch (Exception e) {

            System.err.println("MainError: " + e.getMessage());

        }


        CalculateCORR ca = new CalculateCORR();

        int left = 0;
        int right = 0;
        //用于存储各列之间的相关系数
        double[][] corr = new double[16][16];
        for (int i = 0; i < 15; i++) {
            left = order[i];
            for (int j = 0; j < i; j++) {
                right = order[j];
                corr[left][right] = Math.abs(ca.CORRcal(left, right,filename));
            }
        }

        //为每个变量找到他的父亲节点变量
        for (int i = 0; i < 15; i++) {
            double old_score = 0.0;
            ArrayList<Integer> parentSub = new ArrayList<Integer>();
            left = order[i];
            for (int j = 0; j < i; j++) {
                right = order[j];
                double new_score = corr[left][right];
                if (new_score > old_score) {
                    old_score = new_score;
                    parentSub.add(right);
                }
            }
            if (parentSub != null) {
                parent.add(parentSub);
            }
        }

        /*for (int i = 0; i < 15; i++) {
            System.out.println("parentnode!!");
            System.out.print(order[i] + ": ");
            ArrayList<Integer> temp = new ArrayList<Integer>();
            temp = parent.get(i);
            for (int j = 0; j < temp.size(); j++) {
                System.out.print(temp.get(j) + " ");
            }
            System.out.print("\n");
        }*/

        //开始填充 从top到bottom

        Inference binf = new Inference();

        //为了不频繁的读写文件,定义一个数组,用来暂存初始值和被填充后的值,如果不确定性较高,返回-1
        double[][] tempdataset = new double[400][16];

        try {
            File f = new File(filename);

            FileInputStream fstreamf = new FileInputStream(f);

            DataInputStream inf = new DataInputStream(fstreamf);

            BufferedReader brf = new BufferedReader(new InputStreamReader(inf));

            String strLine;

            int count = 0;

            while ((strLine = brf.readLine()) != null) {

                String[] values = strLine.split(";");

                //将要填充的目标元组转换成double类型
                double[] value = new double[16];
                for (int j = 0; j < 16; j++) {

                    if (values[j].equals("?")) {
                        value[j] = -1;
                    } else {
                        value[j] = Double.valueOf(values[j]).doubleValue();
                    }
                }

                //先将确定的部分赋给tempdataset
                for (int j = 0; j < 16; j++) {
                    tempdataset[count][j] = value[j];
                }

                count++;
            }
            inf.close();
        }catch(Exception ef){
                System.out.println("dashuzu!");
            }

        for (int i = 0; i < 15; i++) {
            int targetnode = order[i];
            //判断是不是连续变量
            if ((targetnode != 1) && (targetnode != 6) && (targetnode != 7) && (targetnode != 8) && (targetnode != 9)) {
                try {
                    File f = new File(filename);

                    FileInputStream fstreamf = new FileInputStream(f);

                    DataInputStream inf = new DataInputStream(fstreamf);

                    BufferedReader brf = new BufferedReader(new InputStreamReader(inf));

                    String strLine;

                    int count = 0;

                    while ((strLine = brf.readLine()) != null) {

                        String[] values = strLine.split(";");

                        //将要填充的目标元组转换成double类型
                        double[] value = new double[16];
                        for (int j = 0; j < 16; j++) {

                            if (values[j].equals("?")) {
                                value[j] = -1;
                            }
                            else{
                                value[j] = Double.valueOf(values[j]).doubleValue();
                            }
                        }

                        ArrayList<Integer> temp = new ArrayList<Integer>();

                        if (values[targetnode - 1].equals("?")) {

                            temp = parent.get(i);

                            //找到合适的填充值
                            tempdataset[count][targetnode - 1] = binf.disimpute(value, temp, targetnode,filename);

                            //System.out.println(tempdataset[count][targetnode - 1]);
                        }

                        count++;
                    }
                    inf.close();
                } catch (Exception e) {

                    System.err.println("MainInfDisError: " + e.getMessage());

                }
            }
            //连续变量的处理
            else {
                try {
                    File f = new File(filename);

                    FileInputStream fstreamf = new FileInputStream(f);

                    DataInputStream inf = new DataInputStream(fstreamf);

                    BufferedReader brf = new BufferedReader(new InputStreamReader(inf));

                    String strLine;

                    int count = 0;

                    while ((strLine = brf.readLine()) != null) {

                        String[] values = strLine.split(";");

                        //将要填充的目标元组转换成double类型
                        double[] value = new double[16];
                        for (int j = 0; j < 16; j++) {

                            if (values[j].equals("?")) {
                                value[j] = -1;
                            }
                            else{
                                value[j] = Double.valueOf(values[j]).doubleValue();
                            }
                        }

                        ArrayList<Integer> temp = new ArrayList<Integer>();

                        if (values[targetnode - 1].equals("?")) {

                            temp = parent.get(i);

                            //找到合适的填充值
                            tempdataset[count][targetnode - 1] = binf.conimpute(value, temp, targetnode,filename);

                            //System.out.println(tempdataset[count][targetnode - 1]);
                        }

                        count++;

                    }
                } catch (Exception e) {

                    System.err.println("MainInfConError: " + e.getMessage());

                }
            }

        }
        System.out.println("tianchongwanbi");

        //for循环结束,说明所有可以被填充的部分被填充完毕,将tempdataset写入new.txt文件
        try {
            FileWriter writer = new FileWriter("new.txt");
            String str = "";

            for (int count = 0; count < 400; count++) {
                for (int j = 0; j < 15; j++) {
                    str += tempdataset[count][j] + ";";
                }
                str += tempdataset[count][15]+"\n";
                writer.write(str);
                str = "";
            }
            writer.close();
        } catch (IOException ee) {

            ee.printStackTrace();

        }

        try {
            File f = new File("new.txt");

            FileInputStream fstreamf = new FileInputStream(f);

            DataInputStream inf = new DataInputStream(fstreamf);

            BufferedReader brf = new BufferedReader(new InputStreamReader(inf));

            String strLine;

            int[] count = new int[15];

            while ((strLine = brf.readLine()) != null) {

                String[] values = strLine.split(";");

                for (int j = 0; j < 15; j++) {
                    if (values[j].equals("-1.0")) {
                        count[j]++;
                    }
                }
            }

            for (int j = 0; j < 15; j++) {
                map.put(j + 1, count[j]);
            }

            List<Map.Entry<Integer, Integer>> infoIds =
                    new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());

            //排序
            Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Integer>>() {
                public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            for (int i = 0; i < infoIds.size(); i++) {
                String id = infoIds.get(i).getKey().toString() + " " + infoIds.get(i).getValue().toString();
                order[i] = infoIds.get(i).getKey();
                System.out.println(id);
            }
        }catch(Exception e){
            System.out.println("test");
        }
    }
}
