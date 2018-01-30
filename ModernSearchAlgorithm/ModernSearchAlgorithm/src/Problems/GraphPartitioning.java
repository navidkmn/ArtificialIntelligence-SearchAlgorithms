package Problems;

import java.util.ArrayList;

public class GraphPartitioning implements Problem {

    public final int SIZE = 12;

    public class State extends Problem.State{
        public int[][] graph;
        public ArrayList<Integer> leftSubGraph;
        public ArrayList<Integer> rightSubGraph;
        public int edgesNo;

        public State(State state,int[][] graph , ArrayList<Integer> left, ArrayList<Integer> right){
            this.graph = graph;
            for(int i = 0; i<graph.length;i++)
                for (int j = 0 ;j<graph.length ;j++)
                    if(graph[i][j] == 1)
                        edgesNo++;
            edgesNo/=2;
            parrent = state;
            distanceFromGoal = 0;
            this.leftSubGraph = left;
            this.rightSubGraph = right;
        }
    }


    @Override
    public State initialState() {
        int[][] graph = {
                {0,1,1,0,0,0,0,0,0,0,0,0}, //0
                {1,0,1,0,1,1,0,0,0,0,0,0}, //1
                {1,1,0,1,0,0,0,0,0,0,0,0}, //2
                {0,0,1,0,1,0,1,0,0,0,0,0}, //3
                {0,1,0,1,0,0,0,1,0,0,0,0}, //4
                {0,1,0,0,0,0,0,0,1,0,0,0}, //5
                {0,0,0,1,0,0,0,1,0,0,0,0}, //6
                {0,0,0,0,1,0,1,0,1,0,0,1}, //7
                {0,0,0,0,0,1,0,1,0,1,1,0}, //8
                {0,0,0,0,0,0,0,0,1,0,1,0}, //9
                {0,0,0,0,0,0,0,0,1,1,0,1}, //10
                {0,0,0,0,0,0,0,1,0,0,1,0}  //11
        };
        ArrayList<Integer> leftSubGraph = new ArrayList<>();
        ArrayList<Integer> rightSubGraph = new ArrayList<>();
        leftSubGraph.add(0);
        for (int i = 1; i < SIZE; i++)
            rightSubGraph.add(i);
        State state = new State(null,graph , leftSubGraph , rightSubGraph);
        update(state);
        return state;
    }

    @Override
    public float distanceFromGoal(Problem.State state) {
        State temp = (State)state;
        float balance;
        if(temp.leftSubGraph.size() < temp.rightSubGraph.size())
            balance = (float)temp.leftSubGraph.size() / (float)temp.rightSubGraph.size();
        else
            balance = (float)temp.rightSubGraph.size() / (float)temp.leftSubGraph.size();

        float connection = 0;
        for(int i = 0; i<temp.leftSubGraph.size(); i++){
            int a = temp.leftSubGraph.get(i);
            for(int j = 0; j < SIZE; j++){
                if(temp.graph[a][j] == 1 && a != j && !temp.leftSubGraph.contains(j))
                    connection++;
            }
        }
        connection /= temp.edgesNo;
        return balance + connection;
    }

    @Override
    public ArrayList<Problem.State> neighbours(Problem.State state) {
        State temp = (State) state;
        ArrayList<Problem.State> output = new ArrayList<>();
        for(int i = 0; i < temp.leftSubGraph.size() ;i++){
            int a = temp.leftSubGraph.get(i);
            for(int j = 0; j < SIZE ; j++){
                ArrayList<Integer> leftTemp = (ArrayList<Integer>) temp.leftSubGraph.clone();
                ArrayList<Integer> rightTemp = (ArrayList<Integer>) temp.rightSubGraph.clone();
                if(temp.graph[a][j] == 1 && a != j && temp.rightSubGraph.contains(j)){
                    leftTemp.add(j);
                    for(int k = 0; k < rightTemp.size(); k++)
                        if(rightTemp.get(k) == j)
                            rightTemp.remove(k);
                    State neighbour = new State(temp,temp.graph,leftTemp , rightTemp);
                    update(neighbour);
                    output.add(neighbour);
                }
            }
        }
        return output;
    }

    @Override
    public boolean goalTest(Problem.State state) {
        if(state.parrent != null) {
            if (state.distanceFromGoal < state.parrent.distanceFromGoal)
                return true;
            return false;
        }
        return false;
    }

    @Override
    public void showInfo(Problem.State state) {
        State temp = (State) state;
        if(temp == null)
            return;
        showInfo(state.parrent);
        System.out.println(temp.rightSubGraph + " " + temp.leftSubGraph);
    }

    @Override
    public void update(Problem.State state) {
        state.distanceFromGoal = distanceFromGoal(state);
    }

    @Override
    public boolean isExist(ArrayList<Problem.State> input, Problem.State second) {
        return false;
    }
}
