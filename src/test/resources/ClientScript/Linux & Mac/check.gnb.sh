#!/bin/bash
cd ./../../../../../../../..
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/TestSuites/Billing/Regression/1.Freetrial/1-1.StayInFreetrial/1-1-3.CheckGNB.xml
