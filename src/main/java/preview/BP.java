package preview;

import java.util.Random;

public class BP {
	private final double[] input;
	private final double[] hidden;
	private final double[] output;
	private final double[] target;
	private final double[] hidDelta;
	private final double[] optDelta;
	private final double eta;
	private final double momentum;
	private final double[][] iptHidWeights;
	private final double[][] hidOptWeights;
	private final double[][] iptHidPrevUptWeights;
	private final double[][] hidOptPrevUptWeights;
	public double optErrSum = 0d;
	public double hidErrSum = 0d;
	private final Random random;
	
	public BP(int inputSize, int hiddenSize, int outputSize, double eta, double momentum)
	{
		input = new double[inputSize + 1];
		hidden = new double[hiddenSize + 1];
		output = new double[outputSize + 1];
		target = new double[outputSize + 1];
		
		hidDelta = new double[hiddenSize + 1];
		optDelta = new double[outputSize + 1];
		
		iptHidWeights = new double[inputSize + 1][hiddenSize + 1];
		hidOptWeights = new double[hiddenSize + 1][outputSize + 1];
		
		random = new Random(19881211);
		randomizeWeights(iptHidWeights);
		randomizeWeights(hidOptWeights);
		
		iptHidPrevUptWeights = new double[inputSize + 1][hiddenSize + 1];
		hidOptPrevUptWeights = new double[hiddenSize + 1][outputSize + 1];
		
		this.eta = eta;
		this.momentum = momentum;
	}
	
	private void randomizeWeights(double[][] matrix)
	{
		for(int i = 0, len = matrix.length; i != len; i++)
			for(int j = 0, len2 = matrix[i].length; j != len2; j++)
			{
				double real = random.nextDouble();
				matrix[i][j] = random.nextDouble() > 0.5 ? real : -real;
			}
	}
	
	public BP(int inputSize, int hiddenSize, int outputSize)
	{
		this(inputSize,hiddenSize,outputSize,0.25,0.9);
	}
	
	public void train(double[] trainData, double[] target)
	{
		loadInput(trainData);
		loadTarget(target);
		forward();
		calculateDelta();
		adjustWeight();
	}
	
	public double[] test(double[] inData)
	{
		if(inData.length != input.length-1)
		{
			throw new IllegalArgumentException("Size Do Not Match.");
		}
		System.arraycopy(inData, 0, input, 1, inData.length);
		forward();
		return getNetworkOutput();
	}
	
	private double[] getNetworkOutput()
	{
		int len = output.length;
		double[] temp = new double[len - 1];
		for(int i = 1; i != len; i++)
		{
			temp[i-1] = output[i];
		}
		return temp;
	}
	
	private void loadTarget(double[] arg)
	{
		if(arg.length != target.length-1)
		{
			throw new IllegalArgumentException("Size Do Not Match.");
		}
		System.arraycopy(arg, 0, target, 1, arg.length);
	}
	
	private void loadInput(double[] inData)
	{
		if(inData.length != input.length-1)
		{
			throw new IllegalArgumentException("Size Do Not Match.");
		}
		System.arraycopy(inData, 0, input, 1, inData.length);
	}
	
	private void forward(double[] layer0, double[] layer1, double[][] weight)
	{
		layer0[0] = 1.0;
		for(int j = 1, len = layer1.length; j != len; ++j)
		{
			double sum = 0;
			for(int i = 0, len2 = layer0.length; i != len2; ++i)
			{
				sum += weight[i][j] * layer0[i];
			}
			layer1[j] = sigmoid(sum);
		}
	}
	
	private void forward()
	{
		forward(input,hidden,iptHidWeights);
		forward(hidden,output,hidOptWeights);
	}
	
	private void outputErr()
	{
		double errSum = 0;
		for(int idx = 1, len = optDelta.length; idx != len; ++idx)
		{
			double o = output[idx];
			optDelta[idx] = o * (1d-o) * (target[idx]-o);
			errSum += Math.abs(optDelta[idx]);
		}
		optErrSum = errSum;
	}
	
	private void hiddenErr()
	{
		double errSum = 0;
		for(int j = 1, len = hidDelta.length; j != len; ++j)
		{
			double o = hidden[j];
			double sum = 0;
			for(int k = 1, len2 = optDelta.length; k != len2; ++k)
			{
				sum += hidOptWeights[j][k] * optDelta[k];
			}
			hidDelta[j] = o * (1d-o) * sum;
			errSum += Math.abs(hidDelta[j]);
		}
		hidErrSum = errSum;
	}
	
	private void calculateDelta()
	{
		outputErr();
		hiddenErr();
	}
	
	private void adjustWeight(double[] delta, double[] layer, double[][] weight, double[][] prevWeight)
	{
		layer[0] = 1;
		for(int i = 1, len = delta.length; i != len; ++i)
		{
			for(int j = 1, len2 = layer.length; j != len2; ++j)
			{
				double newVal = momentum * prevWeight[j][i] + eta * delta[i] * layer[j];
				weight[j][i] += newVal;
				prevWeight[j][i] = newVal;
			}
		}
	}
	
	private void adjustWeight()
	{
		adjustWeight(optDelta, hidden, hidOptWeights, hidOptPrevUptWeights);
		adjustWeight(hidDelta, input, iptHidWeights, iptHidPrevUptWeights);
	}
	
	private double sigmoid(double val)
	{
		return 1d / (1d + Math.exp(-val));
	}
}
