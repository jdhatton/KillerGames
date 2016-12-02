@echo off
echo Compiling DLLUninstallAction.java ...

javac -classpath "%CLASSPATH%;d:\install4j\resource\i4jruntime.jar" DLLUninstallAction.java
echo Finished.