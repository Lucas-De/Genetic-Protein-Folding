import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class Main {
	final static int POPULATION = 100000;
	final static int CARRYOVER = 200;
	final static String chain = "HHPHPHPHPHPHHPHPPPHPPPHPPPPHPPPHPPHPHHPHPHPHPHPHPH";
	static GraphicsOnly app;
	static Folding[] generation;
	static int gen;
	static int counter = 0;

	public static void main(String[] args) {
		initialize();
		simulate();
		System.out.println("stop");
	}

	static void initialize() {
		generation = new Folding[POPULATION];
		gen = 0;
		int consecutiveH = consecutiveH();
		generation[0] = new Folding(chain);
		generation[0].consecutiveH = consecutiveH;
		
		Folding[] generationTwo = new Folding[1];
		generationTwo[0] = generation[0].CopyObject();
		
		int counterTwo = 0;
		int genCount = 1;
		int i = 0;
		while (Math.pow(3, i)<POPULATION) {
			i++;
			Folding[] generationThree = new Folding[(int) Math.pow(3, i)];
			for (int j = 0; j<generationTwo.length; j++) {
				generationThree[3*j] = generationTwo[j].CopyObject();
				generationThree[3*j].foldLeft(counter);
				generationThree[3*j+1] = generationTwo[j].CopyObject();
				generationThree[3*j+1].foldRight(counter);
				generationThree[3*j+2] = generationTwo[j].CopyObject();

			}
			counter++;
			generationTwo=generationThree;
//			generation[i].consecutiveH = consecutiveH;
		}
		generation = top(generationTwo);
		

		JFrame frame = new JFrame();
		int size = chain.length() * 2;
		app = new GraphicsOnly(frame, generation[9].graphics(), size);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(app.getGui());
		app.setImage(app.image);
		frame.getContentPane().add(app.getControl(), "Last");
		frame.setSize(app.width, app.height);
		frame.setLocation(200, 200);
		frame.setVisible(true);
//		app.refreshImage(generation[genIndex].graphics(),"Max: " + max + " Stability: " + generation[genIndex].stability + " Gen: " + gen);
		
	}
	
	static void simulate() {
		int maxLoop = chain.length();
		Folding[] temporaryGen = new Folding[3*POPULATION];
		Folding genMax;
		Folding totalMax = new Folding(chain);
		while(counter<maxLoop) {
			for(int j = 0; j<POPULATION; j++) {
				temporaryGen[3*j] = generation[j].CopyObject();
				temporaryGen[3*j].foldLeft(counter);
				temporaryGen[3*j+1] = generation[j].CopyObject();
				temporaryGen[3*j+1].foldRight(counter);
				temporaryGen[3*j+2] = generation[j].CopyObject();
			}
			generation = top(temporaryGen);
			genMax = max(generation);
			if(genMax.stability>totalMax.stability) {
				totalMax = genMax.CopyObject();
			}
//			app.refreshImage(genMax.graphics(),"Max: " + totalMax.stability + " Stability: " + genMax.stability + " Gen: " + counter);
			counter++;
//			System.out.println(counter);
//			sleep(100);
		}
		app.refreshImage(totalMax.graphics(),"Max: " + totalMax.stability + " Stability: " + totalMax.stability + " Gen: " + counter);
	}
	
	static Folding max(Folding[] generation) {
		Folding empty = generation[0];
		for(int i = 1; i<generation.length;i++) {
			if(generation[i].stability>empty.stability) {
				empty = generation[i];
			}
		}
		return empty.CopyObject();
	}

	static int consecutiveH() {
		int count = 0;
		for (int i = 0; i < chain.length() - 1; i++) {
			if (chain.charAt(i) == 'H' && chain.charAt(i + 1) == 'H')
				count++;
		}
		return count;
	}


//
//			sleep(10);

	static Folding[] top(Folding[] generation) {
		Folding[] top = new Folding[POPULATION];
		int newSmallest = 10000000;
		int newSmallestIndex = 0;
		for (int i = 0; i < POPULATION; i++) {
			if (i < POPULATION) {
				top[i] = generation[i];
				if (top[i].stability < newSmallest) {
					newSmallest = top[i].stability;
					newSmallestIndex = i;
				}
			} else if (generation[i].stability > newSmallest) {
				top[newSmallestIndex] = generation[i];
				newSmallest = top[0].stability;
				newSmallestIndex = 0;
				for (int j = 1; j < POPULATION; j++) {
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
