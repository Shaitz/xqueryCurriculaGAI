package visualizadores;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.xquery.*;

import com.saxonica.xqj.*;

public class VisualizadorCurriculaES
{
  public static void main(String[] args) throws XQException
  {
	String fileName = "output/spain.html";
	
	// create the data source and expression to process
	
    XQDataSource xqs = new SaxonXQDataSource();
    XQConnection conn = xqs.getConnection();
    XQExpression xqe = conn.createExpression();
    String xmlPath = "pepeGarcía.xml";
    
    String xqueryString = "declare namespace e='http://www.erasmus.org'; declare namespace esp='http://www.erasmusEsp.org';"
    		+ "let $curriculum := (doc('input/"+xmlPath+"')//e:Candidate)"+
    		" return <html><h1>List of Spanish Students</h1> "+
    		" <table border='1'><tr><th>ID</th><th>Date</th><th>Name</th><th>Surname</th><th>Contact Info</th><th>Passed Credits</th>"
    		+ "{for $l in $curriculum//e:LanguageLevel return <th>Language Level</th>}{for $s in $curriculum//e:Subject return <th>Subject</th>}"
    		+ "<th>Origin University</th><th></th>Destiny University<th>Previous University</th>{for $h in $curriculum//esp:Hobby return <th>Hobby</th>}</tr> "
    		+"{"
    		+ "for $c in $curriculum "
    		+ "return <tr>"
    		+ "<td>{data($c/@ID)}</td>"
    		+ "<td><li>From: {data($c/@ValidFrom)}</li><li>To: {data($c/@ValidTo)}</li></td>"
    		+ "<td>{data($c/e:Name)}</td>"
    		+ "<td>{data($c/e:Surname)}</td>"
    		+ "<td>{data($c/e:ContactInfo)}</td>"
    		+ "<td>{data($c/e:PassedCredits)}</td>"
    		+ "<td>{for $l in $c/e:LanguageLevel return concat(concat(data($l/e:Level), '  '), data($l/@Language))}</td>"
    		+ "{for $s in $c/e:Subject return <td>Term: {data($s/@Term)}<li>Name: {data($s/e:Name)}</li> <li>Credits: {data($s/e:Credits)}</li><li>Tutor: {data($s/e:Tutor)}</li><li>Speciality: {data($s/e:Speciality)}</li></td>}"
    		+ "<td><li>Name: {data($c/e:OriginUniversity/e:Name)}</li><li>Email: {data($c/e:OriginUniversity/e:Contact_email)}</li><li>Country: {data($c/e:OriginUniversity/e:Country)}</li><li>Province: {data($c/e:OriginUniversity/esp:Province)}</li></td>"
    		+ "<td><li>Name: {data($c/e:DestinyUniversity/e:Name)}</li><li>Email: {data($c/e:DestinyUniversity/e:Contact_email)}</li><li>Country: {data($c/e:DestinyUniversity/e:Country)}</li></td>"
    		+ "<td><li>Name: {data($c/esp:extras/esp:UniversidadPrevia/e:Name)}</li><li>Email: {data($c/esp:extras/esp:UniversidadPrevia/e:Contact_email)}</li><li>Country: {data($c/esp:extras/esp:UniversidadPrevia/e:Country)}</li><li>Province: {data($c/esp:extras/esp:UniversidadPrevia/esp:Province)}</li></td>"
    		+ "{for $h in $c/esp:extras//esp:Hobby return <td>{data($h)}</td>}"
    		+ "</tr>"
    		+"} </table></html>";
    
    XQResultSequence rs = xqe.executeQuery(xqueryString);
 
    try {
        
        BufferedWriter bufferedWriter =
            new BufferedWriter(new FileWriter(fileName));
        
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