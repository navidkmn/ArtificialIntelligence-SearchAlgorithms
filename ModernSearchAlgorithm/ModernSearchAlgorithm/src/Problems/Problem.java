package Problems;

import java.util.ArrayList;

public interface Problem {

    abstract class State{
        public State parrent;
        public float distanceFromGoal;
    }

    State initialState();
    float distanceFromGoal(State state);
    ArrayList<State> neighbours(State state);
    boolean goalTest(State state);
    void showInfo(State state);
    void update(State state);
    boolean isExist(ArrayList<State> input, State second);

}
