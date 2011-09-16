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

import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import java.io.*;
import java.io.StringReader;

import java.util.*;
/**
 *
 * @author SalesMarketing
 */
public class HelperFunctions {

    private static Random rn = new Random();

    public HelperFunctions(){
        
    }

    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }


    public static InputSource loadISFromString(String xml) throws Exception
    {
        InputSource is = new InputSource(new StringReader(xml));
        return is;
    }


    public static int rand(int lo, int hi)
    {
            int n = hi - lo + 1;
            int i = rn.nextInt() % n;
            if (i < 0)
                    i = -i;
            return lo + i;
    }

    public static String randomstring(int lo, int hi)
    {
            int n = rand(lo, hi);
            byte b[] = new byte[n];
            for (int i = 0; i < n; i++)
                    b[i] = (byte)rand('a', 'z');
            return new String(b, 0);
    }

    public static String randomstring()
    {
            return randomstring(5, 25);
    }

    public static String stripLeadingAndTrailingQuotes(String str)
    {
        if (str.startsWith("\""))
        {
          str = str.substring(1, str.length());
        }
        if (str.endsWith("\""))
        {
          str = str.substring(0, str.length() - 1);
        }
        return str;
    }
    
    public static void writeStringToTextFile(String fileFullPath, String textToWrite)
    {
        try {
            FileWriter outFile = new FileWriter(fileFullPath);
            PrintWriter out = new PrintWriter(outFile);
            
            out.println(textToWrite);
            
            out.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


