#mvn package
for line in $(cat files2test.txt | grep -v "^[[:space:]]*$" files2test.txt | grep -v "^#.*"); do 
  echo ">>> Benchmark: $line";
  java -cp .:target/minha-benchmarks-1.0-SNAPSHOT-jar-with-dependencies.jar $line > /dev/null
  echo ">>> Results:"
  cd ../minha-checker
  bash ../minha-checker/tester.bash 
  cd - > /dev/null
  echo
done 
