package Algorithms.UniformCostSearch;

import Algorithms.Algorithm;
import Problems.Problem;

import java.util.ArrayList;

public class UCS_Graph extends Algorithm {

    ArrayList<Problem.State> openStates;
    ArrayList<Problem.State> closeStates;
    long start,end;

    public UCS_Graph(Problem problem) {
        super(problem);
        openStates = new ArrayList<>();
        closeStates = new ArrayList<>();
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

            if(problem.goalTest(s)) {
                showResult(s);
                return;
            }
            expandedNodesNo++;
            visitedNodesNo++;
            closeStates.add(s);

            ArrayList<Problem.Action> actions = problem.actions(s);
            for (Problem.Action a:actions) {
                Problem.State next = problem.result(s, a);
                next.updateState(s,next,problem.stepCost(s, next, a));

                if (!isExist(next)) {
                    openStates.add(next);
                }
            }
            maxMemoryUse = Math.max(maxMemoryUse , openStates.size() + closeStates.size());
        }
    }

    @Override
    public Problem.State getNextState() {
        int selected = -1;
        float bestGn = Integer.MAX_VALUE;
        for(int i = 0 ; i < openStates.size() ; i++){
            if(openStates.get(i).gn < bestGn){
                selected = i;
                bestGn = openStates.get(i).gn; //update bestGn
            }
        }
        return openStates.remove(selected);
    }

    public void showResult(Problem.State finalState){
        float[] temp = problem.bestPath(finalState);
        System.out.println("NODE NUMBER EXIST: " + temp[0]);
        System.out.println("PATH COST: " + temp[1]);
        System.out.println("MAX MEMORY USAGE: " + maxMemoryUse);
        System.out.println("EXPANDED NODES: "+closeStates.size());
        System.out.println("VISITED NODES: "+closeStates.size());

        end = System.currentTimeMillis();
        System.out.println("RUN TIME:" + (end - start) + " MS");
    }

    public boolean isExist(Problem.State next){
        for (int i = 0; i < closeStates.size(); i++) {
            Problem.State temp = closeStates.get(i);
            if (temp.equalStates(next))
                return true;
        }
        for (int i = 0; i < openStates.size(); i++) {
            Problem.State temp = openStates.get(i);
            if (temp.equalStates(next)) {
                if(next.gn < temp.gn) {
                    openStates.remove(i);
                    return false;
                }
                else
                    return true;
            }
        }
        return false;
    }
}
