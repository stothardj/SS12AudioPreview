#!/bin/bash

JAR_LOC='../website/New.jar'
SIGNER_PASSWORD='uclass12'
SIGNER_STORE='uclass12'

function fail_and_quit {
  echo '***ERROR: ' $1
  exit -1
}

echo "code goes in, jar comes out."
printf "never a miscommunication\n\n"
echo "***BUILDING SS12AudioPreview" 

cd src

if ! javac $(find ./ -iname "*.java")
then
  fail_and_quit "compilation"
fi

CLASS_FILES=$(find ./ -iname "*.class") 
#AUDIO_FILES=$(find ./ -iname "*.wav")

if ! jar cvf $JAR_LOC $CLASS_FILES #$AUDIO_FILES #models
then
  fail_and_quit "creating jar"
fi

if ! (echo $SIGNER_PASSWORD | jarsigner $JAR_LOC $SIGNER_STORE 2> /dev/null)
then
  fail_and_quit "signing jar"
fi

#lzma $JAR_LOC

echo "***SUCCESS"
echo "jar placed at " $JAR_LOC

if [ 'c' = "$1" ] 
then
  rm $CLASS_FILES
  echo "***CLEANED .class files"
fi


