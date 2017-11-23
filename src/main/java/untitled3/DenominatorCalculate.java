package untitled3;

import java.util.List;

/**
 * @author alan-king 实现相关系数分母计算
 *
 */
public class DenominatorCalculate {

    //add denominatorCalculate method
    public double calculateDenominator(List<String> xList,List<String> yList){
        double standardDifference = 0.0;
        int size = xList.size();
        double xAverage = 0.0;
        double yAverage = 0.0;
        double xException = 0.0;
        double yException = 0.0;
        double temp = 0.0;
        for(int i=0;i<size;i++){
            temp += Double.parseDouble(xList.get(i));
        }
        xAverage = temp/size;

        temp = 0.0;
        for(int i=0;i<size;i++){
            temp += Double.parseDouble(yList.get(i));
        }
        yAverage = temp/size;

        for(int i=0;i<size;i++){
            xException += Math.pow(Double.parseDouble(xList.get(i))-xAverage,2);
            yException += Math.pow(Double.parseDouble(yList.get(i))-yAverage, 2);
        }
        //calculate denominator of
        return standardDifference = Math.sqrt(xException*yException);
    }
}