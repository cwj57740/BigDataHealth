package untitled3;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class Dong extends HttpServlet {

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

        int inputsize=Integer.valueOf(req.getParameter("sr"));

        String res=null;

        if(inputsize==13) {

            res = XorExample.circle +" " +"Global error:"+ XorExample.globalerror*100+"%";
        }
        else {
            res =" " + Kidney.circle +" " +"Global error:"+ Kidney.globalerror*100+"%";;
        }

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
//������Ӧ��������

    }

    @Override

    public void destroy() {

        super.destroy();

    }

}