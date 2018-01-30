package Algorithms;

import Problems.Problem;

import java.util.ArrayList;
import java.util.Random;

public class SA extends Algorithm {

    float t = 1.0f;
    float alpha = 0.99f;
    float tn = 0.001f;
    float t0 = 1.0f;
    float n = 20000;

    int counter = 1;

    public SA(Problem problem) {
        super(problem);
        run();
    }

    @Override
    public void run() {
        visitedNodesNo = 0;
        expandedNodesNo = 0;
        boolean status = true;
        Problem.State currentState = problem.initialState();
        Problem.State tempState = null;
        while (status && counter < n){
            visitedNodesNo++;
            if(problem.goalTest(currentState)){
                getInfo(currentState.parrent);
                return;
            }
            ArrayList<Problem.State> neighbours = problem.neighbours(currentState);
            if(neighbours.size() == 0){
                trap();
                return;
            }
            expandedNodesNo++;
            while (true){
                int random = (int)(Math.random()*neighbours.size());
                if(neighbours.get(random).distanceFromGoal > currentState.distanceFromGoal) {
                    tempState = neighbours.get(random);
                    break;
                }
                float temperature = getTemperature(currentState.distanceFromGoal - neighbours.get(random).distanceFromGoal , 2 , counter);
                float rand = (float) Math.random();
                if(temperature > rand) {
                    tempState = neighbours.get(random);
                    break;
                }
            }
            currentState = tempState;
            counter++;
        }
    }

    @Override
    public Problem.State getNextState() {
        return null;
    }

    @Override
    public void getInfo(Problem.State state) {
        problem.showInfo(state);
        System.out.println("Counter: " + counter);
        System.out.println("Visited Nodes: " + visitedNodesNo);
        System.out.println("Extended Nodes: " + expandedNodesNo);
    }

    @Override
    public void trap() {
        System.out.println("We Are In Trap!!!");
    }

    public float getTemperature(float distance , int i , int counter){
        float temp = (float) Math.exp(distance / t);
        switch (i) {
            case 0:
                t *= alpha;
                break;
            case 1:
                t = t0 - (counter * ((t0 - tn)/ n));
                break;
            case 2:
                t = (float) (t0 * Math.pow((tn / t0),(counter / n)));
                break;
        }
        return temp;
    }
}
