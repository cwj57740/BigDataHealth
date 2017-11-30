package untitled3;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;

import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import org.joone.engine.*;
import org.joone.engine.learning.*;
import org.joone.io.*;
import org.joone.net.NeuralNet;
import org.joone.net.NeuralNetLoader;

/**
 * Example: The XOR Problem with JOONE
 *
 * @author Ming Sun
 * @version 1.0
 */
public class Kidney extends JFrame implements
ActionListener,NeuralNetListener {

FullSynapse t1,t2;


  public Kidney()
  {

  }

  /**
   * The main function, just display the JFrame.
   * 
   * @param args No arguments are used.
   */
  public static void main(String args[])
  {
    (new Kidney()).show(true);
  }

  /**
   * Called when the user clicks one of the three
   * buttons.
   * 
   * @param e The event.
   */
  public void actionPerformed(ActionEvent e)
  {

  }
  
  //���к����ڼ�������
  private static final long serialVersionUID = -3472219226214066504L;

    public static long circle = 0;

    //��¼ȫ�����
        public static double globalerror = 0;

  //�������Ա
  private NeuralNet nnet = null;
  //�����籣��·��
  private String NNet_Path = null;

  /**��ʼ�������� 
   * String NNet_Path ��������·��
   * int InputNum     �������Ԫ����
   * int HiddenNum    ���ز���Ԫ����
   * int OutputNum    �������Ԫ����
   * */
  public void Init_BPNN(String NNet_Path,int InputNum,int HiddenNum,int OutputNum) {
      //����������ı���·��
      this.NNet_Path = NNet_Path;
      //�½�����Layer���ֱ���Ϊ����㣬���ز㣬�����
      LinearLayer input = new LinearLayer();
      SigmoidLayer hidden = new SigmoidLayer();
      SigmoidLayer output = new SigmoidLayer();
      //����ÿ��Layer��������Ԫ����
      input.setRows(InputNum);
      hidden.setRows(HiddenNum);
      output.setRows(OutputNum);
      //�½�����ͻ�����������Ӹ���
      FullSynapse synapse_IH = new FullSynapse();
      FullSynapse synapse_HO = new FullSynapse();
      //��������-���أ�����-�������
      input.addOutputSynapse(synapse_IH);
      hidden.addInputSynapse(synapse_IH);
      hidden.addOutputSynapse(synapse_HO);
      output.addInputSynapse(synapse_HO);
      //�½�һ�������磬���������㣬���ز㣬�����
      nnet = new NeuralNet();
      nnet.addLayer(input, NeuralNet.INPUT_LAYER);
      nnet.addLayer(hidden, NeuralNet.HIDDEN_LAYER);
      nnet.addLayer(output, NeuralNet.OUTPUT_LAYER);
  }

  /**ʹ�ô����ļ�ѵ���½��������磬����Init_BPNN����ʹ��
   * String TrainFile ѵ���ļ����·��
   * int TrainLength  ѵ���ļ�������
   * double Rate      ������ѵ���ٶ�
   * double Momentum  ������ѵ������
   * int TrainCicles  ������ѵ������
   * */
  public void Train_BPNN(String TrainFile,int TrainLength,double Rate,double Momentum,int TrainCicles){   
      //��ȡ�����
      Layer input = nnet.getInputLayer();     
      //�½�����ͻ��
      FileInputSynapse trains = new FileInputSynapse();
      //���������ļ�
      trains.setInputFile(new File(TrainFile));
      //����ʹ�õ��������ļ���1��2��3��4����Ϊѵ��������
      trains.setAdvancedColumnSelector("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15");

      //��ȡ�����
      Layer output = nnet.getOutputLayer();
      //�½�����ͻ��
      FileInputSynapse target = new FileInputSynapse();
      //���������ļ�
      target.setInputFile(new File(TrainFile));
      //����ʹ�õ��������ļ���5��6����Ϊѵ����Ŀ��
      target.setAdvancedColumnSelector("16");

      //�½�ѵ��ͻ��
      TeachingSynapse trainer = new TeachingSynapse();
      //����ѵ��Ŀ��
      trainer.setDesired(target);

      //�������������ͻ��
      input.addInputSynapse(trains);
      //������������ͻ��
      output.addOutputSynapse(trainer);       
      //�����������ѵ��ͻ��
      nnet.setTeacher(trainer);

      //��ȡ������ļ�����
      Monitor monitor = nnet.getMonitor();
      //����ѵ������
      monitor.setLearningRate(Rate);
      //����ѵ������
      monitor.setMomentum(Momentum);
      //����������
      monitor.addNeuralNetListener(this);// ������
      //����ѵ�����ݸ�����������
      monitor.setTrainingPatterns(TrainLength);
      //����ѵ������
      monitor.setTotCicles(TrainCicles); 
      //��ѵ��ģʽ
      monitor.setLearning(true);
      //��ʼѵ��
      nnet.go();
  }
  
  /**ʹ�ô����ļ�����ѵ������������
   * String NNet_Path ��������·��
   * String OutFile   ���Խ�����·��    
   * String TestFile  ѵ���ļ����·��
   * int TestLength   ѵ���ļ�������
   * */
  public double Test_BPNN(String NNet_Path,double[] inputs, final HttpServletResponse response,int TestLength){
        NeuralNet testBPNN = this.restoreNeuralNet(NNet_Path);
        if (testBPNN != null) {

           Layer input = testBPNN.getInputLayer();

            Pattern pattern=new Pattern();

            Double[] doubles=new Double[15];

            pattern.setValues(inputs);
           // pattern.setValue(1,0.6);
            Vector vector1=new Vector();
            vector1.add(pattern);
            FileInputSynapse inputStream = new FileInputSynapse();
           inputStream.setInputPatterns(vector1);

            input.removeAllInputs();

            input.addInputSynapse(inputStream);

            System.out.println("asfafa==========");
            Layer output = testBPNN.getOutputLayer();

            final resultOut fileOutput = new resultOut();
            output.addOutputSynapse(fileOutput);

            double[] dou=new double[1];

            Monitor monitor = testBPNN.getMonitor();
            monitor.setTrainingPatterns(TestLength);
            monitor.setTotCicles(1);
            //�ر�ѵ��ģʽ
            monitor.setLearning(false);
            //��ʼ����
            testBPNN.go(false,true);

            System.out.println("size=" + output.getAllOutputs().size());
            System.out.println("test"+fileOutput.getResult());
            return fileOutput.getResult();
        }
        return -1;
    }

    /**�������е�������
   * String NNet_Path ��������·��
   * */
  NeuralNet Get_BPNN(String NNet_Path) {
      NeuralNetLoader loader = new NeuralNetLoader(NNet_Path);
      NeuralNet nnet = loader.getNeuralNet();
      return nnet;
  }
  /**
   * Called when the user clicks the run button.
   */
  public double run(double[] inputs,HttpServletResponse response,String NNet_Path)
      {
          System.out.println("in run");
    	  return  Test_BPNN(NNet_Path,inputs,response,1);
    	 // status.setText("Results written to " + resultFile.getText());
      }

  /**
   * Called when the user clicks the train button.
   */
  public void train(String NNet_Path,String trainFile,int inputsize,int hiddensize,double Rate,double Momentum,int TrainCicles)
    {
    	Init_BPNN(NNet_Path,inputsize ,hiddensize ,1);
    	Train_BPNN(trainFile ,400 ,Rate ,Momentum ,TrainCicles);
    }

  /**
   * JOONE Callback: called when the neural network
   * stops. Not used.
   * 
   * @param e The JOONE event
   */
  public void netStopped(NeuralNetEvent e) {
	  saveNeuralNet(System.getProperty("webapp")+"kidney.snet"); //
  }

  /**
   * JOONE Callback: called to update the progress
   * of the neural network. Used to update the
   * status line.
   * 
   * @param e The JOONE event
   */
  public void cicleTerminated(NeuralNetEvent e) {
    Monitor mon = (Monitor)e.getSource();
      circle = mon.getCurrentCicle()-1;
            if(circle!=0){
                System.out.println("circle="+ circle);
            }
      globalerror = mon.getGlobalError();
  }
  
  public void saveNeuralNet(String fileName) {  
      try {  
          FileOutputStream stream = new FileOutputStream(fileName);  
          ObjectOutputStream out = new ObjectOutputStream(stream);  
          out.writeObject(nnet);// д��nnet����  
          out.close();  
      } catch (Exception excp) {  
          excp.printStackTrace();  
      }  
  }  
    
  NeuralNet restoreNeuralNet(String fileName) {  
      NeuralNetLoader loader = new NeuralNetLoader(fileName);  
      NeuralNet nnet = loader.getNeuralNet();  
      return nnet;  
      } 

  /**
   * JOONE Callback: Called when the network
   * is starting up. Not used.
   * 
   * @param e The JOONE event.
   */
  public void netStarted(NeuralNetEvent e) {
  }

@Override
public void errorChanged(NeuralNetEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void netStoppedError(NeuralNetEvent arg0, String arg1) {
	// TODO Auto-generated method stub
	
}

}