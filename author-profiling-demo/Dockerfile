FROM tomcat:alpine

MAINTAINER Joan Codina <joan.codina@upf.edu>
#This docker is thought to be combined with another docker that has the profiling-service
#The other docker should have the name profiling-books and expose its services in port 5000
#Both dockers must share the same doker net


COPY target/author-profiling-demo-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/author-profiling-demo.war
COPY CoNLL2009-ST-English-ALL.anna-3.3.lemmatizer.model /home/CoNLL2009-ST-English-ALL.anna-3.3.lemmatizer.model



EXPOSE 8080
