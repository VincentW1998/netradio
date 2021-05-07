#!/bin/bash

if [ "$1" == "client" ]; then
  cd codeC/
  echo 'make clean :'
  make clean
  echo '#######################################'
  echo 'make :'
  make
  echo '#######################################'
  echo "Enter id : "
  read id
  echo ''
  ./client $id

elif [ "$1" == "gest" ]; then
  cd codeJava/
  rm *.class
  javac Gestionnaire.java
  java Gestionnaire
elif [ "$1" == "diff" ]; then
  cd codeJava/
  rm *.class
  javac Diffuser.java
  echo "Enter register's port : "
  read port
  java Diffuser $id2 $port
fi
