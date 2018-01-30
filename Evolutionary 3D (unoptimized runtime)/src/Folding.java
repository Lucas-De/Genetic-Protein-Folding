
public class Folding {
	amino[] foldingArray;
	int len;
	int stability = 0;
	int consecutiveH;
	int consecutiveC;
	int consecutiveHC;
	int score;

	Folding(amino[] foldingArray, int len) {
		this.foldingArray = foldingArray;
		this.len = len;
	}

	Folding(String chain) {
		len = chain.length();
		foldingArray = new amino[len];
		for (int i = 0; i < len; i++) {
			foldingArray[i] = new amino(chain.charAt(i), 2);
		}

	}

	void straighten() {
		for (int i = 0; i < len; i++) {
			foldingArray[i].nextposition = 2;
		}
		stability = 0;
	}

	boolean foldLeft(int aminoIndex) {
		amino[] temp = copy(foldingArray);
		for (int i = aminoIndex; i < len; i++) {
			temp[i].nextposition = Math.floorMod((temp[i].nextposition + 1), 4);
		}
		if (checkOverlap(temp) == false) {
			foldingArray = copy(temp);
			return true;
		}
		return false;
	}

	boolean foldRight(int aminoIndex) {
		amino[] temp = copy(foldingArray);
		for (int i = aminoIndex; i < len; i++) {
			temp[i].nextposition = Math.floorMod((temp[i].nextposition - 1), 4);
		}
		if (checkOverlap(temp) == false) {
			foldingArray = copy(temp);
			return true;
		}
		return false;

	}

	boolean foldUp(int aminoIndex) {
		amino[] temp = copy(foldingArray);

		if (temp[aminoIndex].nextposition == 2) {
			for (int i = aminoIndex; i < len; i++) {
				switch (temp[i].nextposition) {
				case 0:
					temp[i].nextposition = -10;
					break;
				case 2:
					temp[i].nextposition = 10;
					break;
				case 10:
					temp[i].nextposition = 0;
					break;
				case -10:
					temp[i].nextposition = 2;
					break;
				}
			}
		}

		if (temp[aminoIndex].nextposition == 0) {
			for (int i = aminoIndex; i < len; i++) {
				switch (temp[i].nextposition) {
				case 0:
					temp[i].nextposition = 10;
					break;
				case 2:
					temp[i].nextposition = -10;
					break;
				case 10:
					temp[i].nextposition = 2;
					break;
				case -10:
					temp[i].nextposition = 0;
					break;
				}
			}
		}

		if (temp[aminoIndex].nextposition == 1) {
			for (int i = aminoIndex; i < len; i++) {
				switch (temp[i].nextposition) {
				case 1:
					temp[i].nextposition = 10;
					break;
				case 3:
					temp[i].nextposition = -10;
					break;
				case 10:
					temp[i].nextposition = 3;
					break;
				case -10:
					temp[i].nextposition = 1;
					break;
				}
			}
		}

		if (temp[aminoIndex].nextposition == 3) {
			for (int i = aminoIndex; i < len; i++) {
				switch (temp[i].nextposition) {
				case 1:
					temp[i].nextposition = -10;
					break;
				case 3:
					temp[i].nextposition = 10;
					break;
				case 10:
					temp[i].nextposition = 1;
					break;
				case -10:
					temp[i].nextposition = 3;
					break;
				}
			}
		}
		double rand=Math.random();
		if (temp[aminoIndex].nextposition == -10) {
			if (rand < 0.25) {
				for (int i = aminoIndex; i < len; i++) {
					switch (temp[i].nextposition) {
					case 0:
						temp[i].nextposition = 10;
						break;
					case 2:
						temp[i].nextposition = -10;
						break;
					case 10:
						temp[i].nextposition = 2;
						break;
					case -10:
						temp[i].nextposition = 0;
						break;
					}
				}
			} else if (rand < 0.5) {
				for (int i = aminoIndex; i < len; i++) {
					switch (temp[i].nextposition) {
					case 1:
						temp[i].nextposition = 10;
						break;
					case 3:
						temp[i].nextposition = -10;
						break;
					case 10:
						temp[i].nextposition = 3;
						break;
					case -10:
						temp[i].nextposition = 1;
						break;
					}
				}
			} else if (rand < 0.75) {
				for (int i = aminoIndex; i < len; i++) {
					switch (temp[i].nextposition) {
					case 0:
						temp[i].nextposition = -10;
						break;
					case 2:
						temp[i].nextposition = 10;
						break;
					case 10:
						temp[i].nextposition = 0;
						break;
					case -10:
						temp[i].nextposition = 2;
						break;
					}
				}
			} else {
				for (int i = aminoIndex; i < len; i++) {
					switch (temp[i].nextposition) {
					case 1:
						temp[i].nextposition = -10;
						break;
					case 3:
						temp[i].nextposition = 10;
						break;
					case 10:
						temp[i].nextposition = 1;
						break;
					case -10:
						temp[i].nextposition = 3;
						break;
					}
				}
			}
		}
		
		

		if (checkOverlap(temp) == false) {
			foldingArray = copy(temp);
			return true;
		}
		return false;

	}

