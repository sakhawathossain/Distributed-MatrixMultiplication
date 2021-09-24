# Distributed Matrix Multiplication
**Written for UTSA CS5523 (Spring 2020)**


### Prerequisite: RMI registry

Run RMI registry from terminal:

```
rmiregistry &
```

### Executing Worker program from terminal:

```
java Worker
nodename      # name of this node
ip_master     # ip address of Master
```

### Running Master:

```
java Master
              # Run Workers now
              # Wait for Workers to be ready
m1.txt        # first matrix CSV file
m2.txt        # second matrix CSV file
```

###  Notes:

Use different terminal/command windows for each Master/Worker node
