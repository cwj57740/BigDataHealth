package untitled3;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloWorld extends HttpServlet {

    private String message;
    private XorExample xorExamplexl;
    private Kidney kid;
    @Override
    public void init() throws ServletException {
        xorExamplexl=new XorExample();
        kid=new Kidney();
        System.out.println("in init");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                                System.out.println("afasdfdsaf");

        String file1=req.getParameter("file1");

        String filename1="/Users/MingSun/"+file1.substring(12);
        System.out.println(filename1);

        String file2=req.getParameter("file2");

        String filename2="/Users/MingSun/"+file2.substring(12);
        System.out.println(filename2);


        int inputsize=Integer.valueOf(req.getParameter("sr"));

        int hiddensize=Integer.valueOf(req.getParameter("yc"));

        int outsize=1;

        System.out.println(inputsize+"");

        double Rate=Double.valueOf(req.getParameter("xlsl"));

        double Momentum=Double.valueOf(req.getParameter("xldl"));

        int circles=Integer.valueOf(req.getParameter("xlcs"));

        if((file1.substring(12).equals("heart.snet"))) {

            xorExamplexl.train(filename1, filename2, inputsize, hiddensize, Rate, Momentum, circles);
        }
        else {
             kid.train(filename1, filename2, inputsize, hiddensize, Rate, Momentum, circles);
        }

    }

    @Override

    public void destroy() {

        super.destroy();

    }

}