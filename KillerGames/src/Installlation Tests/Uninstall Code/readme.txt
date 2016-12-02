
Appendix A. Installation using install4j

From:
  Killer Game Programming in Java
  Andrew Davison
  O'Reilly, May 2005
  ISBN: 0-596-00730-2
  http://www.oreilly.com/catalog/killergame/
  Web Site for the book: http://fivedots.coe.psu.ac.th/~ad/jg

Contact Address:
  Dr. Andrew Davison
  Dept. of Computer Engineering
  Prince of Songkla University
  Hat Yai, Songkhla 90112, Thailand
  E-mail: ad@fivedots.coe.psu.ac.th

If you use this code, please mention my name, and include a link
to the book's Web site.

Thanks,
  Andrew


============================
The DLLUninstallAction class in custom.jar is used by the BugRunner
and Checkers3D install4j uninstaller to remove their DLLs.

============================
Compilation:

> javac -classpath "%CLASSPATH%;d:\install4j\resource\i4jruntime.jar" DLLUninstallAction.java
// or use compile.bat

The compilation assumes that install4j had been installed 
at d:\install4j

============================
JAR creation:

> jar cvf custom.jar *.class
// or use makeJar.bat

-----
Last updated: 21st April 2005

