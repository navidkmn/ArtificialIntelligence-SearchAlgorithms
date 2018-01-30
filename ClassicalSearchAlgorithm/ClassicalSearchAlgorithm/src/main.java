import Algorithms.Astar.As_Graph;
import Algorithms.Astar.As_Tree;
import Algorithms.BFS.BFS_Graph;
import Algorithms.BFS.BFS_Tree;
import Algorithms.Bidirectional.Bidirectional_Graph;
import Algorithms.Bidirectional.Bidirectional_Tree;
import Algorithms.DFS.dfs.DFS_Graph;
import Algorithms.DFS.dfs.DFS_Tree;
import Algorithms.DFS.iterativeDeepening.DFS_iterativeDeepening_Graph;
import Algorithms.DFS.limitDepth.DFS_limitDepth_Grapth;
import Algorithms.DFS.limitDepth.DFS_limitDepth_Tree;
import Algorithms.UniformCostSearch.UCS_Graph;
import Algorithms.UniformCostSearch.UCS_Tree;

import Problems.Problem1;
import Problems.Problem2;
import Problems.Problem3;

public class main {
    public static void main(String[] args){

        int [][] start = {
                {2, 1, 1 , 0},
                {0, 0, 1 , 1},
                {1, 1, 1 , 0},
                {0, 1, 0 , 1},
                {1, 1 ,0 , 0},
                {1, 1 ,1 , 1}
        };
        Problem3 problem = new Problem3(start);
        new Bidirectional_Tree(problem);
    }
}
