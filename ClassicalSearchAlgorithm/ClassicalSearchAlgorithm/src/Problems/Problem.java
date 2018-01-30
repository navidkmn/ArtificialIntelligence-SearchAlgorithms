package Problems;

import java.util.ArrayList;

public interface Problem {

    abstract class State{
        public float cost; //cost from parent state
        public int levelNo; //state depth
        public float gn; //cost from initial state
        public float fn; // fn = gn + heuristic(n)
        abstract public boolean equalStates(State state);
        abstract public void updateState(State parent,State state, float movementCost);
    }

    class Action{

    }

    State initialState();

    State finalState();

    ArrayList<Action> actions(State state);

    ArrayList<Action> reverseActions (State state);

    State result(State state, Action action);

    boolean goalTest(State state);

    float stepCost(State parent , State state , Action action);
    float[] bestPath(State finalState);
    float pathCost(ArrayList<State> path);
    float heuristic(State state);
}
