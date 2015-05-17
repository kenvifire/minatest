#!/usr/bin/env bash
mvn assembly:assembly
scp target/minademo-1.0-SNAPSHOT-jar-with-dependencies.jar kenvi@182.92.77.50:/home/kenvi/app/

