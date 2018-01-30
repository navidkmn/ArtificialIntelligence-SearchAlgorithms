import Algorithms.GA;
import Algorithms.HillClimbing.FirstChoiceHC;
import Algorithms.HillClimbing.RandomRestartHC;
import Algorithms.HillClimbing.SimpleHC;
import Algorithms.HillClimbing.StochasticHC;
import Algorithms.SA;
import Problems.EightQueen;
import Problems.EquationSolving;
import Problems.GraphPartitioning;
import Problems.Problem;

public class main {

    public static void main (String[] args){
        EquationSolving equationSolving = new EquationSolving();
        GA ga = new GA(equationSolving);
    }
}
