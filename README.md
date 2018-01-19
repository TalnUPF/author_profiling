# author_profiling
To install the software:
  - clone the project
  - download the profiling service (in a side folder named "profiling")
  take it from ... the drive. 
  - build the project mvn clean install -DskipTests
  - Move to folder author_profiling_demo
      - do a new mvn install 
      - run
      docker-compose up
      
The docker compose creates two containers:
 - one for the profiling service: profiling-books
 - the other for the interface:  authorprofilingdemo_profiling-books-demo_1 on port 8082
    
you can access it from server:8082/author-profiling-demo/profiling/
