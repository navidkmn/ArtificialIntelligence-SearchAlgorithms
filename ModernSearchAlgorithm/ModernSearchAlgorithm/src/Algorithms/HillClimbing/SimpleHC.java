package Algorithms.HillClimbing;

import Algorithms.Algorithm;
import Problems.EightQueen;
import Problems.Problem;

import java.util.ArrayList;

public class SimpleHC extends Algorithm {

    public SimpleHC(Problem problem) {
        super(problem);
        run();
    }

    @Override
    public void run() {
        boolean status = true;
        Problem.State currentState = problem.initialState();
        Problem.State tempState = null;
        while (status){
            visitedNodesNo++;
            float closestStateDistance = currentState.distanceFromGoal;
            ArrayList<Problem.State> neighbours = problem.neighbours(currentState);
            if(neighbours.size() == 0){
                trap();
                return;
            }
            for (int i = 0 ; i<neighbours.size(); i++){
                if(neighbours.get(i).distanceFromGoal < closestStateDistance){
                    closestStateDistance = neighbours.get(i).distanceFromGoal;
                    tempState = neighbours.get(i);
                }
            }
            if(closestStateDistance == currentState.distanceFromGoal){
                trap();
                return;
            }
            expandedNodesNo++;
            if(problem.goalTest(tempState)){
                getInfo(tempState);
                return;
            }
            currentState = tempState;
        }
    }

    @Override
    public Problem.State getNextState() {
        return null;
    }

    @Override
    public void getInfo(Problem.State state){
        problem.showInfo(state);
        System.out.println("Visited Nodes: " + visitedNodesNo);
        System.out.println("Expanded Nodes " + expandedNodesNo);
    }

    @Override
    public void trap() {
        System.out.println("We Are In Trap!!!");
    }
}
