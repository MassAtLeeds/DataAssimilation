javac -cp .;commons-math3-3.5.jar *.java
REM Some default parameters for the model
REM currentPop, startTime, endTime, parameterString
REM parameterString is made of lose rate to alternative routes, rate in per time step, rate out per time step.
java Model 1000 1 10 "0.01,200,200"