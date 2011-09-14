/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
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
    
    public String buildWLSTSecurityAppRoleMemberScript() throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException {


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
                    // Write out the items to the string
                    //---------------------------------------
                    if((currentAppRole.equals("BISystem") && appRoleMemberName.equals("BISystemUser")) 
                            || (currentAppRole.equals("BIAdministrator") && appRoleMemberName.equals("BIAdministrators"))
                            || (currentAppRole.equals("BIAuthor") && appRoleMemberName.equals("BIAuthors"))
                            || (currentAppRole.equals("BIConsumer") && appRoleMemberName.equals("BIConsumers"))
                            || (currentAppRole.equals("BIConsumer") && appRoleMemberName.equals("authenticated-role")))
                        sb.append("#");
                    
                    sb.append("grantAppRole('obi', '")
                            .append(currentAppRole).append("', '")
                            .append(appRoleMemberClass).append("', '")
                            .append(appRoleMemberName).append("'")
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
