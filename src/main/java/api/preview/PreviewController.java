package api.preview;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/preview")
public class PreviewController {

    private static double [][] a;
    private static double [] b;
    private static double [] s;
    @RequestMapping("/api")
    public String d(HttpServletRequest request){

        try {
            int city_number = Integer.parseInt(request.getParameter("city_number"));
            a = new double[city_number][city_number];
            b = new double[city_number];
            s = new double[city_number];
            int t = Integer.parseInt(request.getParameter("time"));
            List<Double[]> matrix = JSON.parseArray(request.getParameter("matrix"),Double[].class);
            List<Double> populations = JSON.parseArray(request.getParameter("population"),Double.class);
            List<Double> prevalence = JSON.parseArray(request.getParameter("prevalence"), Double.class);
            for(Double[] doubles : matrix){
                int i = 0;
                for(double d:doubles){
                    int j = 0;
                    a[i][j]=d;
                    System.out.println("a["+i+"]"+i+"["+j+"]="+a[i][j]);
                    j++;
                }
                i++;
            }
            for(int i = 0;i<city_number;i++){
                b[i] = populations.get(i);
                System.out.println("b["+i+"]="+b[i]);
            }
            for(int i = 0;i<prevalence.size();i++){
                s[i] = prevalence.get(i);
                System.out.println("s["+i+"]="+s[i]);
            }

//        Network c = new Network(b, a, city_number);
//        Map<String, Map> resultMap = new HashMap<>();
//        Map<String, Map> map = c.getResult();
//        for (int i=0;i<city_number;i++)
//            c.infect(i, s[i]);
//
//        for (int i=0;i<t;i++){
//            c.UpdateFor(1);
//            map = c.getResult();
//        }
        }catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }
}
