@echo off

echo Making BugRunner Jar...
jar cvmf mainClass.txt BugRunner.jar *.class *.dll Images Sounds
echo.

echo Indexing jar...
jar i BugRunner.jar
echo done
