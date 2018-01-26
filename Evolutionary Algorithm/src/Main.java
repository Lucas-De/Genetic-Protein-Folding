import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class Main {
	final static int POPULATION = 10000;
	final static int CARRYOVER = 2000; // number of parents selected to generate children
	final static int TOP = 10; // the number of top proteins to be cloned to the next generation

	// Probability of each mutation type occurring. (Note that A retracing mutation
	// is
	// extrapolation from values below)
	final static double PROB_TYPE1 = 0.8; // Single Rotation probability
	final static double PROB_TYPE2 = 0; // Double Rotation probability
	final static double PROB_TYPE3 = 0.2; // Crank

	final static boolean PROTECTION = false; // True: Child protection is activated
	final static int PROTECTION_GEN = 10; // Number of generations for which to protect top children

	final static boolean GRAPHICS = false;

	static String chain = "PPHHHPHHHHHHHHPPPHHHHHHHHHHPHPPPHHHHHHHHHHHHPPPPHHHHHHPHHPHP";
	static GraphicsOnly app;
	static Folding[] generation;
	static Folding[] selection = new Folding[CARRYOVER];
	static Folding[] top = new Folding[TOP];
	static int gen;

	public static void main(String[] args) {
		long startTime;
		long endTime;
		long duration;
		initialize();

		for (int i = 0; i < 100; i++) {
			startTime = System.nanoTime();

			simulate();
			reset();
			endTime = System.nanoTime();
			duration = (endTime - startTime);
			// System.out.println("Iteration #"+i+" ended in " + duration/1000000.0 +"ms");

		}
	}

	static void initialize() {
		generation = new Folding[POPULATION];
		gen = 0;
		int consecutiveH = consecutiveH();
		int consecutiveC = consecutiveC();

		for (int i = 0; i < POPULATION; i++) {
			generation[i] = new Folding(chain);
			generation[i].consecutiveH = consecutiveH;
			generation[i].consecutiveC = consecutiveC;
		}
		for (int i = 0; i < CARRYOVER; i++) {
			selection[i] = new Folding(chain);
			selection[i].consecutiveH = consecutiveH;
			selection[i].consecutiveC = consecutiveC;
		}
		for (int i = 0; i < TOP; i++) {
			top[i] = new Folding(chain);
			top[i].consecutiveH = consecutiveH;
			top[i].consecutiveC = consecutiveC;
		}

		if (GRAPHICS) {
			JFrame frame = new JFrame();
			int size = chain.length() * 2;
			app = new GraphicsOnly(frame, generation[0].graphics(), size);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setContentPane(app.getGui());
			app.setImage(app.image);
			frame.getContentPane().add(app.getControl(), "Last");
			frame.setSize(500, 500);
			frame.setLocation(250, 250);
			frame.setVisible(true);
			app.scale = 7;
			app.paintImage();
		}
	}

	static int simulate() {

		Folding M = new Folding(chain);
		int sinceLast = 0;
		int prev = 0;
		int genAtMax = 0;
		while (gen < 1000) {
			gen++;
			boolean newMax = false;

			for (int i = 0; i < POPULATION; i++) {
				if (generation[i].stability > M.stability) {
					M = generation[i];
					newMax = true;
				}
			}
			if (newMax) {
				M = M.CopyObject();
				genAtMax = gen;
				prev = sinceLast;
				sinceLast = 0;
			} else {
				sinceLast++;
			}

			int genMax = 0;
			int genIndex = 0;
			for (int i = 0; i < POPULATION; i++) {
				if (generation[i].stability > genMax) {
					genIndex = i;
					genMax = generation[i].stability;
				}
			}

			if (GRAPHICS) {
				// app.refreshImage(generation[genIndex].graphics(),"Max: " + M.stability + "
				// Stability: " + generation[genIndex].stability + " Gen: " + gen);
			}

			nextGen();
			
		}

		if (GRAPHICS) {
			app.refreshImage(M.graphics(), "Max: " + M.stability);
		}
		System.out.println(M.stability + " , " + genAtMax + "  , " + prev);
		// System.out.println(M.stability);
		return M.stability;

	}

	static void nextGen() {
		Folding[] top = top(generation, TOP);
		Folding[] parents = roulette_wheel_selection(CARRYOVER);

		for (int i = 0; i < POPULATION; i++) {
			if (generation[i].protection == 0) {
				copy(generation[i], parents[i % CARRYOVER]);

			} else {
				generation[i].protection--;
			}

			double rand = Math.random();

			if (rand < PROB_TYPE1) {
				generation[i].mutate();
			} else if (rand < PROB_TYPE1 + PROB_TYPE2) {
				generation[i].mutate_type3();
			} else if (rand < PROB_TYPE1 + PROB_TYPE2 + PROB_TYPE3) {
				generation[i].Krank();
			} else {
				generation[i].mutate_type4();
			}

		}
		int i = 0;
		if (PROTECTION == true) {
			while (i < TOP) {
				int rand = (int) Math.floor(Math.random() * POPULATION);

				if (generation[rand].protection == 0) {
					copy(generation[rand], top[i]);
					generation[rand].mutate();
					generation[rand].protection = PROTECTION_GEN;
					i++;
				}
			}
			i = 0;
		}
		while (i < TOP) {
			int rand = (int) Math.floor(Math.random() * POPULATION);
			if (generation[rand].protection == 0) {
				copy(generation[rand], top[i]);
				i++;
			}
		}

	}

	static Folding[] roulette_wheel_selection(int parentNumber) {

		int S = 0;
		for (int i = 0; i < POPULATION; i++) {
			S += (generation[i].stability + 1);
			generation[i].cumul = S;
		}

		int j;
		int P;
		int indexL;
		int indexR;
		double rand;
		for (int i = 0; i < parentNumber; i++) {
			P = 0;
			indexR = POPULATION;
			indexL = 0;
			rand = Math.random() * S;
			while (indexR - indexL > 1) {
				j = (indexL + indexR) / 2;
				P = generation[j].cumul;
				if (P >= rand)
					indexR = j;
				else
					indexL = j;
				// System.out.println(indexL +" , "+indexR+" , "+j);

			}
			copy(selection[i], generation[indexR]);
		}
		return selection;
	}

	static Folding[] top(Folding[] generation, int num) {
		int newSmallest = 10000000;
		int newSmallestIndex = 0;
		for (int i = 0; i < POPULATION; i++) {
			if (i < num) {
				copy(top[i], generation[i]);
				if (top[i].stability < newSmallest) {
					newSmallest = top[i].stability;
					newSmallestIndex = i;
				}
			} else if (generation[i].stability > newSmallest) {
				copy(top[newSmallestIndex], generation[i]);
				newSmallest = top[0].stability;
				newSmallestIndex = 0;
				for (int j = 1; j < num; j++) {
					if (top[j].stability < newSmallest) {
						newSmallestIndex = j;
						newSmallest = top[j].stability;
					}
				}
			}
		}

		return top;
	}

	static void reset() {
		gen = 0;
		for (int i = 0; i < generation.length; i++) {
			generation[i].straighten();
		}
	}

	static int consecutiveH() {
		int count = 0;
		for (int i = 0; i < chain.length() - 1; i++) {
			if (chain.charAt(i) == 'H' && chain.charAt(i + 1) == 'H')
				count++;
		}
		return count;
	}

	static int consecutiveC() {
		int count = 0;
		for (int i = 0; i < chain.length() - 1; i++) {
			if (chain.charAt(i) == 'C' && chain.charAt(i + 1) == 'C')
				count++;
		}
		return count;
	}

	static void copy(Folding child, Folding parent) {
		child.stability = parent.stability;
		for (int j = 0; j < child.len; j++) {
			child.foldingArray[j].nextposition = parent.foldingArray[j].nextposition;
		}
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
