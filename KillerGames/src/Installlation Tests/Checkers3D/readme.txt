
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
Checkers3D is a simple Java 3D example from chapter 15 of the book.

It requires all the JARs and DLLs from Java 3D to be placed in this 
directory:
  * j3dcore.jar, j3daudio.jar, vecmath.jar, j3dutils.jar
  * J3D.dll, j3daudio.dll, J3DUtils.dll 

**NOTE**
These JARs and DLLs are *NOT* here. You should extract 
them from the Java 3D distribution. 
I used Java 3D v1.3.1 Win32/OpenGL, available from
http://java.sun.com/products/java-media/3D/


Before the installer can be created, the application must be
packaged up as a JAR file. Checkers3D requires five JARs:

    Checkers3D.jar     // the application
    j3dcore.jar, j3daudio.jar, vecmath.jar, j3dutils.jar      // for Java 3D

============================
Compilation:

> javac -classpath "%CLASSPATH%;vecmath.jar;j3daudio.jar;j3dcore.jar;j3dutils.jar" *.java
// or use compileChk.bat

If you get "Warning" messages, please see the note below.

============================
Execution:

> java -cp "%CLASSPATH%;vecmath.jar;j3daudio.jar;j3dcore.jar;j3dutils.jar" Checkers3D
// or use Checkers3D.bat

============================
JAR creation:

> jar cvmf mainClass.txt Checkers3D.jar *.class *.dll
// or use makeJar.bat

============================
JAR execution:

Best testing: move Checkers3D.jar, j3dcore.jar, j3daudio.jar, 
vecmath.jar, and j3dutils.jar to another machine, one without 
Java 3D installed.

> java -jar Checkers3D.jar
// or use runJar.bat
// or double click on Checkers3D.jar

-----------
Note on "unchecked or unsafe operation" Warnings

As explained in chapter 3, I have not used J2SE 5.0's type-safe 
collections, so that this code will compile in early versions of
J2SE (e.g. version 1.4).

The warning messages are always related to my use of collections
(e.g. ArrayList) without specifying a type for the objects they will
contain at run time.

No. of Warnings generated in J2SE 5.0 for the examples:
/Checkers3D: 9 warnings

---------------------
Last updated: 21st April 2005

