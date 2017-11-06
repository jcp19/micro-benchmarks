## Minha Benchmarks

This repository contains micro-benchmarks with distributed data races: 
- package `microbenchmarks` includes a number of micro-benchmarks simulating real-world concurrency bugs of popular distributed applications. The name of each package indicates the application name (`m` - MapReduce, `h` - Hadoop) and the issue number in the corresponding bug repository.

- package `dataflaskslite` contains an implementation of a peer sampling service named [Cylon](http://gossple2.irisa.fr/~akermarr/cyclon.jnsm.pdf), used by the gossip-based key-value store [DataFlasks](https://dl.acm.org/citation.cfm?id=2707804). This benchmark can be run under different configurations by setting the values in class `dataflaskslite.common.Parameters`. In particular, one can tune the number of nodes in the system (`NUMPEERS`), the number of cycles during which the nodes exchange messages (`NUMCYCLES`), the shuffling period (`INTERVAL`), the size of each node's view (`VIEWSIZE`), and the number of peers exchange in a shuffling (`GOSSIPSIZE`).

Each benchmark includes two main classes: a standalone version and a *Minha* version.

### Usage
**1. Compile:**

```
$ mvn package 
```

**2. Run jar:**

```
$ java -jar target/minha-benchmarks-1.0-SNAPSHOT-jar-with-dependencies.jar
```
At the moment, the jar will execute main class `dataflaskslite.test.DFMainMinha`. Change `pom.xml`to compile the jar file for other main classes.  

**3. Runtime Trace:** Assuming that the Minha framework is correctly configured with the shared variables and message handlers of the target program, after the execution of the jar, a trace file `minhaTRACER.log` will be placed under folder `logs`. The trace file contains runtime events (in JSON format) regarding: socket sends/receives, read/write accesses to variables of interest, thread synchronization points. 
