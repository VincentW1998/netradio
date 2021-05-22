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
  echo "Number of max diffuser : "
  read maxDiff
  java Gestionnaire $maxDiff
elif [ "$1" == "diff" ]; then
  cd codeJava/
  rm *.class
  javac Diffuser.java
  echo "Enter register's port : "
  read port
  java Diffuser $id2 $port
elif [ "$1" == "difftest" ]; then
  cd codeJava/
  rm *.class
  javac Diffuser.java
  java Diffuser $id2 "9998" "toto" "225.0.0.0" "localhost"
fi
