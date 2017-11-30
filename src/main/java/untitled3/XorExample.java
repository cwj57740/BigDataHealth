package untitled3;

import javax.servlet.http.HttpServletResponse;

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
public class XorExample implements NeuralNetListener {

FullSynapse t1,t2;

  /**
   * The train button.
   */


  /**
   * Constructor. Set up the components.
   */
  public XorExample()
  {

  }
  /**
   * Called when the user clicks one of the three
   * buttons.
   * 
   * @param e The event.
   */
  public void actionPerformed(ActionEvent e)
  {
     // run();
  }
  
  //序列号用于检查兼容性
  private static final long serialVersionUID = -3472219226214066504L;
     //记录剩余训练轮数
    public static long circle = 0;
    //记录全局误差
    public static double globalerror = 0;
    //神经网络成员
  private NeuralNet nnet = null;
  //神经网络保存路径
  private String NNet_Path = null;

    /**初始化神经网络
       * String NNet_Path 神经网络存放路径
       * int InputNum     输入层神经元个数
       * int HiddenNum    隐藏层神经元个数
       * int OutputNum    输出层神经元个数
       * */
  public void Init_BPNN(String NNet_Path,int InputNum,int HiddenNum,int OutputNum) {
      //设置新网络的保存路径
      this.NNet_Path = NNet_Path;
      //新建三个Layer，分别作为输入层，隐藏层，输出层
      LinearLayer input = new LinearLayer();
      SigmoidLayer hidden = new SigmoidLayer();
      SigmoidLayer output = new SigmoidLayer();
      //设置每个Layer包含的神经元个数
      input.setRows(InputNum);
      hidden.setRows(HiddenNum);
      output.setRows(OutputNum);
      //新建两条突触，用于连接各层
      FullSynapse synapse_IH = new FullSynapse();
      FullSynapse synapse_HO = new FullSynapse();
      //连接输入-隐藏，隐藏-输出各层
      input.addOutputSynapse(synapse_IH);
      hidden.addInputSynapse(synapse_IH);
      hidden.addOutputSynapse(synapse_HO);
      output.addInputSynapse(synapse_HO);
      //新建一个神经网络，并添加输入层，隐藏层，输出层
      nnet = new NeuralNet();
      nnet.addLayer(input, NeuralNet.INPUT_LAYER);
      nnet.addLayer(hidden, NeuralNet.HIDDEN_LAYER);
      nnet.addLayer(output, NeuralNet.OUTPUT_LAYER);
  }

    /**使用磁盘文件训练新建的神经网络，需与Init_BPNN搭配使用
       * String TrainFile 训练文件存放路径
       * int TrainLength  训练文件的行数
       * double Rate      神经网络训练速度
       * double Momentum  神经网络训练动量
       * int TrainCicles  神经网络训练次数
       * */
  public void Train_BPNN(String TrainFile,int TrainLength,double Rate,double Momentum,int TrainCicles){   
      //获取输入层
      Layer input = nnet.getInputLayer();     
      //新建输入突触
      FileInputSynapse trains = new FileInputSynapse();
      //设置输入文件
      trains.setInputFile(new File(TrainFile));
      //设置使用的列数，文件第1，2，3，4,5,6,7,8,9,10,11,12,13列作为训练的输入
      trains.setAdvancedColumnSelector("1,2,3,4,5,6,7,8,9,10,11,12,13");

      //获取输出层
      Layer output = nnet.getOutputLayer();
      //新建输入突触
      FileInputSynapse target = new FileInputSynapse();
      //设置输入文件
      target.setInputFile(new File(TrainFile));
      //设置使用的列数，文件第14列作为训练的目标
      target.setAdvancedColumnSelector("14");

      //新建训练突触
      TeachingSynapse trainer = new TeachingSynapse();
      //设置训练目标
      trainer.setDesired(target);

      //添加输入层的输入突触
      input.addInputSynapse(trains);
      //添加输出层的输出突触
      output.addOutputSynapse(trainer);       
      //设置神经网络的训练突触
      nnet.setTeacher(trainer);
      //获取神经网络的监视器
      Monitor monitor = nnet.getMonitor();
      //设置训练速率
      monitor.setLearningRate(Rate);
      //设置训练动量
      monitor.setMomentum(Momentum);
      //新增监听者
      monitor.addNeuralNetListener(this);//输入流
      //设置训练数据个数（行数）
      monitor.setTrainingPatterns(TrainLength);
      //设置训练次数
      monitor.setTotCicles(TrainCicles);

      //打开训练模式
      monitor.setLearning(true);

      //开始训练
      nnet.go();
  }
  
    /**使用磁盘文件测试训练过的神经网络
       * String NNet_Path 神经网络存放路径
       * String OutFile   测试结果存放路径
       * String TestFile  训练文件存放路径
       * int TestLength   训练文件的行数
       * */
  public double Test_BPNN(String NNet_Path,double[] inputs, final HttpServletResponse response,int TestLength){
      NeuralNet testBPNN = this.restoreNeuralNet(NNet_Path);
      if (testBPNN != null) { 

         Layer input = testBPNN.getInputLayer();

          Pattern pattern=new Pattern();

          Double[] doubles=new Double[13];

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
          //关闭训练模式
          monitor.setLearning(false);
          //开始测试
          testBPNN.go(false,true);

          System.out.println("size=" + output.getAllOutputs().size());
          System.out.println("test"+fileOutput.getResult());
          return fileOutput.getResult();
      }
      return -1;
  }
    //开始预测
    public double startPredict(String NNet_Path,double[] inputs,int TestLength){
        NeuralNet testBPNN = this.restoreNeuralNet(NNet_Path);
        if (testBPNN != null) {

            Layer input = testBPNN.getInputLayer();
            Pattern pattern=new Pattern();
            pattern.setValues(inputs);
            Vector vector1=new Vector();
            vector1.add(pattern);
            FileInputSynapse inputStream = new FileInputSynapse();
            inputStream.setInputPatterns(vector1);
            input.removeAllInputs();
            input.addInputSynapse(inputStream);
            Layer output = testBPNN.getOutputLayer();
            final resultOut fileOutput = new resultOut();
            output.addOutputSynapse(fileOutput);
            double[] dou=new double[1];
            Monitor monitor = testBPNN.getMonitor();
            monitor.setTrainingPatterns(TestLength);
            monitor.setTotCicles(1);
            monitor.setLearning(false);
            testBPNN.go(false,true);
            System.out.println("size=" + output.getAllOutputs().size());
            System.out.println("test"+fileOutput.getResult());
            return fileOutput.getResult();
        }
        return -1;
    }

    /**读入已有的神经网络
       * String NNet_Path 神经网络存放路径
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
  	Train_BPNN(trainFile ,297 ,Rate ,Momentum ,TrainCicles);
  }

  /**
   * JOONE Callback: called when the neural network
   * stops. Not used.
   * 
   * @param e The JOONE event
   */
  public void netStopped(NeuralNetEvent e) {
	  saveNeuralNet(System.getProperty("webapp")+"heart.snet");
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
    //long cl = c / 1000;
    // print the results every 1000 cycles

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
