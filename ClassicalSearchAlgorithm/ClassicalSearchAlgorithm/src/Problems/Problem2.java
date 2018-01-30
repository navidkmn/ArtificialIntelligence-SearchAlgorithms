package Problems;

import java.util.ArrayList;

public class Problem2 implements Problem {

//    int [][] start = {
//            {0, 1, 4},
//            {3, 5, 2},
//            {6, 7, 8}
//    };

//        int [][] start = {
//                {0, 8, 2},
//                {5, 4, 3},
//                {6, 7, 1}
//        };

//    int [][] start = {
//            {1, 4, 2},
//            {7, 0, 5},
//            {3, 6, 8}
//    };



    public State init;
    public static final int COLUMN = 3;
    public static final int ROW = 3;

    public Problem2(int[][] matrix){
        init = new State(matrix);
    }

    public class State extends Problem.State{

        public State previousState;
        public int[][] puzzle;
        public int zeroPosition;

        public State(int[][] state){
            puzzle = new int[ROW][COLUMN];

            for(int i = 0 ; i < ROW ; i++) {
                for (int j = 0; j < COLUMN; j++) {
                    puzzle[i][j] = state[i][j];
                    if (state[i][j] == 0)
                        zeroPosition = i * COLUMN + j;
                }
            }
            previousState = null;
            cost = 0;
            levelNo = 0;
            gn = 0;
            fn = 0;
        }

        @Override
        public boolean equalStates(Problem.State state) {
            State newState = (State) state;
            for(int i = 0 ; i < ROW ; i++)
                for (int j = 0 ; j < COLUMN ; j++)
                    if(puzzle[i][j] != newState.puzzle[i][j])
                        return false;
            return true;
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

        public int move; //Zero Movement : 0 = UP , 1 = RIGHT , 2 = DOWN , 3 = LEFT
        public Action(int move){
            this.move = move;
        }
    }

    @Override
    public State initialState() {
        return init;
    }

    @Override
    public Problem.State finalState() {

        int[][] temp = new int[ROW][COLUMN];

        for(int i = 0 ; i < ROW; i++)
            for (int j = 0; j < COLUMN; j++)
                temp[i][j] = i * COLUMN + j;

        return new State(temp);
    }

    @Override
    public ArrayList<Problem.Action> actions(Problem.State state) {
        State newState = (State)state;
        ArrayList<Problem.Action> canAct = new ArrayList<>();

        int zeroX = newState.zeroPosition / COLUMN;
        int zeroY = newState.zeroPosition % COLUMN;

        if(zeroX > 0) {
            canAct.add(new Action(0));
        }
        if(zeroX < ROW - 1) {
            canAct.add(new Action(2));
        }
        if(zeroY > 0) {
            canAct.add(new Action(3));
        }
        if(zeroY < COLUMN - 1) {
            canAct.add(new Action(1));
        }
        return canAct;
    }

    @Override
    public ArrayList<Problem.Action> reverseActions(Problem.State state) {
        return actions(state);
    }

    @Override
    public Problem.State result(Problem.State state, Problem.Action action) {
        State newState = (State)state;
        Action newAction = (Action) action;

        int[][] temp = new int[ROW][COLUMN];

        for(int i = 0 ; i < ROW ; i++)
            for(int j = 0 ; j < COLUMN ; j++)
                temp[i][j] = newState.puzzle[i][j];

        int zeroX = newState.zeroPosition / COLUMN;
        int zeroY = newState.zeroPosition % COLUMN;

        switch (newAction.move) {
            case 0: //UP
                temp[zeroX][zeroY] = temp[zeroX - 1][zeroY];
                temp [zeroX - 1][zeroY] = 0;
                break;
            case 1: //RIGHT
                temp[zeroX][zeroY] = temp[zeroX][zeroY + 1];
                temp [zeroX][zeroY + 1] = 0;
                break;
            case 2: //DOWN
                temp[zeroX][zeroY] = temp[zeroX + 1][zeroY];
                temp [zeroX + 1][zeroY] = 0;
                break;
            case 3: //LEFT
                temp[zeroX][zeroY] = temp[zeroX][zeroY - 1];
                temp [zeroX][zeroY - 1] = 0;
                break;
        }
        return new State(temp);
    }

    @Override
    public boolean goalTest(Problem.State state) {
        State newState = (State) state;

        for(int i = 0 ; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (newState.puzzle[i][j] != i * COLUMN + j)
                    return false;
            }
        }
        return true;
    }

    @Override
    public float stepCost(Problem.State parent, Problem.State state, Problem.Action action) {
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
            for(int k = 0 ; k < ROW ; k++) {
                for (int j = 0; j < COLUMN; j++)
                    System.out.print(state.puzzle[k][j] + " ");
                System.out.println();
            }
            System.out.println();
        }

        float[] out = new float[2];
        out[0] = path.size();
        out[1] = pathCost(path);

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
    public float heuristic(Problem.State state) {
        State newState = (State) state;
        float count = 0;

        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COLUMN; j++){
                int no = newState.puzzle[i][j];
                int x = no / COLUMN;
                int y = no % COLUMN;
                int xDistance = Math.abs(i - x);
                int yDistance = Math.abs(j - y);
                count += Math.sqrt(xDistance*xDistance + yDistance*yDistance);
            }
        return count;
    }
}
