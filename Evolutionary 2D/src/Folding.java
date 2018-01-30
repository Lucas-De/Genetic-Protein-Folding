import java.util.ArrayList;
import java.util.Random;

public class Folding {
	amino[] foldingArray;
	int cumul;
	int len;
	int stability = 0;
	int consecutiveH;
	int consecutiveC;
	int consecutiveHC;
	int protection;
	static char[][] canvas;
	int score;
	ArrayList<Integer> pos = new ArrayList<Integer>();
	Random random = new Random();

	Folding(amino[] foldingArray, int len) {
		this.foldingArray = foldingArray;
		this.len = len;
		if(canvas==null)canvas = new char[len * 2 + 10][len * 2 + 10];
	}

	Folding(String chain) {

		len = chain.length();
		if(canvas==null)canvas = new char[len * 2 + 10][len * 2 + 10];
		foldingArray = new amino[len];
		for (int i = 0; i < len; i++) {
			foldingArray[i] = new amino(chain.charAt(i), 2);
		}

	}

	void mutate_type4() {
		int l = 6+random.nextInt(6);
		int s = random.nextInt(len-l-1)+1;
		int e = (l + s < len) ? l + s : len - 1;
		Setup_Canvas_for_redraw(s, e);
		for(int a=0;a<canvas.length;a++){
			for(int b=0;b<canvas.length;b++){
				canvas[a][b]=0;
			}
		}
	} 


	void redraw(int s, int e, int[] S,int[] E, int distX,int distY) {
		int x=S[0];
		int y=S[1];
		int remaining_amino=e-s+1;
		int flex=remaining_amino-Math.abs(distX)-Math.abs(distY);
		int[] amino=new int[len];
		for(int i=0;i<len;i++){
			amino[i]=foldingArray[i].nextposition;
		}
		
		for(int i=s-1;i<e;i++){
			if(canvas[x-1][y]==0 && (distX<0 || flex/2>=1)) {pos.add(0);}
			if(canvas[x][y+1]==0 && (distY>0 || flex/2>=1)) {pos.add(1);}
			if(canvas[x+1][y]==0 && (distX>0 || flex/2>=1)) {pos.add(2);}
			if(canvas[x][y-1]==0 && (distY<0 || flex/2>=1)) {pos.add(3);}
			
			if(pos.size()!=0) {
				int newPos=pos.get(random.nextInt(pos.size()));
				pos.clear();
				foldingArray[i].nextposition=newPos;

				
				switch(newPos) {
				case 0:distX++;x-=1; break;
				case 1:distY--;y+=1; break;
				case 2:distX--;x+=1; break;
				case 3:distY++;y-=1; break;
				}
				canvas[x][y]=foldingArray[i+1].type;

				
				remaining_amino--;
				flex=1+remaining_amino-Math.abs(distX)-Math.abs(distY);
			}else {
				for(int j=0;j<len;j++){
					foldingArray[j].nextposition=amino[j];
				}
				return;
			}

		}
		if(distX==1) { foldingArray[e].nextposition=2;}
		else if(distX==-1) { foldingArray[e].nextposition=0;}
		else if(distY==1) { foldingArray[e].nextposition=1;}
		else if(distY==-1) { foldingArray[e].nextposition=3;}


		getStability(foldingArray);
	}
	
	void Setup_Canvas_for_redraw(int s,int e) {
		for(int a=0;a<canvas.length;a++){
			for(int b=0;b<canvas.length;b++){
				canvas[a][b]=0;
			}
		}
		int x=len+1;
		int y=len+1;
		int[] S= new int[2];
		int[] E= new int[2];;

		for(int i=0;i<len;i++){
			if(i<s || i>e) {canvas[x][y]=foldingArray[i].type;}
			if(i==s-1) { S[0]=x;S[1]=y;}
			if(i==e+1) { E[0]=x;E[1]=y;}
			switch(foldingArray[i].nextposition) {
			case 0: x-=1;break;
			case 1: y+=1;break;
			case 2: x+=1;break;
			case 3: y-=1;break;
			}
		}
		
		


		int distX=E[0]-S[0];
		int distY=E[1]-S[1];

		
		redraw(s,e,S,E,distX,distY);
		}

	void straighten() {
		for (int i = 0; i < len; i++) {
			foldingArray[i].nextposition = 2;
		}
		stability = 0;
	}

	boolean foldLeft(int aminoIndex) {
		for (int i = aminoIndex; i < len; i++) {
			int n = foldingArray[i].nextposition + 1;
			if (n == 4)
				n = 0;
			foldingArray[i].nextposition = n;
		}
		if (checkOverlap(foldingArray) == false) {
			return true;
		}
		for (int i = aminoIndex; i < len; i++) {
			int n = foldingArray[i].nextposition - 1;
			if (n == -1)
				n = 3;
			foldingArray[i].nextposition = n;
		}
		return false;
	}

	void Krank() {
		int krankcount = 0;
		int post;
		int nxt;
		for (int i = 1; i < len - 1; i++) {
			post = foldingArray[i - 1].nextposition;
			nxt = foldingArray[i + 1].nextposition;
			if (post == nxt + 2 || post == nxt - 2) {
				krankcount++;
			}
		}
		if (krankcount == 0) {
			mutate();
			return;
		}
		int rand = (int) Math.floor(Math.random() * krankcount);
		krankcount = 0;
		for (int i = 1; i < len - 1; i++) {
			post = foldingArray[i - 1].nextposition;
			nxt = foldingArray[i + 1].nextposition;
			if (post == nxt + 2 || post == nxt - 2) {
				if (krankcount == rand) {
					foldingArray[i - 1].nextposition = nxt;
					foldingArray[i + 1].nextposition = post;
					if (checkOverlap(foldingArray)) {
						foldingArray[i - 1].nextposition = post;
						foldingArray[i + 1].nextposition = nxt;
						mutate();
						return;
					}
				}
				krankcount++;
			}
		}

	}

