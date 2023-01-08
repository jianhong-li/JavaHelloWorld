#!/usr/bin/env bash

mvn --settings ~/.m2/settings.tujia.xml versions:set -DnewVersion=$@
mvn --settings ~/.m2/settings.tujia.xml -N versions:update-child-modules
mvn --settings ~/.m2/settings.tujia.xml versions:commit
