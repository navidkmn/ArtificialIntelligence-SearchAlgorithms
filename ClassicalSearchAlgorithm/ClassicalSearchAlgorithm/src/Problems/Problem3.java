package Problems;

import java.util.ArrayList;

public class Problem3 implements Problem {

//    int [][] start = {
//                {2, 1, 1 , 0},
//                {0, 0, 1 , 1},
//                {1, 1, 1 , 0},
//                {0, 1, 0 , 1},
//                {1, 1 ,0 , 0},
//                {1, 1 ,1 , 1}
//        };

    public State init;
    public int row;
    public int column;

    public static final int WALL = 0;

    public Problem3(int[][] matrix){
        row = matrix.length;
        column = matrix[0].length;
        init = new State(matrix);
    }

    public class State extends Problem.State {

        public State previousState;
        public int[][] maze;
        public int robotPosition;

        public State(int[][] state) {
            maze = new int[row][column];

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    maze[i][j] = state[i][j];
                    if(state[i][j] == 2) {
                        robotPosition = i * column + j;
                    }
                }
                previousState = null;
                cost = 0;
                levelNo = 0;
                gn = 0;
                fn = 0;
            }
        }

        @Override
        public boolean equalStates(Problem.State state) {
            State newState = (State) state;
            for(int i = 0 ; i < row; i++)
                for (int j = 0 ; j < column; j++)
                    if(maze[i][j] != newState.maze[i][j])
                        return false;
            return true;
        }

        @Override
        public void updateState(Problem.State parent, Problem.State state, float movementCost) {
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
        int[][] temp = new int[row][column];

        for(int i = 0 ; i < row; i++)
            for (int j = 0; j < column; j++)
                temp[i][j] = init.maze[i][j];
        temp[0][0] = 1;
        temp[row - 1][column - 1] = 2;
        return new State(temp);
    }

    @Override
    public ArrayList<Problem.Action> actions(Problem.State state) {
        State newState = (State)state;
        ArrayList<Problem.Action> canAct = new ArrayList<>();

        int robotX = newState.robotPosition / column;
        int robotY = newState.robotPosition % column;

        if(robotX > 0) {
            if(newState.maze[robotX - 1][robotY] != WALL)
                canAct.add(new Action(0));
        }
        if(robotX < row - 1) {
            if(newState.maze[robotX + 1][robotY] != WALL)
                canAct.add(new Action(2));
        }
        if(robotY > 0) {
            if(newState.maze[robotX][robotY - 1] != WALL)
                canAct.add(new Action(3));
        }
        if(robotY < column - 1) {
            if(newState.maze[robotX][robotY + 1] != WALL)
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

        int[][] temp = new int[row][column];

        for(int i = 0 ; i < row ; i++)
            for(int j = 0 ; j < column ; j++)
                temp[i][j] = newState.maze[i][j];

        int robotX = newState.robotPosition / column;
        int robotY = newState.robotPosition % column;

        switch (newAction.move) {
            case 0: //UP
                temp[robotX][robotY] = temp[robotX - 1][robotY];
                temp [robotX - 1][robotY] = 2;
                break;
            case 1: //RIGHT
                temp[robotX][robotY] = temp[robotX][robotY + 1];
                temp [robotX][robotY + 1] = 2;
                break;
            case 2: //DOWN
                temp[robotX][robotY] = temp[robotX + 1][robotY];
                temp [robotX + 1][robotY] = 2;
                break;
            case 3: //LEFT
                temp[robotX][robotY] = temp[robotX][robotY - 1];
                temp [robotX][robotY - 1] = 2;
                break;
        }
        return new State(temp);
    }

    @Override
    public boolean goalTest(Problem.State state) {
        State newState = (State) state;
        if(newState.maze[row - 1][column - 1] == 2)
            return true;
        return false;
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
            for(int k = 0 ; k < row ; k++) {
                for (int j = 0; j < column; j++)
                    System.out.print(state.maze[k][j] + " ");
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
        int robotX = newState.robotPosition / column;
        int robotY = newState.robotPosition % column;

        int D = 1; //lowest cost between 2 adjacent squares
        int dx = Math.abs(robotX - (row - 1));
        int dy = Math.abs(robotY - (column - 1));
        return D * (dx + dy);
    }
}