	boolean foldRight(int aminoIndex) {
		for (int i = aminoIndex; i < len; i++) {
			int n = foldingArray[i].nextposition - 1;
			if (n == -1)
				n = 3;
			foldingArray[i].nextposition = n;
		}
		if (checkOverlap(foldingArray) == false) {
			return true;
		}

		for (int i = aminoIndex; i < len; i++) {
			int n = foldingArray[i].nextposition + 1;
			if (n == 4)
				n = 0;
			foldingArray[i].nextposition = n;
		}
		return false;

	}

	void eraseCanvas(amino[] aminoArray) {
		int x = len + 1;
		int y = len + 1;

		for (int i = 0; i < len; i++) {
			canvas[x][y] = 0;
			switch (aminoArray[i].nextposition) {
			case 0:
				x -= 1;
				break;
			case 1:
				y += 1;
				break;
			case 2:
				x += 1;
				break;
			case 3:
				y -= 1;
				break;
			}
		}
	}

	boolean checkOverlap(amino[] aminoArray) {
		int x = len + 1;
		int y = len + 1;

		for (int i = 0; i < len-1; i++) {
			canvas[x][y] = aminoArray[i].type;
			switch (aminoArray[i].nextposition) {
			case 0:
				x -= 1;
				break;
			case 1:
				y += 1;
				break;
			case 2:
				x += 1;
				break;
			case 3:
				y -= 1;
				break;
			}
			if (canvas[x][y] != 0) {
				eraseCanvas(aminoArray);
				return true;
			}
		}
		canvas[x][y] = aminoArray[len-1].type;
		getStability(aminoArray);
		eraseCanvas(aminoArray);
		return false;
	}

	void getStability(amino[] aminoArray) {
		score = 0;
		int x = len + 1;
		int y = len + 1;

		for (int i = 0; i < len; i++) {
			if (canvas[x][y] == 'H') {
				if (canvas[x - 1][y] == 'H')
					score++;
				if (canvas[x + 1][y] == 'H')
					score++;
				if (canvas[x][y + 1] == 'H')
					score++;
				if (canvas[x][y - 1] == 'H')
					score++;
			}
			if (canvas[x][y] == 'C') {
				if (canvas[x - 1][y] == 'C')
					score += 5;
				if (canvas[x + 1][y] == 'C')
					score += 5;
				if (canvas[x][y + 1] == 'C')
					score += 5;
				if (canvas[x][y - 1] == 'C')
					score += 5;
				if (canvas[x - 1][y] == 'H')
					score += 2;
				if (canvas[x + 1][y] == 'H')
					score += 2;
				if (canvas[x][y + 1] == 'H')
					score += 2;
				if (canvas[x][y - 1] == 'H')
					score += 2;
			}

			switch (aminoArray[i].nextposition) {
			case 0:
				x -= 1;
				break;
			case 1:
				y += 1;
				break;
			case 2:
				x += 1;
				break;
			case 3:
				y -= 1;
				break;
			}
		}
		stability = (int) (score / 2.0) - consecutiveH - consecutiveC * 5 - consecutiveHC;
	}

	void mutate() {
		int mutationLocation = (int) Math.floor(Math.random() * len);

		if (Math.random() < 0.5) {
			if (!foldLeft(mutationLocation))
				mutate();
		} else {
			if (!foldRight(mutationLocation))
				mutate();
		}

	}
	

	void mutate_type3() {
		int mutationLocation = (int) Math.floor(Math.random() * len);

		if (Math.random() < 0.5) {
			if (!foldLeft(mutationLocation)) {
				mutate();
			} else if (mutationLocation + 1 < len) {
				foldLeft(mutationLocation + 1);
			}
		} else {
			if (!foldRight(mutationLocation)) {
				mutate();
			} else if (mutationLocation - 1 > 0) {
				foldRight(mutationLocation - 1);
			}
		}

	}

	amino[] copy(amino[] original) {
		amino[] copy = new amino[original.length];
		for (int i = 0; i < original.length; i++) {
			copy[i] = new amino(original[i].type, original[i].nextposition);
		}
		return copy;
	}

	Folding CopyObject() {
		amino[] copyAmino = copy(this.foldingArray);
		Folding copy = new Folding(copyAmino, len);
		copy.consecutiveC = this.consecutiveC;
		copy.consecutiveH = this.consecutiveH;
		copy.stability = this.stability;
		return copy;
	}

	char[][] graphics() {
		char[][] canvas = new char[len * 2 + 1][len * 2 + 1];
		int x = len + 1;
		int y = len + 1;
		for (int i = 0; i < len; i++) {
			canvas[x][y] = foldingArray[i].type;
			switch (foldingArray[i].nextposition) {
			case 0:
				x -= 1;
				break;
			case 1:
				y += 1;
				break;
			case 2:
				x += 1;
				break;
			case 3:
				y -= 1;
				break;
			}

		}
		return canvas;
	}
}
