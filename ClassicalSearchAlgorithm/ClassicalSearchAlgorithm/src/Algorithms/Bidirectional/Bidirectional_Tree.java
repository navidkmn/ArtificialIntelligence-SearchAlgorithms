package Algorithms.Bidirectional;

import Algorithms.Algorithm;
import Problems.Problem;

import java.util.ArrayList;

public class Bidirectional_Tree extends Algorithm {

    ArrayList<Problem.State> sourceOpenStates;
    ArrayList<Problem.State> destOpenStates;

    long start,end;

    public Bidirectional_Tree(Problem problem) {
        super(problem);
        sourceOpenStates = new ArrayList<>();
        destOpenStates = new ArrayList<>();
        run();
    }

    @Override
    public void run() {

        start = System.currentTimeMillis();

        Problem.State s = problem.initialState();
        sourceOpenStates.add(s);
        maxMemoryUse++;

        Problem.State d = problem.finalState();
        destOpenStates.add(d);
        maxMemoryUse++;

        while (!sourceOpenStates.isEmpty() || !destOpenStates.isEmpty()){
            if(!sourceOpenStates.isEmpty()){
                s = getNextState();
                BFS(s,sourceOpenStates);

                ArrayList temp = checker();
                if(temp != null){
                    System.out.println("FROM ROOT");
                    float[] first = showResult((Problem.State) temp.get(0));
                    System.out.println("FROM Goal");
                    float[] second = showResult((Problem.State) temp.get(1));
                    printStatus(first[0] + second[0] - 1 , first[1] + second[1]);
                    return;
                }
            }

            if(!destOpenStates.isEmpty()){
                d = getDestNextState();
                reverseBFS(d,destOpenStates);

                ArrayList temp = checker();
                if(checker() != null){
                    System.out.println("FROM ROOT");
                    float[] first = showResult((Problem.State) temp.get(0));
                    System.out.println("FROM Goal");
                    float[] second = showResult((Problem.State) temp.get(1));
                    printStatus(first[0] + second[0] - 1 , first[1] + second[1]);
                    return;
                }
            }
        }
    }

    public ArrayList checker(){
        for (int i = 0 ; i < sourceOpenStates.size(); i++){
            for(int j = 0 ; j < destOpenStates.size(); j++) {
                if (sourceOpenStates.get(i).equalStates(destOpenStates.get(j))) {
                    ArrayList output = new ArrayList();
                    output.add(sourceOpenStates.get(i));
                    output.add(destOpenStates.get(j));
                    return output;
                }
            }
        }
        return null;
    }

    public void printStatus(float nodes, float pathCost){
        System.out.println("NODE NUMBER EXIST: " + nodes);
        System.out.println("PATH COST: " + pathCost);
        System.out.println("MAX MEMORY USAGE: " + maxMemoryUse);
        System.out.println("EXPANDED NODES: "+ expandedNodesNo);
        System.out.println("VISITED NODES: "+ visitedNodesNo);

        end = System.currentTimeMillis();
        System.out.println("RUN TIME: " + (end - start) + " MS");
    }

    public void BFS(Problem.State s,ArrayList open){
        expandedNodesNo++;
        visitedNodesNo++;

        ArrayList<Problem.Action> actions = problem.actions(s);

        for (Problem.Action a:actions) {
            Problem.State next = problem.result(s, a);
            next.updateState(s,next,problem.stepCost(s,next,a));
            open.add(next);
        }
        maxMemoryUse = Math.max(maxMemoryUse , sourceOpenStates.size() + destOpenStates.size());
    }

    public void reverseBFS(Problem.State s,ArrayList open){
        expandedNodesNo++;
        visitedNodesNo++;

        ArrayList<Problem.Action> actions = problem.reverseActions(s);

        for (Problem.Action a:actions) {
            Problem.State next = problem.result(s, a);
            next.updateState(s,next,problem.stepCost(s,next,a));
            open.add(next);
        }
        maxMemoryUse = Math.max(maxMemoryUse , sourceOpenStates.size() + destOpenStates.size());
    }

    public float[] showResult(Problem.State finalState){
        return problem.bestPath(finalState);
    }

    @Override
    public Problem.State getNextState() {
        return sourceOpenStates.remove(0);
    }

    public Problem.State getDestNextState(){
        return destOpenStates.remove(0);
    }
}
