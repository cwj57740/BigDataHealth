package api.personalDiagnosis;

import com.alibaba.fastjson.JSON;
import com.diagnosis.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/personalDiagnosis")
public class PersonalDiagnosisController {

    private mySql ml=new mySql();


    @RequestMapping("/registerUser")
    public String registerUser(HttpServletRequest request){
        try{
            Connection conn=ml.getConnection();

            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String address = request.getParameter("address");
            MapDistance mapDis=new MapDistance();
            Map patientInfo=new HashMap();
            //将地址转换为经纬度
            Map<String,Double> addressMap = mapDis.getLngAndLat(address);
            patientInfo.put("name", name);
            patientInfo.put("longitude", addressMap.get("lng"));
            patientInfo.put("latitude", addressMap.get("lat"));
            patientInfo.put("password",password);
            patient pt=new patient();
            System.out.println("注册新用户：");
            int patient_id = pt.inputPatientInfo(patientInfo, conn);
            return Integer.toString(patient_id);

        } catch (Exception e){
            e.printStackTrace();
            return "failed";
        }
    }

    @RequestMapping("/registerHospital")
    public String registerHospital(HttpServletRequest request){
        try{
            Connection conn = ml.getConnection();
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String level = request.getParameter("level");
            MapDistance mapDis=new MapDistance();
            Map<String,Double> addressMap=mapDis.getLngAndLat(address);
            Map hospitalInfo=new HashMap();
            hospitalInfo.put("name", name);
            hospitalInfo.put("longitude", addressMap.get("lng"));
            hospitalInfo.put("latitude", addressMap.get("lat"));
            hospitalInfo.put("level", level);
            hospital h=new hospital();
            int disease_id = h.inputInfo(hospitalInfo, conn);
            return Integer.toString(disease_id);
        } catch (Exception e){
            e.printStackTrace();
            return "failed";
        }
    }

    @RequestMapping("/diagnosis")
    public String diagnosis(HttpServletRequest request, HttpServletResponse response){
        try{
            Connection conn = ml.getConnection();
            Diagnosis diagnosis = new Diagnosis();
            String symptom = request.getParameter("symptom");
            System.out.println(symptom);
            ArrayList<Map<String, String>> arrayList = diagnosis.fullDiagnosis(symptom, conn);
            response.setContentType("application/json; charset=UTF-8");
            return JSON.toJSONString(arrayList);
        }catch (Exception e){
            e.printStackTrace();
            return "failed";
        }
    }

    @RequestMapping("/recommendHospital")
    public String recommendHospital(HttpServletRequest request){
        try {
            Connection conn = ml.getConnection();
            String patient_id = request.getParameter("patient_id");
            String disease_id = "5";
            double range=15;
            evaluater ev=new evaluater();
            ArrayList<Map.Entry<Integer, Double>>  CFScoreMap=new ArrayList<>();
            CFScoreMap=ev.CFEvaluator(Integer.parseInt(patient_id),Integer.parseInt(disease_id), range, conn);

            List<Map> resultList = new ArrayList<>();
            for(int index=0;index<CFScoreMap.size();++index){
                Map<String,String> map = new HashMap<>();
                map.put("index",CFScoreMap.get(index).getKey().toString());
                map.put("score",CFScoreMap.get(index).getValue().toString());
                map.put("name",Util.getHospitalName(conn,CFScoreMap.get(index).getKey()));
                System.out.println("医院编号:"+CFScoreMap.get(index).getKey()+";评分:"+CFScoreMap.get(index).getValue());
                resultList.add(map);
            }
            return JSON.toJSONString(resultList);
        } catch (Exception e){
            e.printStackTrace();
            return "failed";
        }
    }
}
