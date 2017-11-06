## Minha Benchmarks

This repository contains a set of micro-benchmarks with distributed data races. In particular, package `microbenchmarks` contains


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
