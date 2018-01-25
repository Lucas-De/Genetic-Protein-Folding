import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class Main {


    // Probability of each mutation type occurring. (Note that PROB_TYPE2 is
    // interpolated from values below)
    final static double alpha = 0.8;
    final static double beta = 0.1;
    final static int repeat = 10000;
    static int max_stability = 0;

    // For visual, please change the boolean to True
    final static boolean GRAPHICS=false;

    static String chain = "PPHPPHHPPHHPPPPPHHHHHHHHHHPPPPPPHHPPHHPPHPPHHHHH";
    static GraphicsOnly app;
    static Folding protein;

    public static void main(String[] args) {
        for (int k = 0; k < repeat; k++){
        initialize();
        }
        System.out.println("End");
        System.out.println("Max Stability : "+ max_stability);
    }

    static void initialize() {
        protein = new Folding(chain);
        protein.consecutiveH = consecutiveH();
        protein.consecutiveC = consecutiveC();
        Folding protein_left = new Folding(chain);
        Folding protein_right = new Folding(chain);
        copy(protein_left,protein);
        copy(protein_right,protein);
        for(int i = 0; i < chain.length(); i++){
            double probability = Math.random();
            int stability = Math.max(protein.stability, Math.max(left_fold_score(protein_left,i), right_fold_score(protein_right, i)));
            if (stability == left_fold_score(protein_left,i)){
                if (probability <= alpha){ protein.foldLeft(i); }
                if (alpha < probability && probability <= alpha+beta){ protein.foldRight(i); }
                if (probability > alpha+beta){ continue; }
            }
            else if (stability == right_fold_score(protein_right,i)){
                if (probability <= alpha){ protein.foldRight(i); }
                if (alpha < probability && probability <= alpha+beta){ protein.foldLeft(i); }
                if (probability > alpha+beta){ continue; }
            }
            else if (stability == protein.stability){
                if (probability <= alpha){ continue; }
                if (alpha < probability && probability <= alpha+beta){ protein.foldLeft(i); }
                if (probability > alpha+beta){ protein.foldRight(i); }
                continue;
            }
            else {
                System.out.println(protein.stability + " Something is wrong!");

            }
            copy(protein_left,protein);
            copy(protein_right,protein);
            if (protein.stability > max_stability){
                max_stability = protein.stability;
            }

        }
        if (protein.stability > 0){ System.out.print(protein.stability + ", ");}



        if(GRAPHICS) {
            JFrame frame = new JFrame();
            int size = chain.length() * 2;
            app = new GraphicsOnly(frame, protein.graphics(), size);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(app.getGui());
            app.setImage(app.image);
            frame.getContentPane().add(app.getControl(), "Last");
            frame.setSize( 500, 500);
            frame.setLocation(250, 250);
            frame.setVisible(true);
            app.scale=7;
            app.paintImage();
        }
    }

    static int left_fold_score(Folding current_protein, int i){
//        Folding lefty = new Folding(chain);
//        copy(lefty,current_protein);
        Folding lefty = current_protein;
        lefty.consecutiveH = consecutiveH();
        lefty.consecutiveC = consecutiveC();
        lefty.foldLeft(i);
        return lefty.stability;
    }

    static int right_fold_score(Folding current_protein, int i){
//        Folding righty = new Folding(chain);
//        copy(righty,current_protein);
        Folding righty = current_protein;
        righty.consecutiveH = consecutiveH();
        righty.consecutiveC = consecutiveC();
        righty.foldRight(i);
        return righty.stability;
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
		child.stability=parent.stability;
		for(int j=0;j<parent.len;j++){
			child.foldingArray[j].nextposition=parent.foldingArray[j].nextposition;
		}
	}



//	static void sleep(int t) {
//		try {
//			TimeUnit.MILLISECONDS.sleep(t);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
