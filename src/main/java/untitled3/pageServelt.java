package untitled3;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Ming Sun on 16/4/26.
 */


public class pageServelt extends HttpServlet
{
   // Connection connection=null;
    private XorExample xorExample;
    private Kidney kid;

    @Override
    public void init() throws ServletException {
        xorExample=new XorExample();
        kid=new Kidney();
    }
    @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                                  System.out.println("afasdfdsaf");
        String age=req.getParameter("age");
        String sex=req.getParameter("sex");
        //String male=req.getParameter("male");
        //System.out.println(sex);
        String hurttype=req.getParameter("hurttype");
        String trestbps=req.getParameter("trestbps");
        String chol=req.getParameter("chol");
        String fbs=req.getParameter("fbs");
        String restecg=req.getParameter("restecg");
        String thalach=req.getParameter("thalach");
        String exang=req.getParameter("exang");
        String oldpeak=req.getParameter("oldpeak");
        String slope=req.getParameter("slope");
        String ca=req.getParameter("ca");
        String thal=req.getParameter("thal");
        String hxb=req.getParameter("hxb");
        String nb=req.getParameter("nb");
        String nbtk=req.getParameter("nbtk");
        String xj=req.getParameter("xj");
        String xt=req.getParameter("xt");
        String xns=req.getParameter("xns");
        String xjg=req.getParameter("xjg");
        String hxbjs=req.getParameter("hxbjs");
        String gxy=req.getParameter("gxy");
        String tnb=req.getParameter("tnb");
        String sy=req.getParameter("sy");
        String zsz=req.getParameter("zsz");
        String px=req.getParameter("px");

        String tt = "";
        tt = age+";"+hxb+";"+nb+";"+nbtk+";"+xj+";"+xt+";"+xns+";"+xjg+";"+hxbjs+";"+gxy+";"+tnb+";"+"1.0"+";"+sy+";"+zsz+";"+px;

        System.out.println("tt="+tt);

        String file1=req.getParameter("file1");


        String filename1="/Users/MingSun/"+file1.substring(12);

        System.out.println(filename1);

        String file2=req.getParameter("file2");

                String filename2="/Users/MingSun/"+file2.substring(12);

        //System.out.println(filename2);

        Double age1=Double.valueOf(age)/100;
        age=age1+"";

        Double cp1=Double.valueOf(hurttype)/10;
        hurttype=cp1+"";

        Double trestbps1=Double.valueOf(trestbps)/1000;
        trestbps=trestbps1+"";

        Double chol1=Double.valueOf(chol)/1000;
        chol=chol1+"";

        Double restecg1=Double.valueOf(restecg)/10;
        restecg=restecg1+"";

        Double thalach1=Double.valueOf(thalach)/1000;
        thalach=thalach1+"";

        Double oldpeak1=Double.valueOf(oldpeak)/10;
        oldpeak=oldpeak1+"";

        Double slope1=Double.valueOf(slope)/10;
        slope=slope1+"";

        Double ca1=Double.valueOf(ca)/10;
        ca=ca1+"";

        Double thal1=Double.valueOf(thal)/10;
        thal=thal1+"";

        Double xt1=Double.valueOf(xt)/1000;
        xt=xt1+"";

        Double xns1=Double.valueOf(xns)/1000;
        xns=xns1+"";

        Double xjg1=Double.valueOf(xjg)/100;
        xjg=xjg1+"";

        Double hxbjs1=Double.valueOf(hxbjs)/10;
        hxbjs=hxbjs1+"";

        System.out.println("befor");
        double[]   aa=new double[13];
        aa[0]=age1;
        aa[1]=Double.parseDouble(sex);
        aa[2]=cp1;
        aa[3]=trestbps1;
        aa[4]=chol1;
        aa[5]=Double.parseDouble(fbs);
        aa[6]=restecg1;
        aa[7]=thalach1;
        aa[8]=Double.parseDouble(exang);
        aa[9]=oldpeak1;
        aa[10]=slope1;
        aa[11]=ca1;
        aa[12]=thal1;

        if(xorExample==null)
        {
            System.out.println("null");
        }
        else
        {
            System.out.println("not null");
        }
        double s=xorExample.run(aa,resp,filename1);
                System.out.println("s="+s);

        String heart = null;
        String hres = null;

        if((1-s)<(s-0))  { heart="1.0"; hres="may suffer heart disease"; } //��1���� Ԥ��Ϊ�в�
        else { heart="0.0";  hres="may not suffer heart disease" ;     }

        String[] tte = tt.split(";");
        tte[10] = heart;
        /*for(int j =0 ;j<tte.length;j++){
            System.out.println(tte[j]);
        }*/

        String kres = null;

        if(file2.substring(12).equals("new.txt")){
            //System.out.println("here~~~");
            //要用贝叶斯网络
            try {
                FileWriter writer = new FileWriter("temp.txt");
                String str = "";
                for(int j = 0;j < tte.length-1;j++){
                    str += tte[j]+";";
                }
                str = str + tte[tte.length-1];
                //str = hxb+";"+nb+";"+nbtk+";"+xj+";"+xt+";"+xns+";"+xjg+";"+hxbjs+";"+gxy+";"+tnb+";"+heart+";"+sy+";"+zsz+";"+px;
                System.out.println(str);
                writer.write(str);
                writer.close();
            } catch (IOException ee) {

                ee.printStackTrace();

            }

            String filename = "/Library/apache-tomcat-8.5.4/bin/"+file2.substring(12);

            FaultTolerate ftt = new FaultTolerate();
            int res = 0;
            res = ftt.FT(filename);

            if(res ==1){
                kres="may suffer chronic kidney disease";
            }
            else{
                kres="may not suffer chronic kidney disease";
            }
        }else{

        double[]   bb=new double[15];
                bb[0]=age1;
                bb[1]=Double.parseDouble(hxb);
                bb[2]=Double.parseDouble(nb);
                bb[3]=Double.parseDouble(nbtk);
                bb[4]=Double.parseDouble(xj);
                bb[5]=xt1;
                bb[6]=xns1;
                bb[7]=xjg1;
                bb[8]=hxbjs1;
                bb[9]=Double.parseDouble(gxy);
                bb[10]=Double.parseDouble(tnb);
                bb[11]=Double.parseDouble(heart);
                bb[12]=Double.parseDouble(sy);
                bb[13]=Double.parseDouble(zsz);
                bb[14]=Double.parseDouble(px);
        double ss=kid.run(bb,resp,filename2);


                if((1-ss)<(ss-0))  { kres="may suffer chronic kidney disease"; } //��1���� Ԥ��Ϊ�в�
                else { kres="may not suffer chronic kidney disease";      }
        }

            OutputStream outputStream=resp.getOutputStream();
            byte[] bytes=(hres+";"+kres).getBytes("utf-8");
            resp.setHeader("Content-Length", bytes.length + "");
            for(int j=0;j<bytes.length;j++)
            {
                //System.out.println(bytes[j]+"   ");
            }
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
//������Ӧ��������

      }
       
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // super.doPost(req, resp);
        }



}
