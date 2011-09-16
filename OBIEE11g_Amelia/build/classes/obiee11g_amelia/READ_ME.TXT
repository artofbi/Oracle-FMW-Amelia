===================================================
READ ME
Version : 0.99
Last Modified : September 16, 2011
===================================================

This JAR file can be used in Windows and *Nix environments.

It is intended to assist with the migration of Oracle BI 11g Fusion Middleware 
security from one environment to another with some level of ease 
while leveraging WLST.


Step 1.
---------------------------------------------------
Download the project and extract to a stage location.  Extract the JAR file from the /dist folder

Step 2.
---------------------------------------------------
On the source FMW environment locate the system-jazn-data.xml file which is normally found here, 
/<FMW_HOME>/user_projects/domains/bifoundation_domain/config/fmwconfig/

Copy the system-jazn-data.xml file to your staging directory or keep it in place as a reference if 
executing Project Amelia from the same machine.

Make sure that the java home path /jdk.../bin/ is set in your PATH environment variable.  If not just 
make sure you reference the full path to java to execute the JAR.

Execute the Project Amelia from the standard command line / terminal window (not WLST - not yet!) using : 
java -jar OBIEE11g_Amelia.jar "C:\Stage_Files\system-jazn-data.xml" "C:\Stage_Files\"
or
java -jar OBIEE11g_Amelia.jar "C:\Stage_Files\system-jazn-data.xml" "C:/Stage_Files/"  

Note:   The file name in argument 1 need not matter (i.e. it is a back up like system-jazn-data-old.xml) but it must specified.

Note:   Argument 2 is the output directory for the generated helper script.  It must contain a trailing slash
        such as / or \.  This is to enable to simplicity of the script to be agnostic of OS. Some windows machines need 
        forward slashes to work properly.

Note:   All arguments must be entered for argument 1 & 2.  Arguments 3 - 6 specify username, password, wls server, and wls port
        If arguments 3-6 are not entered then default values appear in the generated WLST script.

Example:
java -jar OBIEE11g_Amelia.jar "C:\Stage_Files\system-jazn-data.xml" "C:/Stage_Files/" weblogic Admin123 myserver 7001


********
Note:   Some OS's require that the output file path in argument 2 be written like "C:/Stage_Files/" using forward slashes.
********


Remember:   The credentials entered in the command-line arguments are those used for the environment to which you are migrating, the target environment. 
            That is to say that the system-jazn-data.xml file should be of your source environment, you are merely entering the credential 
            so that the script will automatically write the correct connect syntax for your target environment.


Step 3.
---------------------------------------------------
After the execution of Project Amelia in Step 2 a Python(.py) script is generated.
The generated .py script can now be leveraged against WLST. Open and copy the text, or FTP it 
to the server on which the migration is to ensue. You can also connect to the target server remotely using WLST.

Note:   By default the out-of-the-box Application roles are commented out so that they are not attempted to be re-added in 
        the environment on which the migration will be attempted.

Note:   From an Oracle BI 10g to 11g upgrade RPD groups are attempted to be scripted twice.  These principal to application 
        role assignments are commented out in the script to avoid duplication to a target environment.

Run the WLST command to start the WLST command console.  For Oracle BI 11g FMW this location is,
/bishiphome/Middleware/oracle_common/common/bin/wlst.sh (*Nix)
C:\<FMW_HOME>\oracle_common\common\bin\wlst.cmd (Windows)

Run the generated .py script from the WLST command line by entering,
execfile(<full file path location of the file>)



Step 4.
---------------------------------------------------
Basically the migration will go smoothly when running against the target WLS server using WLST.

Don't forget that you will need to verify that all application roles have been created and principal assignments have been made.

Please also provide feedback to the project to continue its betterment.

Go get lunch.  You've just cut a few hours of work out of your day.

Don't forget to visit http://www.artofbi.com



Observations.
---------------------------------------------------
Potential Issues:
1.  When the grantAppRole statements execute, if the principal (i.e.: user,group, or app role) is not found in the system then 
    an error will occur stopping the script.
