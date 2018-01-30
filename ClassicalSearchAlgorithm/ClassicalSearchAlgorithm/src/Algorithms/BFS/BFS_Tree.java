package Algorithms.BFS;

import Algorithms.Algorithm;
import Problems.Problem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS_Tree extends Algorithm{

    Queue<Problem.State> openStates;
    long start,end;

    public BFS_Tree(Problem problem) {
        super(problem);
        openStates = new LinkedList<>();
        run();
    }

    @Override
    public void run() {
        start = System.currentTimeMillis();
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

            ArrayList<Problem.Action> actions = problem.actions(s);
            for (Problem.Action a:actions) {
                Problem.State next = problem.result(s, a);
                next.updateState(s,next,problem.stepCost(s,next,a));

                if(problem.goalTest(next)) {
                    showResult(next);
                    return;
                }

                openStates.add(next);
            }
            maxMemoryUse = Math.max(maxMemoryUse , openStates.size());
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
        System.out.println("EXPANDED NODES: "+ expandedNodesNo);
        System.out.println("VISITED NODES: "+ visitedNodesNo);

        end = System.currentTimeMillis();
        System.out.println("RUN TIME:" + (end - start) + " MS");
    }
}
