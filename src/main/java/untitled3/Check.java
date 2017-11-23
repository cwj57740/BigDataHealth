package untitled3;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class Check extends HttpServlet {

    private String message;
    //private untitled3.XorExample xorExamplexl;
    @Override
    public void init() throws ServletException {
        //xorExamplexl=new untitled3.XorExample();
        System.out.println("in init");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("afasdfdsaf");

        String file2=req.getParameter("file2");

        String filename2="/Users/MingSun/"+file2.substring(12);
        System.out.println(filename2);

        String res="success";

        boolean flag = true;

        try{
            File file=new File(filename2);
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
            bb.bys(filename2);

        }

        System.out.println(file2+res);

        OutputStream outputStream=resp.getOutputStream();
        byte[] bytes=res.getBytes("utf-8");
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

    public void destroy() {

        super.destroy();

    }

}