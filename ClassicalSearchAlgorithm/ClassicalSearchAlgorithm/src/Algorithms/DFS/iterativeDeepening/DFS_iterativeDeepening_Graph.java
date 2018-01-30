package Algorithms.DFS.iterativeDeepening;

import Algorithms.Algorithm;
import Problems.Problem;

import java.util.ArrayList;
import java.util.Stack;

public class DFS_iterativeDeepening_Graph extends Algorithm {

    final int MAX_LIMIT_DEPTH = 20;
    Stack<Problem.State> openStates;
    ArrayList<Problem.State> closeStates;
    int limitDepth;

    public DFS_iterativeDeepening_Graph(Problem problem, int limitDepth) {
        super(problem);
        openStates = new Stack();
        closeStates = new ArrayList<>();
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
                        openStates.push(next);
                    }
                }
                maxMemoryUse = Math.max(maxMemoryUse , openStates.size() + closeStates.size());
            }
        }
        if(++limitDepth <= MAX_LIMIT_DEPTH){
            limitDepth++;
            openStates = new Stack();
            closeStates = new ArrayList<>();
            run();
        }
    }

    @Override
    public Problem.State getNextState() {
        return openStates.pop();
    }

    public void showResult(Problem.State finalState){
        float[] temp = problem.bestPath(finalState);
        System.out.println("NODE NUMBER EXIST: " + temp[0]);
        System.out.println("PATH COST: " + temp[1]);
        System.out.println("DEPTH: " + limitDepth);
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

        for (int i = 0; i < openStates.size(); i++) {
            Problem.State temp = openStates.get(i);
            if (temp.equalStates(next))
                return true;
        }
        return false;
    }
}
