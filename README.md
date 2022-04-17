#Showcase

## Contents
* Actor performance (implemented)
* Websocket / bidirectional socket messaging
* Non-sourced event stream 


## Performance

``sh
mvn package
java -jar xoom-benchmarks/target/benchmarks.jar 
``

# Issues

## Client-server benchmark
An invoker can send about 14M-15M messages/second to an actor.  However, when sending the messages to a client of a another
actor the throughput drops to 4500 messages/second (~ x3000 times fewer / second).

``sh
mvn package
java -jar xoom-benchmarks/target/benchmarks.jar 
``