	boolean foldDown(int aminoIndex) {
		amino[] temp = copy(foldingArray);

		if (temp[aminoIndex].nextposition == 0) {
			for (int i = aminoIndex; i < len; i++) {
				switch (temp[i].nextposition) {
				case 0:
					temp[i].nextposition = -10;
					break;
				case 2:
					temp[i].nextposition = 10;
					break;
				case 10:
					temp[i].nextposition = 0;
					break;
				case -10:
					temp[i].nextposition = 2;
					break;
				}
			}
		}

		if (temp[aminoIndex].nextposition == 2) {
			for (int i = aminoIndex; i < len; i++) {
				switch (temp[i].nextposition) {
				case 0:
					temp[i].nextposition = 10;
					break;
				case 2:
					temp[i].nextposition = -10;
					break;
				case 10:
					temp[i].nextposition = 2;
					break;
				case -10:
					temp[i].nextposition = 0;
					break;
				}
			}
		}

		if (temp[aminoIndex].nextposition == 3) {
			for (int i = aminoIndex; i < len; i++) {
				switch (temp[i].nextposition) {
				case 1:
					temp[i].nextposition = 10;
					break;
				case 3:
					temp[i].nextposition = -10;
					break;
				case 10:
					temp[i].nextposition = 3;
					break;
				case -10:
					temp[i].nextposition = 1;
					break;
				}
			}
		}

		if (temp[aminoIndex].nextposition == 1) {
			for (int i = aminoIndex; i < len; i++) {
				switch (temp[i].nextposition) {
				case 1:
					temp[i].nextposition = -10;
					break;
				case 3:
					temp[i].nextposition = 10;
					break;
				case 10:
					temp[i].nextposition = 1;
					break;
				case -10:
					temp[i].nextposition = 3;
					break;
				}
			}
		}
		double rand=Math.random();
		if (temp[aminoIndex].nextposition == 10) {
			if (rand < 0.25) {
				for (int i = aminoIndex; i < len; i++) {
					switch (temp[i].nextposition) {
					case 0:
						temp[i].nextposition = 10;
						break;
					case 2:
						temp[i].nextposition = -10;
						break;
					case 10:
						temp[i].nextposition = 2;
						break;
					case -10:
						temp[i].nextposition = 0;
						break;
					}
				}
			} else if (rand < 0.5) {
				for (int i = aminoIndex; i < len; i++) {
					switch (temp[i].nextposition) {
					case 1:
						temp[i].nextposition = 10;
						break;
					case 3:
						temp[i].nextposition = -10;
						break;
					case 10:
						temp[i].nextposition = 3;
						break;
					case -10:
						temp[i].nextposition = 1;
						break;
					}
				}
			} else if (rand < 0.75) {
				for (int i = aminoIndex; i < len; i++) {
					switch (temp[i].nextposition) {
					case 0:
						temp[i].nextposition = -10;
						break;
					case 2:
						temp[i].nextposition = 10;
						break;
					case 10:
						temp[i].nextposition = 0;
						break;
					case -10:
						temp[i].nextposition = 2;
						break;
					}
				}
			} else {
				for (int i = aminoIndex; i < len; i++) {
					switch (temp[i].nextposition) {
					case 1:
						temp[i].nextposition = -10;
						break;
					case 3:
						temp[i].nextposition = 10;
						break;
					case 10:
						temp[i].nextposition = 1;
						break;
					case -10:
						temp[i].nextposition = 3;
						break;
					}
				}
			}
		}
		

		if (checkOverlap(temp) == false) {
			foldingArray = copy(temp);
			return true;
		}
		return false;

	}

