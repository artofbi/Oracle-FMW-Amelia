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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;


/**
 *
 * @author Christian Screen - ArtOfBI.com
 */
public class XMLSecurityParserManager {
    
    private String fullPathJAZNFile = null;
    
    private String apiUsername;
    private String apiPassword;
    private String apiIPDNS;
    private String apiPort;
    private String apiURL;

    private String IMPACT_DB_CONN_URL;
    private String IMPACT_DB_DRIVER_NAME;
    private String IMPACT_DB_USERNAME;
    private String IMPACT_DB_PASSWORD;



    public XMLSecurityParserManager()
    {

    }


    public XMLSecurityParserManager(String fileLocation)
    {
        fullPathJAZNFile = fileLocation;
        //this.loadXMLConfigValues(fileLocation);
    }
    
    
    public String listAppRoles() {
        
        StringBuilder sb = new StringBuilder();
        
        try {

            File fXmlFile = new File(fullPathJAZNFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("app-role");
            System.out.println("---------------------------------------------------");
            System.out.println("Existing Application Roles");
            System.out.println("---------------------------------------------------");

            
            String currentAppRole = null;
            
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    System.out.println(getTagValue("name", eElement));
                    //System.out.println("Class : "  + getTagValue("class",eElement));
                    currentAppRole = getTagValue("name", eElement);
                    
                    
                    //---------------------------------------
                    // Write out the items to the string
                    //---------------------------------------
                    if(currentAppRole.equals("BISystem") || currentAppRole.equals("BIAdministrator") 
                            || currentAppRole.equals("BIAuthor") || currentAppRole.equals("BIConsumer"))
                        sb.append("#");
                    
                    sb.append("createAppRole(\"obi\", \"").append(currentAppRole).append("\")");
                    sb.append("\n");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return sb.toString();
        }
    }


    private static String getTagValue(String sTag, Element eElement){
    NodeList nlList= eElement.getElementsByTagName(sTag).item(0).getChildNodes();
    Node nValue = (Node) nlList.item(0);

    return nValue.getNodeValue();
    }



}
