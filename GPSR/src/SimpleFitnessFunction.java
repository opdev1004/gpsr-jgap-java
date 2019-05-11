import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.terminal.Variable;

public class SimpleFitnessFunction extends GPFitnessFunction {

	private Float[] input;
	private Float[] output;
	private Variable _xVariable;
	
	public SimpleFitnessFunction (Float[] input, Float[] output, Variable _xVariable) {
		this.input = input;
		this.output = output;
		this._xVariable = _xVariable;
	}
	
	@Override
	protected double evaluate(final IGPProgram program) {
		double result = 0.0f;
		Object[] NO_ARGS = new Object[0];
		
		for(int i = 0; i < input.length; i++) {
			_xVariable.set(input[i]);
			double value = program.execute_float(0, NO_ARGS);
			result += Math.abs(value - output[i]);
		}
		return result;
	}

	
}
