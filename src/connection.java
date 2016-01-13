import java.sql.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class connection
{

    public static void main(String[] args) 
    {
        // TODO code application logic here
    	String sUsername = "s_wjanpong";
		String sPassword= "FbeYRGNdq7t2ebdE";
		
		try
		{
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://cypress;" +
			        "user = " + sUsername + ";" +
			        "password = " + sPassword);
			String query = "SELECT StudentNo, LastName, FirstName, GPA FROM Students";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			try
			{
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				
				//root elements (GPASummanry)
				Document doc = docBuilder.newDocument();
				doc.setXmlStandalone(true);
				//Document doc = docBuilder.parse("U:/cmpt354/GPASummary.xml");
				Element rootElement = doc.createElement("GPASummary");;
				doc.appendChild(rootElement);
				
			
							while(rs.next())
							{
								int studentNum = rs.getInt("StudentNo");
								String studentNo = String.valueOf(studentNum);
								String lastName = rs.getString("LastName");
								String firstName = rs.getString("FirstName");
								float S_gpa = rs.getFloat("GPA");
								String gpa = String.valueOf(S_gpa);
								//System.out.println(studentNo,lastName,firstName,gpa);
								
			
								//STUDENT element
								Element student = doc.createElement("STUDENT");
								rootElement.appendChild(student);
								
								//StudentNo element
								Element Snumber = doc.createElement("StudentNo");
								Snumber.appendChild(doc.createTextNode(studentNo));
								student.appendChild(Snumber);
								
								//LastName
								Element LName = doc.createElement("LastName");
								LName.appendChild(doc.createTextNode(lastName));
								student.appendChild(LName);
								
								//FirstName
								Element FName = doc.createElement("FirstName");
								FName.appendChild(doc.createTextNode(firstName));
								student.appendChild(FName);
								
								//gpa
								Element SGPA = doc.createElement("GPA");
								SGPA.appendChild(doc.createTextNode(gpa));
								student.appendChild(SGPA);
								
								
							}
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.METHOD, "xml");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("U:\\cmpt354\\GPASummary.xml"));


				transformer.transform(source, result);

				System.out.println("File saved!");

				st.close();
			
			}
			catch (ParserConfigurationException pce) 
			{
				pce.printStackTrace();
			} 
			catch (TransformerException tfe) 
			{
				tfe.printStackTrace();
			}
		}
		
		catch (SQLException se)
		{
			System.out.println("\nSQL Exception occured, the state : "+
							se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
			return;
		}

			
    }
}