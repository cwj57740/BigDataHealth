package api.personalDiagnosis;

import com.diagnosis.mySql;

import java.sql.Connection;
import java.sql.ResultSet;

public class Util {
    public static String getHospitalName(Connection conn,int id){
        String name = null;
        try {
            mySql m = new mySql();

            String sql = "select name from hospital_info where hospital_id='"+id+"'";
            ResultSet rs = m.selectData(conn, sql);
            if(rs.next()){
                name = rs.getString("name");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return name;
    }

}
