/*****************************************************************************
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * ArtOfBI.com licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

package obiee11g_amelia;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


//List<String> result = new ArrayList<String>();

/**
 *
 * @author SalesMarketing
 */
public class XQuerySecurityManager {
        
    //Variables
    private String fullPathJAZNFile = null;
    
    //Constructur
    public XQuerySecurityManager(String fileLocation) {
        fullPathJAZNFile = fileLocation;
    }
    
    public String buildWLSTSecurityAppRoleMemberScript(boolean isDeleteScript) throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException {

        String strRoleGrantCommand = "grantAppRole";
        
        if(isDeleteScript)
            strRoleGrantCommand = "revokeAppRole";

        StringBuilder sb = new StringBuilder();

        try {
            // Standard of reading a XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder;
            Document doc = null;
            XPathExpression expr, exprRoleMembers = null;
            builder = factory.newDocumentBuilder();
            doc = builder.parse(fullPathJAZNFile);

            // Create a XPathFactory
            XPathFactory xFactory = XPathFactory.newInstance();

            // Create a XPath object
            XPath xpath = xFactory.newXPath();

            // Compile the XPath expression
            //expr = xpath.compile("//person[firstname='Lars']/lastname/text()");
            expr = xpath.compile("//policy-store/applications/application/app-roles/app-role/name/text()");

            
            // StdOut header display
            System.out.println();
            System.out.println();
            System.out.println("---------------------------------------------------");
            System.out.println("Application Roles to Principal(User/Group/AppRole)");
            System.out.println("---------------------------------------------------");
            
            
            

            // Run the query and get a nodeset
            Object result = expr.evaluate(doc, XPathConstants.NODESET);

            // Label the app role in question
            String currentAppRole = null;

            // Cast the result to a DOM NodeList
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {

                System.out.println(nodes.item(i).getNodeValue());
                currentAppRole = nodes.item(i).getNodeValue().toString();

                exprRoleMembers = xpath.compile("//policy-store/applications/application/app-roles/app-role[name='" + currentAppRole + "']/members/member");
                Object resultMembers = exprRoleMembers.evaluate(doc, XPathConstants.NODESET);
                NodeList nodesMembers = (NodeList) resultMembers;

                for (int y = 0; y < nodesMembers.getLength(); y++) {
                    Element show = (Element) nodesMembers.item(y);

                    String appRoleMemberName = xpath.evaluate("name", show);
                    String appRoleMemberClass = xpath.evaluate("class", show);

                    // stdOut the principal name and principal class
                    System.out.println("-->" + appRoleMemberName + " (" + appRoleMemberClass + ")");

                    //---------------------------------------
                    // Comment out what should be the default OBI 11g default installed groups.
                    // There is no need to re-add these because it will cause a fail message to occur at 
                    // point of failure.
                    //---------------------------------------
                    if((currentAppRole.equals("BISystem") && appRoleMemberName.equals("BISystemUser")) 
                            || (currentAppRole.equals("BIAdministrator") && appRoleMemberName.equals("BIAdministrators"))
                            || (currentAppRole.equals("BIAuthor") && appRoleMemberName.equals("BIAuthors"))
                            || (currentAppRole.equals("BIAuthor") && appRoleMemberName.equals("BIAdministrator"))
                            || (currentAppRole.equals("BIConsumer") && appRoleMemberName.equals("BIConsumers"))
                            || (currentAppRole.equals("BIConsumer") && appRoleMemberName.equals("BIAuthor"))
                            || (currentAppRole.equals("BIConsumer") && appRoleMemberName.equals("authenticated-role")))
                        sb.append("#");
                    
                    
                    //---------------------------------------
                    // Comment out a group class principal addition if the app role name is the same as the group name.
                    // This happens during an OBI 10g to OBI 11g upgrade because the upgrade utility needs to extract 
                    // the RPD Groups and place them somewhere. That somewhere is both the App Roles and the LDAP Embedded 
                    // groups. Since security for the RPD gets reattached from RPD Groups to Application Roles the 
                    // RPD groups (now WLS LDAP groups) may or may not be needed.  In most cases they are no longer needed as 
                    // this is really duplication of information and chances are the "groups" are no longer needed. For this case 
                    // we will comment them out so that when migrated they don't get added to the target environment.  
                    
                    // In other cases some groups can actually be mapped to an LDAP system and we want those groups, 
                    // which used to RPD Groups to come from the Alt. LDAP provider and be linked to an App Role to maintain 
                    // a centeralized security model.  In this case just uncomment those groups from the PRoject Amelia 
                    // generated script and you'll be fine.  Either, way you at least have a scripted inventory of what 
                    // app roles and app role principal mappings are coming from one system (possibly a OBI 10g to 11g upgrade),
                    // source in order to be moved to a potential target system.
                    // 
                    //---------------------------------------
                    if(currentAppRole.equals(appRoleMemberName) 
                            && appRoleMemberClass.equals("weblogic.security.principal.WLSGroupImpl"))
                    {
                        sb.append("### Group Dup ###");
                    }
                    
                    
                    
                    //---------------------------------------
                    // Write out the commands to the string
                    //---------------------------------------
                    sb.append(strRoleGrantCommand)  // revokeAppRole or grantAppRole
                            .append("(\"obi\", \"")
                            .append(currentAppRole).append("\", \"")
                            .append(appRoleMemberClass).append("\", \"")
                            .append(appRoleMemberName).append("\"")
                            .append(")");
                    sb.append("\n");
                }



            } //end for
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return sb.toString();
        }


    }

    public void buildWLSTSecurityMigrationTest() throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException {
        
        // Standard of reading a XML file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        XPathExpression expr, exprRoleMembers = null;
        builder = factory.newDocumentBuilder();
        doc = builder.parse(fullPathJAZNFile);

        // Create a XPathFactory
        XPathFactory xFactory = XPathFactory.newInstance();

        // Create a XPath object
        XPath xpath = xFactory.newXPath();

        // Compile the XPath expression
        //expr = xpath.compile("//person[firstname='Lars']/lastname/text()");
        expr = xpath.compile("//policy-store/applications/application/app-roles/app-role/name/text()");
        
        
        // Run the query and get a nodeset
        Object result = expr.evaluate(doc, XPathConstants.NODESET);

        // Cast the result to a DOM NodeList
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
        
            System.out.println(nodes.item(i).getNodeValue());
            

            exprRoleMembers = xpath.compile("//policy-store/applications/application/app-roles/app-role[name='" + nodes.item(i).getNodeValue().toString() + "']/members/member");
            Object resultMembers = exprRoleMembers.evaluate(doc, XPathConstants.NODESET);
            NodeList nodesMembers = (NodeList) resultMembers;
            
            for (int y = 0; y < nodesMembers.getLength(); y++) {
              Element show = (Element) nodesMembers.item(y);
              
              String appRoleMemberName = xpath.evaluate("name", show);
              String appRoleMemberClass = xpath.evaluate("class", show);

              System.out.println("-->" + appRoleMemberName + " (" + appRoleMemberClass + ")");
            }

            
            
        }

//        // New XPath expression to get the number of people with name lars
//        expr = xpath.compile("count(//person[firstname='Lars'])");
//        // Run the query and get the number of nodes
//        Double number = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
//        System.out.println("Number of objects " + number);
//
//        // Do we have more then 2 people with name lars?
//        expr = xpath.compile("count(//person[firstname='Lars']) >2");
//        // Run the query and get the number of nodes
//        Boolean check = (Boolean) expr.evaluate(doc, XPathConstants.BOOLEAN);
//        System.out.println(check);

    }

}
