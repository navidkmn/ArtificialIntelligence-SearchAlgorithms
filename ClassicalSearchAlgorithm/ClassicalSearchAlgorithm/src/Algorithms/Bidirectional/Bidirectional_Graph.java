package Algorithms.Bidirectional;

import Algorithms.Algorithm;
import Problems.Problem;

import java.util.ArrayList;

public class Bidirectional_Graph extends Algorithm {

    ArrayList<Problem.State> sourceOpenStates;
    ArrayList<Problem.State> sourceCloseStates;

    ArrayList<Problem.State> destOpenStates;
    ArrayList<Problem.State> destCloseStates;

    long start,end;

    public Bidirectional_Graph(Problem problem) {
        super(problem);
        sourceOpenStates = new ArrayList<>();
        sourceCloseStates = new ArrayList<>();
        destOpenStates = new ArrayList<>();
        destCloseStates = new ArrayList<>();
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
                BFS(s,sourceOpenStates,sourceCloseStates);

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
                reverseBFS(d,destOpenStates,destCloseStates);

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
        for (int i = 0 ; i < sourceOpenStates.size(); i++){
            for(int j = 0 ; j < destCloseStates.size(); j++) {
                if (sourceOpenStates.get(i).equalStates(destCloseStates.get(j))) {
                    ArrayList output = new ArrayList();
                    output.add(sourceOpenStates.get(i));
                    output.add(destCloseStates.get(j));
                    return output;
                }
            }
        }
        for (int i = 0 ; i < sourceCloseStates.size(); i++){
            for(int j = 0 ; j < destOpenStates.size(); j++) {
                if (sourceCloseStates.get(i).equalStates(destOpenStates.get(j))) {
                    ArrayList output = new ArrayList();
                    output.add(sourceCloseStates.get(i));
                    output.add(destOpenStates.get(j));
                    return output;
                }
            }
        }
        for (int i = 0 ; i < sourceCloseStates.size(); i++){
            for(int j = 0 ; j < destCloseStates.size(); j++) {
                if (sourceCloseStates.get(i).equalStates(destCloseStates.get(j))) {
                    ArrayList output = new ArrayList();
                    output.add(sourceCloseStates.get(i));
                    output.add(destCloseStates.get(j));
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
        System.out.println("EXPANDED NODES: "+ sourceCloseStates.size() + destCloseStates.size());
        System.out.println("VISITED NODES: "+ sourceCloseStates.size() + destCloseStates.size());

        end = System.currentTimeMillis();
        System.out.println("RUN TIME: " + (end - start) + " MS");
    }

    public void BFS(Problem.State s,ArrayList open , ArrayList close){
        expandedNodesNo++;
        visitedNodesNo++;
        close.add(s);

        ArrayList<Problem.Action> actions = problem.actions(s);

        for (Problem.Action a:actions) {
            Problem.State next = problem.result(s, a);
            next.updateState(s,next,problem.stepCost(s,next,a));

            if (!isExist(next,close)) {
                open.add(next);
            }
        }
        maxMemoryUse = Math.max(maxMemoryUse , sourceOpenStates.size() + sourceCloseStates.size() + destOpenStates.size() + destCloseStates.size());
    }

    public void reverseBFS(Problem.State s,ArrayList open , ArrayList close){
        expandedNodesNo++;
        visitedNodesNo++;
        close.add(s);

        ArrayList<Problem.Action> actions = problem.reverseActions(s);

        for (Problem.Action a:actions) {
            Problem.State next = problem.result(s, a);
            next.updateState(s,next,problem.stepCost(s,next,a));

            if (!isExist(next,close)) {
                open.add(next);
            }
        }
        maxMemoryUse = Math.max(maxMemoryUse , sourceOpenStates.size() + sourceCloseStates.size() + destOpenStates.size() + destCloseStates.size());
    }

    public float[] showResult(Problem.State finalState){
        return problem.bestPath(finalState);
    }

    public boolean isExist(Problem.State next , ArrayList input){
        for (int i = 0; i < input.size(); i++) {
            Problem.State temp = (Problem.State)input.get(i);
            if (temp.equalStates(next))
                return true;
        }
        return false;
    }

    @Override
    public Problem.State getNextState() {
        return sourceOpenStates.remove(0);
    }

    public Problem.State getDestNextState(){
        return destOpenStates.remove(0);
    }
}
