# Evolutionary Algorithm:

**POPULATION** : Number of proteins in a generation<br />
**GENERATIONS** : Number of generations<br />
**CARRYOVER** : Number of parents selected to generate children<br />
**TOP** : Number of top proteins to be cloned to the next generation<br />
**PROB_TYPE1** : Single Rotation probability<br />
**PROB_TYPE2** : Double Rotation probability<br />
**PROB_TYPE3** : Crank mutation probability<br />
(Note that the probability the retracing mutation is extrapolated from values above)<br />

**PROTECTION** : If true child protection is activated<br />
**PROTECTION_GEN** : Number of generations for which to protect top children<br />
**GRAPHICS** : If true algorithm will run with graphics<br />
**RUNS** : Number of total algorithm runs<br />
**CHAIN** : String of Amino acid "H","C", "P" for which the optimal stability should be found<br />


# Beam Search:

**POPULATION** : The number of top protein folds in a layer that wil be chosen to fold on<br />
**gen** : The current generation updates with each iteration<br />
**counter** : The counter of each layer<br />
**CHAIN** : String of Amino acid "H", "P" for which the optimal stability should be found<br />
