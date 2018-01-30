package Problems;

import java.util.ArrayList;
import java.util.Arrays;

public class EightQueen implements Problem {

    public final static int QUEENS = 8;

    public class State extends Problem.State{
        public ArrayList<Integer> positions;
        public State(ArrayList<Integer> positions,State state){
            this.positions = positions;
            parrent = state;
            distanceFromGoal = 0;
        }
    }

    @Override
    public State initialState() {
        ArrayList<Integer> positions = new ArrayList<Integer>();
        for (int i = 0; i < QUEENS ;i++)
            positions.add((int)(Math.random() * QUEENS));
        State state = new State(positions , null);
        update(state);
        return state;
    }

    @Override
    public float distanceFromGoal(Problem.State state) {
        State temp = (State) state;
        float distance = 0;

        for (int i = 0; i < QUEENS; i++) {
            for (int j = i + 1; j < QUEENS; j++) {
                if (temp.positions.get(i) == temp.positions.get(j))
                    distance++;
                float a = ((float)(temp.positions.get(i) - temp.positions.get(j))/(float)(i - j));
                if (Math.abs(a) == (float)1) {
                    distance++;
                }
            }
        }
        return distance;
    }

    @Override
    public ArrayList<Problem.State> neighbours(Problem.State state) {
        State temp = (State) state;
        ArrayList<Problem.State> output = new ArrayList<>();
        ArrayList<Integer> tempPosition = (ArrayList<Integer>) temp.positions.clone();

        for (int i = 0; i < QUEENS; i++){
            for (int j = 0; j < QUEENS; j++){
                tempPosition = (ArrayList<Integer>) temp.positions.clone();
                if(tempPosition.get(i) != j){
                    tempPosition.set(i,j);
                    State neighbour = new State(tempPosition , temp);
                    update(neighbour);
                    output.add(neighbour);
                }
            }
        }
        return output;
    }

    @Override
    public boolean goalTest(Problem.State state) {
        if (state.distanceFromGoal == 0)
            return true;
        return false;
    }

    @Override
    public void showInfo(Problem.State state) {
        State temp = (State) state;
        if(temp == null)
            return;
        showInfo(state.parrent);
        System.out.println(temp.positions);
        System.out.println("Distance From Goal: " + temp.distanceFromGoal);
    }

    @Override
    public void update(Problem.State state) {
        state.distanceFromGoal = distanceFromGoal(state);
    }

    @Override
    public boolean isExist(ArrayList<Problem.State> input, Problem.State second) {
        State secondState = (State) second;
        for (int i = 0; i < input.size(); i++) {
            State firstState = (State)input.get(i);
            if (firstState.positions.equals(secondState.positions)) {
                return true;
            }
        }
        return false;
    }

}
