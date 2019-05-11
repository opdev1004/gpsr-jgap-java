import java.io.InputStreamReader;
import java.util.Scanner;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPProblem;
import org.jgap.gp.function.Add;
import org.jgap.gp.function.Divide;
import org.jgap.gp.function.Multiply;
import org.jgap.gp.function.Pow;
import org.jgap.gp.function.Subtract;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;

public class GPSR extends GPProblem {
	  // private Double[] inputD = {-2.00, -1.75, -1.50, -1.25, -1.00, -0.75, -0.50, -0.25, 0.00, 0.25, 0.50, 0.75, 1.00, 1.25, 1.50, 1.75, 2.00, 2.25, 2.50, 2.75};
	  // private Double[] outputD = {37.00000, 24.16016, 15.06250, 8.91016, 5.00000, 2.72266, 1.56250, 1.09766, 1.00000, 1.03516, 1.06250, 1.03516, 1.00000, 1.09766, 1.56250, 2.72266, 5.00000, 8.91016, 15.06250, 24.16016};
	  private Float[] input = new Float[20];
	  private Float[] output = new Float[20];
	  private Variable _xVariable;
	  // max number of evolution.
	  private final static int maxEvo = 1000;
	  
	  public GPSR() throws InvalidConfigurationException{
		  super(new GPConfiguration());
		  
		  String file = "regression.txt";
		  Scanner scan = new Scanner(new InputStreamReader(ClassLoader.getSystemResourceAsStream(file)));
		  scan.nextLine();
		  scan.nextLine();
		  int p = 0;
		  while(scan.hasNextFloat()) {
			  input[p] = scan.nextFloat();
			  output[p] = scan.nextFloat();
			  p++;
		  }
		  
		  for(int i = 0; i < input.length; i++) {
			  System.out.println("input: " + input[i] + ", output: " + output[i]);
		  }
		  
		  GPConfiguration config = getGPConfiguration();
		  _xVariable = Variable.create(config, "X", CommandGene.FloatClass);
		  config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
		  config.setMaxInitDepth(4);
		  config.setPopulationSize(1000);
	      config.setMaxCrossoverDepth(8);
	      config.setFitnessFunction(new SimpleFitnessFunction(input, output, _xVariable));
	      config.setStrictProgramCreation(true);
	  }
	  
	  
	  @Override
	  public GPGenotype create() throws InvalidConfigurationException {
		  GPConfiguration config = getGPConfiguration();
	      // The return type of the GP program.
	      Class[] types = { CommandGene.FloatClass };
	      Class[][] argTypes = {{}};
	      CommandGene[][] nodeSets = {
	    		  {
	    			  _xVariable,
	    			  new Add(config, CommandGene.FloatClass),
	    			  new Multiply(config, CommandGene.FloatClass),
	    			  new Divide(config, CommandGene.FloatClass),
	    			  new Subtract(config, CommandGene.FloatClass),
	    			  //new Exp(config, CommandGene.FloatClass),
	    			  //new Sine(config, CommandGene.FloatClass),
	    			  //new Cosine(config, CommandGene.FloatClass),
	    			  //new Tangent(config, CommandGene.FloatClass),
	    			  new Pow(config, CommandGene.FloatClass),
	    			  new Terminal(config, CommandGene.FloatClass, 0, 2.0, true)
	              }
	      };
	      GPGenotype result = GPGenotype.randomInitialGenotype(config, types, argTypes, nodeSets, 20, true);

	      return result;
	  }
	  
	  public static void main(String[] args) throws Exception {
		  GPProblem problem = new GPSR();
	      GPGenotype gp = problem.create();
	      gp.setVerboseOutput(true);
	      gp.outputSolution(gp.getAllTimeBest());
	      double fitness = -1.0d;
		  for (int i = 1; i < maxEvo; i++) {
		    	System.out.println("Evolution: " + i);
	            gp.evolve();
	            gp.calcFitness();
	            fitness = gp.getAllTimeBest().getFitnessValue();
	            // if the fitness is small enough stop the evolution
	            if (fitness < 0.0001d) break;
	       }
	      System.out.println("==========Finished============");
	      gp.outputSolution(gp.getAllTimeBest());
	  }
}
