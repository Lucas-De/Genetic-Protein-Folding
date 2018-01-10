import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class Main {
	final static int POPULATION = 1000;
	final static int CARRYOVER = 200;
	final static String chain = "HHPHPHPHPHPHHPHPPPHPPPHPPPPHPPPHPPHPHHPHPHPHPHPHPH";
	static GraphicsOnly app;
	static Folding[] generation;
	static int gen;

	public static void main(String[] args) {
		initialize();
		simulate();
	}

	static void initialize() {
		generation = new Folding[POPULATION];
		gen = 0;
		int consecutiveH = consecutiveH();
		System.out.println(consecutiveH);
		for (int i = 0; i < POPULATION; i++) {
			generation[i] = new Folding(chain);
			generation[i].consecutiveH = consecutiveH;
		}

		JFrame frame = new JFrame();
		int size = chain.length() * 2;
		app = new GraphicsOnly(frame, generation[0].graphics(), size);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(app.getGui());
		app.setImage(app.image);
		frame.getContentPane().add(app.getControl(), "Last");
		frame.setSize(app.width, app.height);
		frame.setLocation(200, 200);
		frame.setVisible(true);

	}

	static int consecutiveH() {
		int count = 0;
		for (int i = 0; i < chain.length() - 1; i++) {
			if (chain.charAt(i) == 'H' && chain.charAt(i + 1) == 'H')
				count++;
		}
		return count;
	}

	static void simulate() {

		int max = 0;
		char[][] maxSolution = null;
		while (gen < 10000) {

			for (int i = 0; i < POPULATION; i++) {
				if (generation[i].stability > max) {
					max = generation[i].stability;
					char[][] temp = generation[i].graphics();
					maxSolution = new char[temp.length][temp.length];
					for (int x = 0; x < temp.length; x++) {
						for (int y = 0; y < temp.length; y++) {
							maxSolution[x][y] = temp[x][y];
						}
					}
				}

			}

			int genMax = 0;
			int genIndex = 0;
			for (int i = 0; i < POPULATION; i++) {
				if (generation[i].stability >= genMax) {
					genIndex = i;
					genMax = generation[i].stability;
				}
			}

			gen++;
			app.refreshImage(generation[genIndex].graphics(),
					"Max: " + max + " Stability: " + generation[genIndex].stability + " Gen: " + gen);

			sleep(10);

			nextGen_wheel();
		}
		app.refreshImage(maxSolution, "Max: " + max);

	}

	static void nextGen() {
		Folding[] top = top(generation);
		for (int i = 0; i < POPULATION - CARRYOVER; i++) {
			generation[i] = top[Math.floorMod(i, CARRYOVER)].CopyObject();
			generation[i].mutate();
		}
		for (int i = POPULATION - CARRYOVER; i < POPULATION; i++) {
			generation[i] = top[Math.floorMod(i, CARRYOVER)].CopyObject();
		}
	}
	
	static void nextGen_wheel() {
		Folding[] parents = roulette_wheel_selection(CARRYOVER);
		for (int i = 0; i < POPULATION - CARRYOVER; i++) {
			generation[i] = parents[Math.floorMod(i, CARRYOVER)].CopyObject();
			generation[i].mutate();
		}
	}

	static Folding[] roulette_wheel_selection(int parentNumber) {
		Folding[] selection = new Folding[parentNumber];
		
		int S=0;
		for(int i=0;i<POPULATION;i++) {
			S+=(generation[i].stability+1);
		}
		
		int j;
		int P;
		double rand;
		for(int i=0;i<parentNumber;i++) {
			P=0;
			j=0;
			rand= Math.random()*S;
			while(P<rand){
				P+=(generation[j].stability+1);
				j++;
			}
			selection[i]= generation[j-1];
		}
		return selection;
	}

	static Folding[] top(Folding[] generation) {
		Folding[] top = new Folding[CARRYOVER];
		int newSmallest = 10000000;
		int newSmallestIndex = 0;
		for (int i = 0; i < POPULATION; i++) {
			if (i < CARRYOVER) {
				top[i] = generation[i];
				if (top[i].stability < newSmallest) {
					newSmallest = top[i].stability;
					newSmallestIndex = i;
				}
			} else if (generation[i].stability > newSmallest) {
				top[newSmallestIndex] = generation[i];
				newSmallest = top[0].stability;
				newSmallestIndex = 0;
				for (int j = 1; j < CARRYOVER; j++) {
					if (top[j].stability < newSmallest) {
						newSmallestIndex = j;
						newSmallest = top[j].stability;
					}
				}
			}
		}
		return top;
	}

	static void sleep(int t) {
		try {
			TimeUnit.MILLISECONDS.sleep(t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
