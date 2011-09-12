===================================================
READ ME
===================================================

This JAR file can be used in Windows and *Nix environments.

It is intended to assist with the migration of Oracle BI 11g 
security from one environment to another with some level of ease 
while leveraging WLST.

Locate the system-jazn-data.xml file which is normally located, 
/<FMW_HOME>/user_projects/domains/bifoundation_domain/config/fmwconfig/


Execute the script from the standard command line (not WLST - not yet!)
java -jar OBIEE11g_Amelia.jar "C:\_DevNetBeans\_Stage_Files\system-jazn-data.xml" "C:\_DevNetBeans\_Stage_Files\" 

Note:   The file name in argument 1 need not matter.
Note:   Argument 2 is the output directory for the generated helper script.  It must contain a trailing slash
        such as / or \.  This is to enable to simplicity of the script to be agnostic of OS.
Note:   All arguments must be entered for argument 1 & 2.  Arguments 3 - 6 specify username, password, wls server, and wls port

Example:
java -jar OBIEE11g_Amelia.jar "C:\_DevNetBeans\_Stage_Files\system-jazn-data.xml" "C:\_DevNetBeans\_Stage_Files\" weblogic Admin123 myserver 7001



Remember:   The credentials entered in the command-line arguments are those used for the environment to which you are migrating, the target environment. 
            That is to say that the system-jazn-data.xml file should be of your source environment, you are merely entering the credential 
            so that the script will automatically write the correct connect syntax for your target environment.


Take the generated .py script and leverage it in the existing environment, open and copy the text, or FTP it 
to the server on which the migration is to ensue.

Note:   By default the out-of-the-box Application roles are commented out so that they are not attempted to be re-added in 
        the environment on which the migration will be attempted.

Run the WLST command to start the WLST command console.  For Oracle BI 11g FMW this location is,
/bishiphome/Middleware/oracle_common/common/bin/wlst.sh (*Nix)
C:\<FMW_HOME>\oracle_common\common\bin\wlst.cmd (Windows)

Run the script from the WLST command line by entering,
execfile(<full file path location of the file>)

or

paste the text of the migration helper script into the WLST terminal window.  The script will execute.


Potential Issues:
1.  When the grantAppRole statements execute, if the principal (i.e.: user,group, or app role) is not found in the system then 
    an error will occur stopping the script.
