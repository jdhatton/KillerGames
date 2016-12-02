
// DLLUninstallAction.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Based on install4j's HelloUninstallAction example.

   Uninstall all the DLL files prior to install4j's general 
   uninstallation process.

   This class is executed in the <PROG_DIR>/.install4j directory,
   but must delete DLLs in <PROG_DIR>/Executables, which is the
   reason for the PATH extension.
*/

import java.io.*;
import com.install4j.api.*;


public class DLLUninstallAction extends UninstallAction 
{
  private static final String PATH = "../Executables";
       // location of the DLLs relative to <PROG_DIR>/.install4j


  public boolean performAction(Context context, ProgressInterface progReport) 
  // called by install4j to do uninstallation tasks
  {
    File delDir = new File(PATH);

    FilenameFilter dllFilter = new FilenameFilter() {
      public boolean accept(File dir, String name) 
      { return name.endsWith("dll");  }
    };

    String[] fNms = delDir.list(dllFilter); // list of dll filename
    if (fNms.length == 0)
      System.out.println("Uninstallation: No DLLs found");
    else
      deleteDLLs(fNms, progReport);
    return true;
  } // end of performAction()


  private void deleteDLLs(String[] fNms, ProgressInterface progReport)
  // delete each DLL file, and report the progress
  {
    progReport.setStatusMessage("Deleting installed DLLs");
 
    int numFiles = fNms.length;
    String msg;
    for (int i=0; i < numFiles; i++) {
      msg = new String("" + (i+1) + "/" + numFiles + ": " + fNms[i] + "... ");
      deleteFile(fNms[i], progReport, msg);
      progReport.setPercentCompleted( ((i+1)*100)/numFiles );
      try {
        Thread.sleep(500);   // 0.5 sec to see something
      } 
      catch (InterruptedException e) {}
    }
  }  // end of deleteDLLs()


  private static void deleteFile(String dllFnm, ProgressInterface progReport, String msg)
  // delete the named file from the Executables/ directory
  {
    File f = new File(PATH + "/" + dllFnm);
    if (!f.exists())
      progReport.setDetailMessage(msg + "not present");
    else {
      if (f.delete())
        progReport.setDetailMessage(msg + "done");
      else
        progReport.setDetailMessage(msg + "FAILED");
    }
  }  // end of deleteFile()


  public int getPercentOfTotalInstallation() 
  // percent of status bar to represent this action
  { return 50; }    // since no post-uninstallation task

} // end of DLLUninstallAction class

