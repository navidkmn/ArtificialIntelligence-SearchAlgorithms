package Algorithms;

import Problems.EquationSolving;
import Problems.Problem;
import org.omg.PortableServer.POA;

public class GA {

    EquationSolving es;

    public GA(EquationSolving equationSolving) {
        this.es = equationSolving;
        run();
    }

    public void run() {
        EquationSolving.State s = es.initialState();
        es.update(s);
        int run = es.goalTest(s);
        int counter = 1;
        while (run == -1){
            s = es.nextGen(s);
            run = es.goalTest(s);
            counter++;
        }
        es.showInfo(s,run);
        System.out.println(counter);
    }

    public EquationSolving.State getNextState() {
        return null;
    }

    public void getInfo(Problem.State state) {

    }

    public void trap() {
        System.out.println("We Are In Trap!!!");
    }
}