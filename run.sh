#!/bin/bash

mkdir -p out

echo "Compileren.."
javac -cp lib/jaybird-full-3.0.5.jar:out -d out src/*.java && javac -cp lib/jaybird-full-3.0.5.jar -d out src/**/*.java 
if [ $? -eq 0 ]; then
    echo "Compileren gelukt Applicatie starten"
    java -cp out:lib/* Main
else
    echo "Compileren mislukt!"
fi