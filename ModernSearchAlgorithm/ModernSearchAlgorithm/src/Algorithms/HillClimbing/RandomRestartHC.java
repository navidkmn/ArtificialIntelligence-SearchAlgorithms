package Algorithms.HillClimbing;

import Algorithms.Algorithm;
import Problems.Problem;

import java.util.ArrayList;

public class RandomRestartHC extends Algorithm {

    int tryNo;
    public RandomRestartHC(Problem problem , int tryNo) {
        super(problem);
        this.tryNo = tryNo;
        run();
    }

    @Override
    public void run() {
        visitedNodesNo = 0;
        expandedNodesNo = 0;
        boolean status = true;
        Problem.State currentState = problem.initialState();
        Problem.State tempState = null;
        while (status){
            visitedNodesNo++;
            float closestStateDistance = currentState.distanceFromGoal;
            ArrayList<Problem.State> neighbours = problem.neighbours(currentState);
            if(neighbours.size() == 0){
                if(--tryNo == 0) {
                    trap();
                    return;
                }
                else {
                    break;
                }
            }
            for (int i = 0 ; i< neighbours.size(); i++){
                if(neighbours.get(i).distanceFromGoal < closestStateDistance){
                    closestStateDistance = neighbours.get(i).distanceFromGoal;
                    tempState = neighbours.get(i);
                }
            }
            if(closestStateDistance == currentState.distanceFromGoal){
                if(--tryNo == 0) {
                    trap();
                    return;
                }
                else {
                    break;
                }
            }
            expandedNodesNo++;
            if(problem.goalTest(tempState)){
                getInfo(tempState);
                return;
            }
            currentState = tempState;
        }
        run();
    }

    @Override
    public Problem.State getNextState() { return null;}

    @Override
    public void getInfo(Problem.State state) {
        problem.showInfo(state);
        System.out.println("Visited Nodes: " + visitedNodesNo);
        System.out.println("Expanded Nodes " + expandedNodesNo);
    }

    @Override
    public void trap() {
        System.out.println("We Are In Trap!!!");
    }
}
