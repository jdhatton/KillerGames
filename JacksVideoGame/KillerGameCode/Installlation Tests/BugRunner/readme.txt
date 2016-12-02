
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
BugRunner is a simple 2D game using the Java 3D timer,
from Chapter 11 of the book.

BugRunner requires j3dUtils.jar and J3DUtils.dll from Java 3D
to be placed in this directory.

**NOTE**
j3dUtils.jar and J3DUtils.dll are *NOT* here. You should extract 
them from the Java 3D distribution. 
I used Java 3D v1.3.1 Win32/OpenGL, available from
http://java.sun.com/products/java-media/3D/


Before the installer can be created, the application must be
packaged up as a JAR file. BugRunner requires two JARs:

    BugRunner.jar     // the application and other resources
    j3dUtils.jar      // for the Java 3D timer


============================
Compilation:

> javac -classpath "%CLASSPATH%;j3dutils.jar" *.java
// or use compileBR.bat

If you get "Warning" messages, please see the note below.

============================
Execution:

> java -cp "%CLASSPATH%;j3dutils.jar" BugRunner
// or use BugRunner.bat

============================
JAR creation:

> jar cvmf mainClass.txt BugRunner.jar *.class *.dll Images Sounds
// or use makeJar.bat

============================
JAR execution:

Best testing: move BugRunner.jar and j3dUtils.jar to another 
machine, one without Java 3D installed.

> java -jar BugRunner.jar
// or use runJar.bat
// or double click on BugRunner.jar

-----
-----------
Note on "unchecked or unsafe operation" Warnings

As explained in chapter 3, I have not used J2SE 5.0's type-safe 
collections, so that this code will compile in early versions of
J2SE (e.g. version 1.4).

The warning messages are always related to my use of collections
(e.g. ArrayList) without specifying a type for the objects they will
contain at run time.

No. of Warnings generated in J2SE 5.0 for the examples:
/BugRunner3D: 16 warnings

------------
Last updated: 21st April 2005

