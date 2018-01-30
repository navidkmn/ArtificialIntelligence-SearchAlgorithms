package Algorithms;

import Problems.Problem;

public abstract class Algorithm {

    public Problem problem;
    public int visitedNodesNo;
    public int expandedNodesNo;

    public Algorithm(Problem problem){
        this.problem = problem;
        visitedNodesNo = 0;
        expandedNodesNo = 0;
    }

    public abstract void run();
    public abstract Problem.State getNextState();
    public abstract void getInfo(Problem.State state);
    public abstract void trap();
}
