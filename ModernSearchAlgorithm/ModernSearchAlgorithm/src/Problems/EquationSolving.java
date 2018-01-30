package Problems;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;

import java.util.ArrayList;
import java.util.Arrays;

public class EquationSolving {

    public final int POPULATION_NO = 20;
    public final int VARIABLES_NO = 4;
    public final float CROSSOVER_RATE = 0.25f;
    public final float MUTATION_RATE = 0.1f;

    ArrayList <Float> averages = new ArrayList<>();
    ArrayList <Float> worsts = new ArrayList<>();
    ArrayList <Float> bests = new ArrayList<>();

    public class State {
        public State parrent;

        public int[] distanceFromGoal;
        public int[] coefficients;

        public float[] fitnesses;
        public float[] probabilities;
        public float[] cumulative;

        public float bestMerit;
        public float worstMerit;
        public float averageMerit;

        public ArrayList<int[]> population = new ArrayList<>();

        public State (ArrayList<int[]> population, int[] coefficients , State state){
            this.population = population;
            this.coefficients = coefficients;
            this.parrent = state;

            this.distanceFromGoal = new int[POPULATION_NO];
            this.fitnesses = new float[POPULATION_NO];
            this.cumulative = new float[POPULATION_NO];
            this.probabilities = new float[POPULATION_NO];

            Arrays.fill(distanceFromGoal , 0);
            Arrays.fill(fitnesses , 0);

            bestMerit = Float.MAX_VALUE;
            worstMerit = 0;
            averageMerit = 0;
        }
    }

    public State initialState() {
        ArrayList<int[]> population = new ArrayList<>();
        int[] coefficients = new int[]{1,2,3,4,-40};
        for (int i = 0; i < POPULATION_NO; i++){
            int[] instance = new int[VARIABLES_NO];
            for (int j = 0; j < VARIABLES_NO ;j++) {
                int random = (int) (Math.random() * Math.abs(coefficients[VARIABLES_NO]));
                instance[j] = random;
            }
            population.add(instance);
        }
        return new State(population,coefficients,null);
    }

    public int[] evaluation(State state) {
        // goal is 0
        int[] distances = new int[POPULATION_NO];
        for (int i = 0;i < POPULATION_NO; i++){
            int distance = 0;
            int[] variables = state.population.get(i);
            for(int j = 0; j < VARIABLES_NO; j++)
                distance+= variables[j]*state.coefficients[j];
            distance+= state.coefficients[VARIABLES_NO];

            distance = Math.abs(distance);
            distances[i] = distance;

            if(state.worstMerit < distance)
                state.worstMerit = distance;

            if(state.bestMerit > distance)
                state.bestMerit = distance;
        }


        int avg = 0;
        for (int i = 0; i < POPULATION_NO; i++)
            avg+= distances[i];
        state.averageMerit = (float)avg / (float) POPULATION_NO;

        worsts.add(state.worstMerit);
        bests.add(state.bestMerit);
        averages.add(state.averageMerit);

        return distances;
    }

    public State selection(State state){

        for(int i = 0 ;i<POPULATION_NO; i++) {
            state.fitnesses[i] = (float) 1 / (float) (state.distanceFromGoal[i] + 1);
        }

        float total = 0;
        for (int i = 0; i < POPULATION_NO; i++)
            total += state.fitnesses[i];

        for(int i = 0; i < POPULATION_NO; i++) {
            state.probabilities[i] = state.fitnesses[i] / total;
        }

        for(int i = 0;i < POPULATION_NO; i++) {
            for (int j = 0; j <= i; j++)
                state.cumulative[i] += state.probabilities[j];
        }



        int counter = 0;
        ArrayList<int[]> newPopulation = new ArrayList<>();
        while (newPopulation.size() != POPULATION_NO){
            float rand = (float) Math.random();
            float start = 0;
            for (int i = 0; i < POPULATION_NO ;i++) {
                if (rand >= start && rand < state.cumulative[i]) {
                    newPopulation.add(state.population.get(i));
                    break;
                }
                start = state.cumulative[i];
            }
            counter++;
        }
        state.population = (ArrayList<int[]>) newPopulation.clone();

        counter = 0;
        ArrayList<Integer> parrents = new ArrayList<>();
        while (counter <POPULATION_NO){
            float rand = (float) Math.random();
            if(rand < CROSSOVER_RATE){
                parrents.add(counter);
            }
            counter++;
        }
        ArrayList<int[]> childs = new ArrayList<>();
        //int[] c = new int[]{1,1,2};
        for (int i = 0; i < parrents.size(); i++){
            int c = (int) (Math.random() * (VARIABLES_NO - 2)) + 1;
            int[] father = state.population.get(parrents.get(i));
            int[] mother = state.population.get(parrents.get((i + 1) % parrents.size()));
            int[] child = new int[VARIABLES_NO];
            for (int j = 0 ; j < c ; j++)
                child[j] = father[j];
            for(int j = c; j < VARIABLES_NO ;j++)
                child[j] = mother[j];
            childs.add(child);
        }
        for (int i = 0 ; i < childs.size() ;i++)
            state.population.set(parrents.get(i) , childs.get(i));

        return state;
    }

    public State mutation(State state){
        int totalGen = POPULATION_NO * VARIABLES_NO;
        int mutationNo = (int) (totalGen * MUTATION_RATE);
        for (int i = 0 ; i < mutationNo ; i++) {
            int rand = (int) (Math.random() * totalGen);
            int no = (int) (Math.random() * Math.abs(state.coefficients[VARIABLES_NO]));
            int population = rand / VARIABLES_NO;
            int person = rand % VARIABLES_NO;
            state.population.get(population)[person] = no;
        }
        return state;
    }

    public int goalTest(State state) {
        for(int i = 0; i<POPULATION_NO; i++)
            if(state.distanceFromGoal[i] == 0) {
                return i;
            }
        return -1;
    }

    public void showInfo(State state,int position) {
        int[] goal = state.population.get(position);
        for (int i=0; i<VARIABLES_NO ;i++)
            System.out.print(goal[i] + " ");
        System.out.println();

        double[] time = new double[bests.size()];
        for (int i = 0 ; i < time.length ; i++)
            time[i] = i;

        double[] best = new double[bests.size()];
        for (int i = 0 ; i < best.length ;i++)
            best[i] = bests.get(i);

        double[] worst = new double[worsts.size()];
        for (int i = 0 ; i < worst.length ;i++)
            worst[i] = worsts.get(i);

        double[] average = new double[averages.size()];
        for (int i = 0 ; i < average.length ;i++)
            average[i] = averages.get(i);

        Chart chart = QuickChart.getChart("GA", "Time", "Best Merit", "y(x)", time, best);
        new SwingWrapper(chart).displayChart();

        Chart chart1 = QuickChart.getChart("GA", "Time", "Worst Merit", "y(x)", time, worst);
        new SwingWrapper(chart1).displayChart();

        Chart chart2 = QuickChart.getChart("GA", "Time", "Average Merit", "y(x)", time, average);
        new SwingWrapper(chart2).displayChart();
    }

    public void update(State state) {
        state.distanceFromGoal = evaluation(state);
    }
    public State nextGen(State state){

        state.fitnesses = new float[POPULATION_NO];
        state.cumulative = new float[POPULATION_NO];
        state.probabilities = new float[POPULATION_NO];
        Arrays.fill(state.fitnesses , 0);
        state.bestMerit = Float.MAX_VALUE;
        state.worstMerit = 0;
        state.averageMerit = 0;

        state = selection(state);
        state = mutation(state);
        state.distanceFromGoal = evaluation(state);
        return state;
    }
}
