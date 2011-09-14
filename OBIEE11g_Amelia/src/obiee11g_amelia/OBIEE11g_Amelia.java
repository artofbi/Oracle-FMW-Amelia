/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package obiee11g_amelia;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;
import java.util.Date;

/**
 *
 * @author SalesMarketing
 */
public class OBIEE11g_Amelia {

    private static final String OUTPUT_HELPER_SCRIPT_NAME = "OBI11gSecurityMigration.py";
    public static StringBuilder sbMainScript = new StringBuilder();
    private static String fullFilePath, outputDirPath;
    private static String wlsUsername, wlsPassword, wlsServer, wlsPort;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Show Title of the this Application
        System.out.println("");
        System.out.println("===================================================");
        System.out.println("Oracle BI 11g Security Migration Helper");
        System.out.println("Project Codename Amelia");
        System.out.println("Build Version: 0.99.1");
        System.out.println("===================================================");
        System.out.println("");
        
        
        fullFilePath = args[0].toString();
        outputDirPath = args[1].toString();
        
        wlsUsername = "weblogic";
        wlsPassword = "Admin123";
        wlsServer = "localhost";
        wlsPort = "7001";
        
        
        
        if(args[0].isEmpty() || args[1].isEmpty()) {
            System.out.println("This program requires a minimum of two arguments:");
            System.out.println("--> Argument 1 : Full Path of system-jazn-data.xml file");
            System.out.println("--> Argument 2 : Output Directory for helper script (must contain trailing slash)");
            System.out.println("Example: java -jar OBIEE11g_Amelia.jar \"C:\\mydir\\system-jazn-data.xml\" \"C:\\myoutdir\\\"");
        }
        else if (!(outputDirPath.endsWith("/") || outputDirPath.endsWith("\\")))
        {
            System.out.println("Second Argument (output directory) requires a trailing slash:");
            System.out.println("--> Example : \"C:\\myoutdir\\\"");
        }
        else
        {
            
            if(args.length > 2)
                wlsUsername = args[2].toString();
            
            if(args.length > 3)
                wlsPassword = args[3].toString();
            
            if(args.length > 4)
                wlsServer = args[4].toString();
        
            if(args.length > 5)
                wlsPort = args[5].toString();
        
            goGoGadget();
        }

    }
    
    public static void goGoGadget(){
        // get arguments required
        
        
        //...testing..
        //fullFilePath = "C:\\_DevNetBeans\\system-jazn-data.xml";
        
        
        // Variables
        Date scriptStartTS = new Date();
        String connectionStatement = null;
        

        
        
        //-------------------------------------------------
        // Script Connect
        //-------------------------------------------------
        
        // begin a exception wrapper
        sbMainScript.append("#try:");
        sbMainScript.append("\n");
        
        
        // Connection statement to WLS
        connectionStatement = "connect('" + wlsUsername + "', '" + wlsPassword
                + "', '" + wlsServer + ":" + wlsPort + "')";
        sbMainScript.append(connectionStatement);
        sbMainScript.append("\n");
        sbMainScript.append("\n");
        
        // call parse manager main function to rip and hold app roles
        XMLSecurityParserManager oXSPM = new XMLSecurityParserManager(fullFilePath);
        sbMainScript.append(oXSPM.listAppRoles());
        
        
        
        // Create object which will loop through the app-roles and members
        XQuerySecurityManager oXQSM = new XQuerySecurityManager(fullFilePath);
        try {
            
            // call core script to get grant role statements
            sbMainScript.append(oXQSM.buildWLSTSecurityAppRoleMemberScript());
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(OBIEE11g_Amelia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(OBIEE11g_Amelia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OBIEE11g_Amelia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(OBIEE11g_Amelia.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
                    
        //----------------------------------
        // Build the PY Script for WLST
        //----------------------------------

        // finish the exception wrap
        sbMainScript.append("\n");
        sbMainScript.append("#finally:");
        sbMainScript.append("\n");
        sbMainScript.append("#disconnect()");
        
        
        //HelperFunctions.writeStringToTextFile("C:\\_DevNetBeans\\OBI11gSecurityMigration.py", sbMainScript.toString());
        HelperFunctions.writeStringToTextFile(outputDirPath + OUTPUT_HELPER_SCRIPT_NAME, sbMainScript.toString());

        
        
        //----------------------------------
        
        
        
        //----------------------------------
        // Disply Status Report of Script to stdOut
        //----------------------------------
        Date scriptFinishTS = new Date();
        System.out.println();
        System.out.println();
        System.out.println("---------------------------------------------------");
        System.out.println("Status of Security Migration Script Builder");
        System.out.println("---------------------------------------------------");
        
        // math for difference in time for script start to finsih
        long scriptTimeLapse = (scriptFinishTS.getTime() - scriptStartTS.getTime());
        
        System.out.println("Script Execution Time : " +  String.valueOf(scriptTimeLapse) + " milliseconds" );
        System.out.println("File output to : " + (outputDirPath + OUTPUT_HELPER_SCRIPT_NAME));
        System.out.println("JAZN File Used : " + fullFilePath);
        System.out.println("Connection String : " + connectionStatement);
        
        
        //----------------------------------
        // Shameless self-promotion plug
        //----------------------------------
        System.out.println();
        System.out.println();
        System.out.println("-------------------------------------------------------------------");
        System.out.println("Thank you for your support.  Please visit http://www.artofbi.com");
        System.out.println("-------------------------------------------------------------------");
    }
}
