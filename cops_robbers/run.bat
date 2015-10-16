javac -cp .;commons-math3-3.5.jar *.java
REM Some default parameters for the model
REM numberOfAgents, iterations, probabilityOfCop, probabilityOfArrest, itrationsToOutput
java Model 1000 1000 0.1 0.5 200,400,600,800,999