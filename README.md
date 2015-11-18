OntologyModule
=============
Installation:

Download and install Protege 3.4.8 from: http://protege.stanford.edu/download/protege/3.4/installanywhere/Web_Installers/

PROTEGE_DIR="/opt/protege/Protege_3.4.8"

cd $PROTEGE_DIR

mvn install:install-file -Dfile=protege.jar -DgroupId=edu.stanford.smi.protege -DartifactId=protege -Dversion=3.4.8 -Dpackaging=jar
mvn install:install-file -Dfile=./plugins/edu.stanford.smi.protegex.owl/protege-owl.jar -DgroupId=edu.stanford.smi.protege -DartifactId=protege-owl -Dversion=3.4.8 -Dpackaging=jar
mvn install:install-file -Dfile=./plugins/edu.stanford.smi.protegex.owl/swrl-jess-bridge.jar -DgroupId=edu.stanford.smi.protege -DartifactId=swrl-jess-bridge -Dversion=3.4.8 -Dpackaging=jar
mvn install:install-file -Dfile=jess.jar -DgroupId=jessrules.com -DartifactId=jess -Dversion=7.1 -Dpackaging=jar
mvn install:install-file -Dfile=./plugins/edu.stanford.smi.protegex.owl/orphanNodesAlg.jar -DgroupId=edu.stanford.smi.protege -DartifactId=orphanNodesAlg -Dversion=3.4.8 -Dpackaging=jar


Create a repository:
mkdir -p /var/local/metadatahandler
chown -R <user:group> /var/local/metadatahandler

Copy all the ontologies to that folder. Update properties file to match new settings