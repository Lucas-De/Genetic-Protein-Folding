import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class Main {
	final static int POPULATION = 10000;
	final static int CARRYOVER =  2000;
	final static String given3="CPPC";
	static String chain;
	static GraphicsOnly app;
	static Folding[] generation;
	static int gen;

	public static void main(String[] args) {
		long startTime;
		long endTime;
		long duration;
		initialize();
		for (int i = 0; i < 1; i++) {
			startTime = System.nanoTime();
			simulate();
			startTime = System.nanoTime();
			endTime = System.nanoTime();
			duration = (endTime - startTime);
			System.out.println("Iteration #"+i+" ended in " + duration/1000000.0 +"ms");
		}	
	}

	static void initialize(){
		chain=given3;
		generation = new Folding[POPULATION];
		gen = 0;
		int consecutiveH = consecutiveH();
		int consecutiveC = consecutiveC();
		int consecutiveHC = consecutiveHC();
		for (int i = 0; i < POPULATION; i++) {
			generation[i] = new Folding(chain);
			generation[i].consecutiveH = consecutiveH;
			generation[i].consecutiveC = consecutiveC;
			generation[i].consecutiveHC = consecutiveHC;
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
//static int max ;
	static int simulate() {

		int max = 0;
		char[][] maxSolution = null;
		int[][] Coordinates= new int[chain.length()][3];
		while (gen < 10) {

			for (int i = 0; i < POPULATION; i++) {
				if (generation[i].stability > max) {
					int[][] temp2= generation[i].coordinates();
					
					for (int x = 0; x < chain.length(); x++) {
						for (int y = 0; y < 3; y++) {
							Coordinates[x][y] = temp2[x][y];
						}
					}
					
					
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
			app.refreshImage(generation[0].graphics(),
					"Max: " + max + " Stability: " + generation[0].stability + " Gen: " + gen);
			

			nextGen_wheel(true);
			if(max==23)break;
		}
		app.refreshImage(maxSolution, "Max: " + max);
		
		
		System.out.print("[");
		for (int i = 0; i < chain.length(); i++) {
			System.out.print(Coordinates[i][0]+",");
		}
		System.out.print("] , [");
		for (int i = 0; i < chain.length(); i++) {
			System.out.print(Coordinates[i][1]+",");
		}
		System.out.print("] , [");
	
		for (int i = 0; i < chain.length(); i++) {
			System.out.print(Coordinates[i][2]+",");
		}
		System.out.print("]");
		System.out.println();
		return max;
	}

	static void nextGen() {
		Folding[] top = top(generation,CARRYOVER);

		for (int i = 0; i < POPULATION - CARRYOVER; i++) {
			generation[i] = top[Math.floorMod(i, CARRYOVER)].CopyObject();
			generation[i].mutate();
		}
		for (int i = POPULATION - CARRYOVER; i < POPULATION; i++) {
			generation[i] = top[Math.floorMod(i, CARRYOVER)].CopyObject();
		}
	}
	
	static void nextGen_wheel(boolean SELECTTOP) {
		Folding[] top = top(generation,10);
		Folding[] parents = roulette_wheel_selection(CARRYOVER);
		for (int i = 0; i < POPULATION; i++) {
			generation[i] = parents[i%CARRYOVER].CopyObject();
			double rand=Math.random();
			
//			generation[i].mutate(); 
//			
//			if(rand<0.9) {generation[i].mutate();} 
//			else {generation[i].mutate_type2();}
//			
			if(rand<0.9) {generation[i].mutate();} 
			else {generation[i].mutate_type3();}
			

			
		}
		
		if (SELECTTOP) {
			for (int i = 0; i < 10; i++) {
				generation[(int) Math.floor(Math.random()*POPULATION)] = top[i].CopyObject();
			}

			
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
			
			selection[i]= generation[j-1].CopyObject();
		}
		return selection;
	}
	static int consecutiveHC() {
		int count = 0;
		for (int i = 0; i < chain.length() - 1; i++) {
			if (chain.charAt(i) == 'H' && chain.charAt(i + 1) == 'C')
				count++;
			if (chain.charAt(i) == 'C' && chain.charAt(i + 1) == 'H')
				count++;
		}
		return count;
	}


	static Folding[] top(Folding[] generation,int num) {
		Folding[] top = new Folding[num];
		int newSmallest = 10000000;
		int newSmallestIndex = 0;
		for (int i = 0; i < POPULATION; i++) {
			if (i < num) {
				top[i] = generation[i].CopyObject();
				if (top[i].stability < newSmallest) {
					newSmallest = top[i].stability;
					newSmallestIndex = i;
				}
			} else if (generation[i].stability > newSmallest) {
				top[newSmallestIndex] = generation[i].CopyObject();
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

	static void sleep(int t) {
		try {
			TimeUnit.MILLISECONDS.sleep(t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
