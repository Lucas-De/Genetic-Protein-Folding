
public class Folding {
	amino[] foldingArray;
	int len;
	int stability=0;
	int consecutiveH;
	int consecutiveC;

    Folding(amino[] foldingArray,int len,int consecutiveH){
		this.foldingArray=foldingArray;
		this.len=len;
		this.consecutiveH=consecutiveH;
	}
	Folding(String chain){
		len=chain.length();
		foldingArray=new amino[len];
		for(int i=0;i<len;i++){
			foldingArray[i]=new amino(chain.charAt(i),2);
		}

		
	}
	
	boolean foldLeft(int aminoIndex){
		amino[] temp= copy(foldingArray);
		for(int i=aminoIndex;i<len;i++){
			temp[i].nextposition=Math.floorMod((temp[i].nextposition+1),4);
		}
		if(checkOverlap(temp)==false) {
			foldingArray=copy(temp);
			return true;
		}
		return false;
	}
	
	
	boolean foldRight(int aminoIndex) {
		amino[] temp= copy(foldingArray);
		for(int i=aminoIndex;i<len;i++){
			temp[i].nextposition=Math.floorMod((temp[i].nextposition-1),4);
		}
		if(checkOverlap(temp)==false) {
			foldingArray=copy(temp);
			return true;
		}
		return false;
		
	}
	int score;
	boolean checkOverlap(amino[] aminoArray){
		char[][] canvas= new char[len*2+10][len*2+10];
		int x=len+1;
		int y=len+1;
		
		for(int i=0;i<len;i++){
			canvas[x][y]=aminoArray[i].type;
			switch(aminoArray[i].nextposition) {
			case 0: x-=1;break;
			case 1: y+=1;break;
			case 2: x+=1;break;
			case 3: y-=1;break;
			}
			if(canvas[x][y]!=0) {
				return true;
			}
		}
		getStability(canvas,aminoArray);
		return false;
	}
	void getStability(char[][] canvas,amino[] aminoArray) {
		score=0;
		int x=len+1;
		int y=len+1;
		
		
		for(int i=0;i<len;i++){
			if(canvas[x][y]=='H'){
				if(canvas[x-1][y]=='H') score++;
				if(canvas[x+1][y]=='H') score++;
				if(canvas[x][y+1]=='H') score++;
				if(canvas[x][y-1]=='H') score++;
				}
			
			
			switch(aminoArray[i].nextposition) {
			case 0: x-=1;break;
			case 1: y+=1;break;
			case 2: x+=1;break;
			case 3: y-=1;break;
			}
		}
		stability=(int) (score/2.0)-consecutiveH;
	}
	void mutate() {
     	int mutationLocation=(int) Math.floor(Math.random()*len);
     	
 		if(Math.random()<0.5) {
 			if(!foldLeft(mutationLocation))mutate();
 		}else {
 			if(!foldRight(mutationLocation))mutate();
 		}
 }
	
	amino[] copy(amino[] original) {
		amino[] copy= new amino[original.length];
		for(int i=0;i<original.length;i++){
			copy[i]=new amino(original[i].type,original[i].nextposition);
		}
		return copy;
	}
	
	Folding CopyObject() {
		amino[] copyAmino= copy(this.foldingArray);
		Folding copy= new Folding(copyAmino,len,consecutiveH);
		copy.stability = this.stability;
		return copy;
	}
	


	
	char[][] graphics() {
		char[][] canvas= new char[len*2+1][len*2+1];
		int x=len+1;
		int y=len+1;
		for(int i=0;i<len;i++){
			canvas[x][y]=foldingArray[i].type;
			switch(foldingArray[i].nextposition) {
			case 0: x-=1;break;
			case 1: y+=1;break;
			case 2: x+=1;break;
			case 3: y-=1;break;
			}

			
			
		}
		return canvas;
	}
}
