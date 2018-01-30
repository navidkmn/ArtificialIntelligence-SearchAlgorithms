package Problems;

import java.util.ArrayList;

public class Problem1 implements Problem {

    public static final int FIRST_PITCHER_CAP = 4;
    public static final int SECOND_PITCHER_CAP = 3;

    public class State extends Problem.State{

        public int firstPitcherFilled;
        public int secondPicherFilled;
        public State previousState;
        public State(int first , int second){
            previousState = null;
            cost = 0;
            levelNo = 0;
            gn = 0;
            fn = 0;
            firstPitcherFilled = first;
            secondPicherFilled = second;
        }

        @Override
        public boolean equalStates(Problem.State state) {
            State newState = (State)state;
            if(newState.firstPitcherFilled == firstPitcherFilled && newState.secondPicherFilled == secondPicherFilled)
                return true;
            return false;
        }

        @Override
        public void updateState(Problem.State parent,Problem.State state, float movementCost) {
            previousState = (State) parent;
            State child = (State) state;
            cost = movementCost;
            gn = previousState.gn + movementCost;
            levelNo = previousState.levelNo + 1;
            fn = gn + heuristic(child);
        }
    }

    public class Action extends Problem.Action{
        public int fillFirstPitcher;
        public int fillSecondPitcher;
        public Action(int first , int second){
            fillFirstPitcher = first;
            fillSecondPitcher = second;
        }
    }

    @Override
    public State initialState() {
        State state = new State(0,0);
        return state;
    }

    @Override
    public Problem.State finalState() {
        return new State(2,0);
    }

    @Override
    public ArrayList<Problem.Action> actions(Problem.State state) {
        State newState = (State) state;
        ArrayList<Problem.Action> canAct = new ArrayList<>();
        /*fill or empty pitchers independent*/
        if(newState.firstPitcherFilled != 0)
            canAct.add(new Action(0 - newState.firstPitcherFilled,0));
        if(newState.secondPicherFilled != 0)
            canAct.add(new Action(0,0 - newState.secondPicherFilled));
        if(newState.firstPitcherFilled != FIRST_PITCHER_CAP)
            canAct.add(new Action(FIRST_PITCHER_CAP - newState.firstPitcherFilled,0));
        if(newState.secondPicherFilled != SECOND_PITCHER_CAP)
            canAct.add(new Action(0 , SECOND_PITCHER_CAP - newState.secondPicherFilled));
        /*move from A to B*/
        if(newState.firstPitcherFilled != 0 || newState.secondPicherFilled != 0) {
            if (FIRST_PITCHER_CAP >= newState.firstPitcherFilled + newState.secondPicherFilled) {
                if(newState.secondPicherFilled != 0)
                    canAct.add(new Action(newState.secondPicherFilled, 0 - newState.secondPicherFilled));
            }
            else
                canAct.add(new Action(FIRST_PITCHER_CAP - newState.firstPitcherFilled, newState.firstPitcherFilled - FIRST_PITCHER_CAP));

            if (SECOND_PITCHER_CAP >= newState.firstPitcherFilled + newState.secondPicherFilled) {
                if (newState.firstPitcherFilled != 0)
                    canAct.add(new Action(0 - newState.firstPitcherFilled, newState.firstPitcherFilled));
            }
            else
                canAct.add(new Action(newState.secondPicherFilled - SECOND_PITCHER_CAP, SECOND_PITCHER_CAP - newState.secondPicherFilled));
        }
        return canAct;
    }

    @Override
    public ArrayList<Problem.Action> reverseActions(Problem.State state) {
        State newState = (State) state;
        ArrayList<Problem.Action> canAct = new ArrayList<>();

        int firstFilled = newState.firstPitcherFilled;
        int secondFilled = newState.secondPicherFilled;

        if(firstFilled == FIRST_PITCHER_CAP) {
            //from out
            for(int i = 0 ; i < FIRST_PITCHER_CAP ;i++)
                canAct.add(new Action(i - FIRST_PITCHER_CAP, 0));
            //from second pitcher
            for (int i = 1 ; i <= (SECOND_PITCHER_CAP - secondFilled); i++)
                if(i <= FIRST_PITCHER_CAP)
                    canAct.add(new Action(0 - i, i));

        } else{
            //from second pitcher
            if(firstFilled != 0 && secondFilled == 0){
                for (int i = 1 ; i <= firstFilled; i++) {
                    canAct.add(new Action(0-i,i));
                }
            }
        }

        if(firstFilled == 0) {
            for(int i = 0 ; i < FIRST_PITCHER_CAP; i++)
                canAct.add(new Action(FIRST_PITCHER_CAP - i, 0));
        }

        if(secondFilled == SECOND_PITCHER_CAP) {
            //from out
            for(int i = 0 ; i < SECOND_PITCHER_CAP; i++)
               canAct.add(new Action(0, i - SECOND_PITCHER_CAP));
            //from first pitcher
            for (int i = 1 ; i <= (FIRST_PITCHER_CAP - firstFilled); i++) {
                if(i <= SECOND_PITCHER_CAP)
                    canAct.add(new Action(i, 0-i));
            }
        } else{
            //from first pitcher
            if(secondFilled != 0 && firstFilled == 0){
                for (int i = 1 ; i <= secondFilled; i++) {
                    canAct.add(new Action(i,0-i));
                }
            }
        }

        if(secondFilled == 0) {
            for(int i = 0 ; i < SECOND_PITCHER_CAP; i++)
                canAct.add(new Action(0, SECOND_PITCHER_CAP - i));
        }
        return canAct;
     }

    @Override
    public State result(Problem.State state, Problem.Action action) {
        State newState = (State)state;
        Action newAction = (Action) action;
        return new State(newState.firstPitcherFilled + newAction.fillFirstPitcher ,newState.secondPicherFilled + newAction.fillSecondPitcher);
    }

    @Override
    public boolean goalTest(Problem.State state) {
        State newState = (State)state;
        if(newState.firstPitcherFilled == 2)
            return true;
        return false;
    }

    @Override
    public float stepCost(Problem.State previousState, Problem.State nextState, Problem.Action action) {
        return 1;
    }

    @Override
    public float[] bestPath(Problem.State finalState) {
        ArrayList<Problem.State> path = new ArrayList<>();
        State nextState = (State) finalState;
        path.add(nextState);
        while(nextState.previousState != null){
            nextState = nextState.previousState;
            path.add(nextState);
        }

        for (int i = path.size()-1 ; i >=0; i--){
            State state = (State) path.get(i);
            System.out.println("("+state.firstPitcherFilled + "," + state.secondPicherFilled + ")");
        }

        float[] out = new float[2];
        out[0] = path.size();
        out[1]= pathCost(path);

        return out;
    }

    @Override
    public float pathCost(ArrayList<Problem.State> path) {
        int cost = 0;
        for (int i = 0 ; i < path.size()-1 ;i++)
            cost += path.get(i).cost;
        return cost;
    }

    @Override
    public float heuristic(Problem.State state)
    {
        State nextState = (State) state;
        return 0;
    }
}
