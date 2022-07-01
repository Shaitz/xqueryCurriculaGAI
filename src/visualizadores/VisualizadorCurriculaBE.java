package visualizadores;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.xquery.*;

import com.saxonica.xqj.*;

public class VisualizadorCurriculaBE
{
  public static void main(String[] args) throws XQException
  {
	String fileName = "output/belgica.html";
	
	// create the data source and expression to process
	
    XQDataSource xqs = new SaxonXQDataSource();
    XQConnection conn = xqs.getConnection();
    XQExpression xqe = conn.createExpression();
    
    // the query to process
    String xqueryString = "declare namespace e='http://www.erasmus.org';" +
    "declare namespace be='http://www.belgica.org';" +
    		"let $candidate := (doc('input/AlexBen.xml')//e:Candidate)"+
    		" return <html><h1>List of Candidates</h1> "+
    		" <table border='1'><tr><th>Name</th><th>Surname</th><th>Contact Info</th><th>Passed credits</th><th>Language level</th><th>Language level</th><th>Subject</th><th>Subject</th><th>Subject</th><th>Origin University</th><th>Destiny University</th><th>Address</th><th>Educational History</th><th>Educational History</th><th>Programme</th></tr> "+
    		" { "+
    		"    for $x in $candidate "+
    		"    return <tr><td>{data($x/e:Name)}</td><td>{data($x/e:Surname)}</td><td>{data($x/e:ContactInfo)}</td><td>{data($x/e:PassedCredits)}</td>{for $lan in $x/e:LanguageLevel return <td>{data($lan/@Language)}{data($lan)}</td>}{for $sub in $x/e:Subject return <td>{data($sub)}</td>}<td>{data($x/e:OriginUniversity)}</td><td>{data($x/e:DestinyUniversity)}</td><td>{data($x/be:address)}</td>{for $ed in $x/be:edHistory return <td>{data($ed)}</td>}<td>{data($x/be:programme)}</td></tr> "+
    		" } "+
    		" </table></html>";
    XQResultSequence rs = xqe.executeQuery(xqueryString);
 
    try {
        
        BufferedWriter bufferedWriter =
            new BufferedWriter(new FileWriter(fileName));
        
        // write the result of the query in the file
        while(rs.next()) {
        	String aline = rs.getItemAsString(null);
        	bufferedWriter.write(aline);
        	bufferedWriter.newLine();
        }
        
        bufferedWriter.close();
    }
    catch(IOException ex) {
        System.out.println(
            "Error writing to file '"
            + fileName + "'");
    }
    
    System.out.println("Finished!!");
    conn.close();
  }
}