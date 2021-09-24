### Prerequisite for Running: RMI registry, compiled java program
Command for terminal:

rmiregistry &
javac *.java

###  Notes:

-Use different terminal/command windows for each Master/Worker node

### Running Master:

java Master
              # Run Workers now
              # Wait for Workers to be ready
m2.txt        # first matrix CSV file
m3.txt        # second matrix CSV file
	      # output written in testoutput.txt

### Running Worker

java Worker   # or java worker2
nodename      # name of this node
ip_master     # ip address of Master	     
