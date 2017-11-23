package untitled3;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by novas on 16/4/17.
 */
public class predictSevelt extends HttpServlet
{
    private String message;
    private XorExample xorExample;
    //mysql mysql=null;
    @Override
    public void init() throws ServletException {
        xorExample=new XorExample();
        System.out.println("in init");
        //xorExample=new untitled3.XorExample();

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);

        String file1=req.getParameter("file1");

        String filename1="/Users/MingSun/"+file1.substring(12);
                System.out.println(filename1);

                String file2=req.getParameter("file2");

        String filename2="/Users/MingSun/"+file2.substring(12);
                System.out.println(filename2);

        int rightcount=0;
        int wrongcount=0;
        double ratio=0;

        if(filename1.equals("/Users/MingSun/heart.snet")) {
            System.out.println("heart");
            double[] aa = new double[13];
            double aares = 0;

            try {
                FileReader reader = new FileReader(filename2);
                BufferedReader br = new BufferedReader(reader);

                String str = null;

                while ((str = br.readLine()) != null) {

                    String[] values = str.split(";");
                    aa[0] = Double.parseDouble(values[0]);
                    aa[1] = Double.parseDouble(values[1]);
                    aa[2] = Double.parseDouble(values[2]);
                    aa[3] = Double.parseDouble(values[3]);
                    aa[4] = Double.parseDouble(values[4]);
                    aa[5] = Double.parseDouble(values[5]);
                    aa[6] = Double.parseDouble(values[6]);
                    aa[7] = Double.parseDouble(values[7]);
                    aa[8] = Double.parseDouble(values[8]);
                    aa[9] = Double.parseDouble(values[9]);
                    aa[10] = Double.parseDouble(values[10]);
                    aa[11] = Double.parseDouble(values[11]);
                    aa[12] = Double.parseDouble(values[12]);
                    aares = Double.parseDouble(values[13]);

                    double s = xorExample.run(aa, resp, filename1);
                    if ((1 - s) < (s - 0)) {
                        s = 1;
                    } //��1���� Ԥ��Ϊ�в�
                    else {
                        s = 0;
                    }

                    if (s == aares)   rightcount++;
                    else wrongcount++;

                        System.out.println(str);
                }

                br.close();
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(filename1.equals("/Users/MingSun/kidney.snet")) {
            double[] bb = new double[15];
                        double bbres = 0;

                        try {
                            FileReader reader = new FileReader(filename2);
                            BufferedReader br = new BufferedReader(reader);

                            String str = null;

                            while ((str = br.readLine()) != null) {

                                String[] values = str.split(";");
                                bb[0] = Double.parseDouble(values[0]);
                                bb[1] = Double.parseDouble(values[1]);
                                bb[2] = Double.parseDouble(values[2]);
                                bb[3] = Double.parseDouble(values[3]);
                                bb[4] = Double.parseDouble(values[4]);
                                bb[5] = Double.parseDouble(values[5]);
                                bb[6] = Double.parseDouble(values[6]);
                                bb[7] = Double.parseDouble(values[7]);
                                bb[8] = Double.parseDouble(values[8]);
                                bb[9] = Double.parseDouble(values[9]);
                                bb[10] = Double.parseDouble(values[10]);
                                bb[11] = Double.parseDouble(values[11]);
                                bb[12] = Double.parseDouble(values[12]);
                                bb[13] = Double.parseDouble(values[13]);
                                bb[14] = Double.parseDouble(values[14]);
                                bbres = Double.parseDouble(values[15]);

                                double s = xorExample.run(bb, resp, filename1);
                                if ((1 - s) < (s - 0)) {
                                    s = 1;
                                } //��1���� Ԥ��Ϊ�в�
                                else {
                                    s = 0;
                                }

                                if (s == bbres)   rightcount++;
                                else wrongcount++;

                                    System.out.println(str);
                            }

                            br.close();
                            reader.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
        }
        ratio = (rightcount+0.0)/(rightcount+wrongcount) ;
        System.out.println(rightcount);
        System.out.println(wrongcount);
        OutputStream outputStream=resp.getOutputStream();
                    byte[] bytes=(ratio*100+"%").getBytes("utf-8");
                    resp.setHeader("Content-Length", bytes.length + "");
                    for(int j=0;j<bytes.length;j++)
                    {
                        //System.out.println(bytes[j]+"   ");
                    }
                    outputStream.write(bytes);
                    outputStream.flush();
                    outputStream.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*String file1=req.getParameter("file1");

        String filename1="D://"+file1;
        System.out.println(filename1);

        String file2=req.getParameter("file2");

        String filename2="D://"+file2;
        System.out.println(filename2);

        String file3=req.getParameter("file3");

        String filename3="D://"+file3;
        System.out.println(filename3);

        String file4=req.getParameter("file4");

        String filename4="D://"+file4;
        System.out.println(filename4);    */

    }
}
