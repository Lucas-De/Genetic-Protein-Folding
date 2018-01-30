import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class Main {


    // Probability of each mutation type occurring. (Note that PROB_TYPE2 is
    // interpolated from values below)
    final static double beta = 0.10;
    final static double alpha = (1.00 - 2.00 * beta);
    final static int repeat = 10000;
    static int max_stability = 0;


    // For visual, please change the boolean to True
    final static boolean GRAPHICS=false;



//    static String chain = "HHPHHHPHPHHHPH"; //6
//    static String chain = "HPHPPHHPHPPHPHHPPHPH"; //6
//    static String chain = "PPPHHPPHHPPPPPPHHHHHHHPPHHPPPPHHPPHPP"; //10
    static String chain = "HHPHPHPHPHPHHPHPPPHPPPHPPPPHPPPHPPHPHHPHPHPHPHPHPH"; //13



//    static String chain = "HPHPPHHPHPPHPHHPPHPH"; //HC=4 SA=6
//    static String chain = "PPPHHPPHHPPPPPHHHHHHHPPHHPPPPHHPPHPP"; //HC=6 SA=11
//    static String chain = "PPHPPHHPPHHPPPPPHHHHHHHHHHPPPPPPHHPPHHPPHPPHHHHH"; //HC=11 SA=18
//    static String chain = "PPHHHPHHHHHHHHPPPHHHHHHHHHHPHPPPHHHHHHHHHHHHPPPPHHHHHHPHHPHP"; //HC=23 SA=31

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
        Folding protein_no_fold = new Folding(chain);
        copy(protein_left,protein);
        copy(protein_right,protein);
        copy(protein_no_fold, protein);

        double smooth_alpha;
        double smooth_beta;

        for(int i = 0; i < chain.length(); i++){
            smooth_beta = beta * ( ((double)chain.length() - (double)i) / (double)chain.length() );
            smooth_alpha = 1.00 - 2.00 * smooth_beta;

            //System.out.println("prob1 " + prob_1 + ", "+ "prob2 " + prob_2+ ", "+i);


            double probability = Math.random();
            int stability = Math.max(no_fold_score(protein_no_fold, i), Math.max(left_fold_score(protein_left,i), right_fold_score(protein_right, i)));
            if (stability == left_fold_score(protein_left,i)){
                if (probability <= smooth_alpha){ protein.foldLeft(i); }
                if (smooth_alpha < probability && probability <= smooth_alpha+smooth_beta){ protein.foldRight(i); }
                if (probability > smooth_alpha+smooth_beta){ continue; }
            }
            else if (stability == right_fold_score(protein_right,i)){
                if (probability <= smooth_alpha){ protein.foldRight(i); }
                if (smooth_alpha < probability && probability <= smooth_alpha+smooth_beta){ protein.foldLeft(i); }
                if (probability > smooth_alpha+smooth_beta){ continue; }
            }
            else if (stability == no_fold_score(protein_no_fold, i)){
                if (probability <= smooth_alpha){ continue; }
                if (smooth_alpha < probability && probability <= smooth_alpha+smooth_beta){ protein.foldLeft(i); }
                if (probability > smooth_alpha+smooth_beta){ protein.foldRight(i); }
                continue;
            }
            else {
                System.out.println(protein.stability + " Something is wrong!");
                break;

            }
            copy(protein_left,protein);
            copy(protein_right,protein);
            copy(protein_no_fold, protein);
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
        Folding lefty = current_protein;
        lefty.consecutiveH = consecutiveH();
        lefty.consecutiveC = consecutiveC();

        //Folding to the left, keeping it straight after the first left fold
        lefty.foldLeft(i);

        //After folding to the left, folding to the left again
        Folding lefty_left = new Folding(chain);
        copy(lefty_left,lefty);
        lefty_left.consecutiveH = consecutiveH();
        lefty_left.consecutiveC = consecutiveC();
        lefty_left.foldLeft(i+1);

        //After folding to the left, folding to the right
        Folding lefty_right = new Folding(chain);
        copy(lefty_right,lefty);
        lefty_right.consecutiveH = consecutiveH();
        lefty_right.consecutiveC = consecutiveC();
        lefty_right.foldRight(i+1);

        //Sum of (left&left), (left&straight), and (left&right)
        return lefty.stability + lefty_left.stability + lefty_right.stability;
    }

    static int right_fold_score(Folding current_protein, int i){
        Folding righty = current_protein;
        righty.consecutiveH = consecutiveH();
        righty.consecutiveC = consecutiveC();

        //Folding to the right, keeping it straight after the first right fold
        righty.foldRight(i);

        //After folding to the right, folding to the left
        Folding righty_left = new Folding(chain);
        copy(righty_left,righty);
        righty_left.consecutiveH = consecutiveH();
        righty_left.consecutiveC = consecutiveC();
        righty_left.foldLeft(i+1);

        //After folding to the right, folding to the right again
        Folding righty_right = new Folding(chain);
        copy(righty_right,righty);
        righty_right.consecutiveH = consecutiveH();
        righty_right.consecutiveC = consecutiveC();
        righty_right.foldRight(i+1);

        //Sum of (right&left), (right&right), and (right&right)
        return righty.stability + righty_left.stability + righty_right.stability;
    }

    static int no_fold_score(Folding current_protein, int i){
        Folding no_fold = current_protein;
        no_fold.consecutiveH = consecutiveH();
        no_fold.consecutiveC = consecutiveC();

        //After no folding, folding to the left
        Folding no_fold_left = new Folding(chain);
        copy(no_fold_left,no_fold);
        no_fold_left.consecutiveH = consecutiveH();
        no_fold_left.consecutiveC = consecutiveC();
        no_fold_left.foldLeft(i+1);

        //After no folding, folding to the right
        Folding no_fold_right = new Folding(chain);
        copy(no_fold_right,no_fold);
        no_fold_right.consecutiveH = consecutiveH();
        no_fold_right.consecutiveC = consecutiveC();
        no_fold_right.foldRight(i+1);

        //Sum of (straight&left), (straight&straight), and (straight&right)
        return protein.stability + no_fold_left.stability + no_fold_right.stability;
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
}
