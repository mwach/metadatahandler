How to download:

    git clone git@github.com:mwach/metadatahandler.git -b feature/cxf-onto-integration

How to build:

    **Be sure you performed steps from 'Installation' part first**
    cd MetadataHandler
    mvn clean install

How to run:

    cd MetadataHandler/server/target
    java -Dorg.apache.cxf.ax.allowInsecureParser=1 -jar MetadataHandler.jar


Installation:

Download and install Protege 3.4.8:

```
# Prepare installation dictionary
PROTEGE_DIR="/opt/protege/Protege_3.4.8"
mkdir -p $PROTEGE_DIR
chown <current_user>: $PROTEGE_DIR

# Download progete from web
wget http://protege.stanford.edu/download/protege/3.4/installanywhere/Web_Installers/InstData/Linux_64bit/NoVM/install_protege_3.4.8.bin

# Install it into created directory
chmod +x install_protege_3.4.8.bin
./install_protege_3.4.8.bin

# Copy required jars into maven repository
cd $PROTEGE_DIR
mvn install:install-file -Dfile=protege.jar -DgroupId=edu.stanford.smi.protege -DartifactId=protege -Dversion=3.4.8 -Dpackaging=jar
mvn install:install-file -Dfile=./plugins/edu.stanford.smi.protegex.owl/protege-owl.jar -DgroupId=edu.stanford.smi.protege -DartifactId=protege-owl -Dversion=3.4.8 -Dpackaging=jar
mvn install:install-file -Dfile=./plugins/edu.stanford.smi.protegex.owl/swrl-jess-bridge.jar -DgroupId=edu.stanford.smi.protege -DartifactId=swrl-jess-bridge -Dversion=3.4.8 -Dpackaging=jar
mvn install:install-file -Dfile=jess.jar -DgroupId=jessrules.com -DartifactId=jess -Dversion=7.1 -Dpackaging=jar
mvn install:install-file -Dfile=./plugins/edu.stanford.smi.protegex.owl/orphanNodesAlg.jar -DgroupId=edu.stanford.smi.protege -DartifactId=orphanNodesAlg -Dversion=3.4.8 -Dpackaging=jar
```

Create a local ontology repository

```
# Create a local repository:
MH_REPOSITORY=/var/local/metadatahandler
mkdir -p $MH_REPOSITORY 
chown -R <current_user:> $MH_REPOSITORY 

# Copy all the ontologies to that folder
cd MetadataHandler/server/src/test/resources/*owl $MH_REPOSITORY/

# Update properties file to match new settings
vi server/src/main/resources/MetadataHandlerConfig.properties
# verify paths in the file are correct
```
