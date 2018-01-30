package Algorithms;

import Problems.Problem;
import Problems.Problem1;

import java.util.ArrayList;

public abstract class Algorithm {

    public Problem problem;
    public int visitedNodesNo;
    public int expandedNodesNo;
    public int maxMemoryUse;

    public Algorithm(Problem problem){
        this.problem = problem;
        visitedNodesNo = 0;
        expandedNodesNo = 0;
        maxMemoryUse = 0;
    }

    public abstract void run();

    public abstract Problem.State getNextState();
}
