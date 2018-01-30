package Algorithms.BFS;

import Algorithms.Algorithm;
import Problems.Problem;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS_Graph extends Algorithm{

    Queue<Problem.State> openStates;
    ArrayList<Problem.State> closeStates;

    public BFS_Graph(Problem problem) {
        super(problem);
        openStates = new LinkedList<>();
        closeStates = new ArrayList<>();
        run();
    }

    @Override
    public void run() {
        Problem.State s = problem.initialState();
        openStates.add(s);
        maxMemoryUse++;

        if(problem.goalTest(s)) {
            showResult(s);
            return;
        }

        while (!openStates.isEmpty()){
            s = getNextState();

            expandedNodesNo++;
            visitedNodesNo++;
            closeStates.add(s);

            ArrayList<Problem.Action> actions = problem.actions(s);

            for (Problem.Action a:actions) {
                Problem.State next = problem.result(s, a);
                next.updateState(s,next,problem.stepCost(s,next,a));

                if(problem.goalTest(next)) {
                    showResult(next);
                    return;
                }
                if (!isExist(next)) {
                    openStates.add(next);
                }
            }
            maxMemoryUse = Math.max(maxMemoryUse , openStates.size() + closeStates.size());
        }
    }

    @Override
    public Problem.State getNextState() {
        return openStates.remove();
    }

    public void showResult(Problem.State finalState){
        float[] temp = problem.bestPath(finalState);
        System.out.println("NODE NUMBER EXIST: " + temp[0]);
        System.out.println("PATH COST: " + temp[1]);
        System.out.println("MAX MEMORY USAGE: " + maxMemoryUse);
        System.out.println("EXPANDED NODES: "+closeStates.size());
        System.out.println("VISITED NODES: "+closeStates.size());
    }

    public boolean isExist(Problem.State next){
        for (int i = 0; i < closeStates.size(); i++) {
            Problem.State temp = closeStates.get(i);
            if (temp.equalStates(next))
                return true;
        }
        return false;
    }
}
