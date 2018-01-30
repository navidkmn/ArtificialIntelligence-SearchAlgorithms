package Algorithms.HillClimbing;

import Algorithms.Algorithm;
import Problems.Problem;

import java.util.ArrayList;

public class FirstChoiceHC extends Algorithm {

    public FirstChoiceHC(Problem problem) {
        super(problem);
        run();
    }

    @Override
    public void run() {
        visitedNodesNo = 0;
        expandedNodesNo = 0;
        boolean status = true;
        Problem.State currentState = problem.initialState();
        while (status){
            visitedNodesNo++;
            Problem.State tempState = null;
            ArrayList<Problem.State> neighbours = problem.neighbours(currentState);
            if(neighbours.size() == 0){
                trap();
                return;
            }
            for(int i = 0; i<neighbours.size(); i++) {
                if (neighbours.get(i).distanceFromGoal < currentState.distanceFromGoal) {
                    tempState = neighbours.get(i);
                    break;
                }
            }
            if(tempState == null){
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