	boolean checkOverlap(amino[] aminoArray) {
		char[][][] canvas = new char[len * 2 + 10][len * 2 + 10][len * 2 + 10];
		int x = len + 1;
		int y = len + 1;
		int z = len + 1;

		for (int i = 0; i < len; i++) {
			canvas[x][y][z] = aminoArray[i].type;
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
			case 10:
				z += 1;
				break;
			case -10:
				z -= 1;
				break;

			}
			if (canvas[x][y][z] != 0) {
				return true;
			}
		}
		getStability(canvas, aminoArray);
		return false;
	}

	void getStability(char[][][] canvas, amino[] aminoArray) {
		score = 0;
		int x = len + 1;
		int y = len + 1;
		int z = len + 1;

		for (int i = 0; i < len; i++) {
			if (canvas[x][y][z] == 'H') {
				if (canvas[x - 1][y][z] == 'H')
					score++;
				if (canvas[x + 1][y][z] == 'H')
					score++;
				if (canvas[x][y + 1][z] == 'H')
					score++;
				if (canvas[x][y - 1][z] == 'H')
					score++;
				if (canvas[x][y][z - 1] == 'H')
					score++;
				if (canvas[x][y][z + 1] == 'H')
					score++;
			}
			if (canvas[x][y][z] == 'C') {
				if (canvas[x - 1][y][z] == 'C')
					score += 5;
				if (canvas[x + 1][y][z] == 'C')
					score += 5;
				if (canvas[x][y + 1][z] == 'C')
					score += 5;
				if (canvas[x][y - 1][z] == 'C')
					score += 5;
				if (canvas[x][y][z - 1] == 'C')
					score += 5;
				if (canvas[x][y][z + 1] == 'C')
					score += 5;
				if (canvas[x - 1][y][z] == 'H')
					score += 2;
				if (canvas[x + 1][y][z] == 'H')
					score += 2;
				if (canvas[x][y + 1][z] == 'H')
					score += 2;
				if (canvas[x][y - 1][z] == 'H')
					score += 2;
				if (canvas[x][y][z - 1] == 'H')
					score += 2;
				if (canvas[x][y][z + 1] == 'H')
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
			case 10:
				z += 1;
				break;
			case -10:
				z -= 1;
				break;

			}
		}
		stability = (int) (score / 2.0) - consecutiveH - (consecutiveC * 5) - consecutiveHC;
	}

	void mutate() {
		int mutationLocation = (int) Math.floor(Math.random() * len);
		double rand = Math.random();
		if (rand < 0.25) {
			if (!foldLeft(mutationLocation))
				mutate();
		} else if (rand < 0.5) {
			if (!foldRight(mutationLocation))
				mutate();
		} else if (rand < 0.75) {
			if (!foldUp(mutationLocation))
				;
		} else {
			if (!foldUp(mutationLocation))
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

	void mutate_type2() {
		amino[] temp = copy(foldingArray);
		int a = (int) Math.floor(Math.random() * foldingArray.length);
		int b = (int) Math.floor(Math.random() * foldingArray.length);
		int min;
		int max;
		min = (a >= b) ? b : a;
		max = (a >= b) ? a : b;

		for (int i = a; i <= b; i++) {
			temp[i].nextposition = Math.floorMod(temp[i].nextposition + 2, 4);
		}
		if (checkOverlap(temp) == false) {
			foldingArray = copy(temp);
		} else {
			mutate_type2();
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
	
	int[][] coordinates() {
		int [][] coordinates= new int[len][3];
		
		int x = len + 1;
		int y = len + 1;
		int z = len + 1;
		for (int i = 0; i < len; i++) {
			coordinates[i][0]=x;
			coordinates[i][1]=y;
			coordinates[i][2]=z;
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
			case 10:
				z += 1;
				break;
			case -10:
				z -= 1;
				break;

			}

		}
		return coordinates;
	}
}
