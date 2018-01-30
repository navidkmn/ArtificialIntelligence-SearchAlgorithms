package Algorithms.DFS.limitDepth;

import Algorithms.Algorithm;
import Problems.Problem;

import java.util.ArrayList;
import java.util.Stack;

public class DFS_limitDepth_Tree extends Algorithm {

    Stack<Problem.State> openStates;
    int limitDepth;

    public DFS_limitDepth_Tree(Problem problem,int limitDepth) {
        super(problem);
        openStates = new Stack();
        this.limitDepth = limitDepth;
        run();
    }

    @Override
    public void run() {
        Problem.State s = problem.initialState();
        openStates.push(s);
        maxMemoryUse++;

        if(problem.goalTest(s)) {
            showResult(s);
            return;
        }

        while (!openStates.isEmpty()) {
            s = getNextState();

            if (s.levelNo < limitDepth) {
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

                    openStates.push(next);
                }
                maxMemoryUse = Math.max(maxMemoryUse , openStates.size());
            }
        }
        System.out.println("NO PATH FOUND!!!");
    }

    @Override
    public Problem.State getNextState() {
        return openStates.pop();
    }

    public void showResult(Problem.State finalState){
        float[] temp = problem.bestPath(finalState);
        System.out.println("NODE NUMBER EXIST: " + temp[0]);
        System.out.println("PATH COST: " + temp[1]);
        System.out.println("MAX MEMORY USAGE: " + maxMemoryUse);
        System.out.println("EXPANDED NODES: "+ expandedNodesNo);
        System.out.println("VISITED NODES: "+ visitedNodesNo);
    }
}
