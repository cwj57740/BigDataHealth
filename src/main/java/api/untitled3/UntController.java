package api.untitled3;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import untitled3.BYS;
import untitled3.FaultTolerate;
import untitled3.Kidney;
import untitled3.XorExample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/untitled3")
public class UntController {
    private String message;
    private XorExample xorExample;
    private Kidney kid;

    {
        xorExample=new XorExample();
        kid=new Kidney();
        System.out.println("in init");
    }

    @RequestMapping("/dong")
    public String dong(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int inputsize=Integer.valueOf(req.getParameter("sr"));

            Map<String,String> result = new HashMap<>();

            synchronized (UntController.class){
                if(inputsize==13) {
                    result.put("circle",Long.toString(XorExample.circle));
                    result.put("error",Double.toString(XorExample.globalerror));
//                res = XorExample.circle +" " +"Global error:"+ XorExample.globalerror*100+"%";
                }
                else {
                    result.put("circle",Long.toString(Kidney.circle));
                    result.put("error",Double.toString(Kidney.globalerror));
//                res =" " + Kidney.circle +" " +"Global error:"+ Kidney.globalerror*100+"%";;
                }

                return JSON.toJSONString(result);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "failed";
    }

    @RequestMapping(path = "/helloworld", method = RequestMethod.POST)
    public String helloWorld(@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            File targetFile1 = saveFile(file1);
            File targetFile2 = saveFile(file2);

            int inputsize=Integer.valueOf(req.getParameter("sr"));

            int hiddensize=Integer.valueOf(req.getParameter("yc"));

            int outsize=1;

            System.out.println(inputsize+"");

            double Rate=Double.valueOf(req.getParameter("xlsl"));

            double Momentum=Double.valueOf(req.getParameter("xldl"));

            int circles=Integer.valueOf(req.getParameter("xlcs"));

            if((file1.getOriginalFilename().equals("heart.snet"))) {

                xorExample.train(targetFile1.getAbsolutePath(), targetFile2.getAbsolutePath(), inputsize, hiddensize, Rate, Momentum, circles);
            }
            else {
                kid.train(targetFile1.getAbsolutePath(), targetFile2.getAbsolutePath(), inputsize, hiddensize, Rate, Momentum, circles);
            }
            return "success";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "failed";
    }


    @RequestMapping(path = "/page", method = RequestMethod.POST)
    public String page(@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,HttpServletRequest req, HttpServletResponse resp) throws IOException{
        try{
            File targetFile1 = saveFile(file1);
            File targetFile2 = saveFile(file2);


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
            double s=xorExample.run(aa,resp,targetFile1.getAbsolutePath());
            System.out.println("s="+s);

            String heart = null;
            String hres = null;

            if((1-s)<(s-0))  { heart="1.0"; hres="may suffer heart disease"; }
            else { heart="0.0";  hres="may not suffer heart disease" ;     }

            String[] tte = tt.split(";");
            tte[10] = heart;
        /*for(int j =0 ;j<tte.length;j++){
            System.out.println(tte[j]);
        }*/

            String kres = null;

            if(targetFile2.getName().equals("new.txt")){
                //System.out.println("here~~~");
                //ÒªÓÃ±´Ò¶Ë¹ÍøÂç
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

                String filename = targetFile2.getAbsolutePath();

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
                double ss=kid.run(bb,resp,targetFile2.getAbsolutePath());


                if((1-ss)<(ss-0))  { kres="may suffer chronic kidney disease"; } //??1???? ?????§Ó?
                else { kres="may not suffer chronic kidney disease";      }
            }

            return hres+";"+kres;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "failed";
    }

    @RequestMapping(path = "/predict", method = RequestMethod.POST)
    public String predict(@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,HttpServletRequest req, HttpServletResponse resp) throws IOException{

        try{
            File targetFile1 = saveFile(file1);
            File targetFile2 = saveFile(file2);

            int rightcount=0;
            int wrongcount=0;
            double ratio=0;

            if(targetFile1.getName().equals("heart.snet")) {
                System.out.println("heart");
                double[] aa = new double[13];
                double aares = 0;

                try {
                    FileReader reader = new FileReader(targetFile2);
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

                        double s = xorExample.run(aa, resp, targetFile1.getAbsolutePath());
                        if ((1 - s) < (s - 0)) {
                            s = 1;
                        }
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
            if(targetFile1.getName().equals("kidney.snet")) {
                double[] bb = new double[15];
                double bbres = 0;

                try {
                    FileReader reader = new FileReader(targetFile2);
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

                        double s = xorExample.run(bb, resp, targetFile1.getAbsolutePath());
                        if ((1 - s) < (s - 0)) {
                            s = 1;
                        }
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
            return ratio*100+"%";
        }catch (Exception e){
            e.printStackTrace();

        }
        return "failed";
    }

    @RequestMapping(path = "/check", method = RequestMethod.POST)
    public String check(@RequestParam("file2") MultipartFile file2, HttpServletRequest req, HttpServletResponse resp) throws IOException{

        try{
            File targetFile2 = saveFile(file2);

            String res="success";

            boolean flag = true;

            try{
                File file=new File(targetFile2.getAbsolutePath());
                BufferedReader br=new BufferedReader(new FileReader(file));
                String line=br.readLine();
                while (line!=null)
                {
                    String[] argss=line.split(";");
                    for(int i=0;i<argss.length;i++)
                    {
                        if(argss[i].equals("?")){
                            flag = false;
                            break;
                        }
                    }
                    line=br.readLine();
                }
                if(flag){
                    res = "none";
                }
            }catch(Exception e){
                System.err.println("CheckError: " + e.getMessage());
            }

            if(!flag){
                BYS bb = new BYS();
                bb.bys(targetFile2.getAbsolutePath());

            }

            System.out.println(targetFile2.getName()+res);

            return res;
        }catch (Exception e){
            e.printStackTrace();
        }

        return "failed";
    }

    public static File saveFile(MultipartFile file) throws IOException{
        String path = System.getProperty("webapp");
        String fileName = file.getOriginalFilename();
        File targetFile = new File(path,fileName);
        FileUtils.copyInputStreamToFile(file.getInputStream(),targetFile);
        return targetFile;

    }
}
