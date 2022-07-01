package visualizadores;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;

import com.saxonica.xqj.SaxonXQDataSource;

public class VisualizadorCurriculaFR {
	public static void main(String[] args) throws XQException
	  {
		String fileName = "output/france.html";
		
		// create the data source and expression to process
		
	    XQDataSource xqs = new SaxonXQDataSource();
	    XQConnection conn = xqs.getConnection();
	    XQExpression xqe = conn.createExpression();
	    
	    // the query to process
	    String xqueryString = "declare namespace e=\"http://www.erasmus.org\";"
	    		+ "declare namespace fr=\"http://www.france.org\";"
	    		+ "let $candidate := (doc('input/ChrisMandel.xml')//e:Candidate)"+
	    		" return <html><h1>Erasamus Enrollment</h1> "
	    		+ "<table border='1'>"
	    		+ "<tr>"
	    		+ "<td>Name</td><td>Surname</td><td>Telephone</td><td>Email</td>"
	    		+ "<td>PassedCredits</td>"
	    		+ "{for $l in $candidate/e:LanguageLevel return <td>{data($l/@Language)}</td>}"
	    		+ "{for $s in $candidate/e:Subject return <td>Subject</td>}"
	    		+ "{for $u in $candidate/e:OriginUniversity return <td>OriginUniversity</td>}"
	    		+ "<td>DestinyUniversity</td>"
	    		+ "</tr>"
	    		+ "{"
	    		+ "for $c in $candidate "
	    		+ "return <tr>"
	    		+ "<td>{data($c/e:Name)}</td>"
	    		+ "<td>{data($c/e:Surname)}</td>"
	    		+ "<td>{for $tel in $c/e:ContactInfo/e:Telephone return $tel}</td>"
	    		+ "<td>{for $em in $c/e:ContactInfo/e:Email return $em}</td>"
	    		+ "<td>{data($c/e:PassedCredits)}</td>"
	    		+ "{for $l in $c/e:LanguageLevel return <td>{data($l/Level)}</td>}"
	    		+ "{for $s in $c/e:Subject return <td><table border='1' solid='true'><tr>"
	    		+ "<td>Name</td><td>Credits</td><td>Tutor</td><td>Speciality</td>"
	    		+ "</tr><tr>"
	    		+ "<td>{data($s/e:Name)}</td>"
	    		+ "<td>{data($s/e:Credits)}</td>"
	    		+ "<td>{data($s/e:Tutor)}</td>"
	    		+ "<td>{data($s/e:Speciality)}</td>"
	    		+ "</tr></table></td>}"
	    		+ "{for $u in $c/e:OriginUniversity return <td><table border='1' solid='true'><tr>"
	    		+ "<td>Name</td><td>ContactEmail</td><td>Country</td>"
	    		+ "</tr><tr>"
	    		+ "<td>{data($u/e:Name)}</td>"
	    		+ "<td>{data($u/e:Contact_email)}</td>"
	    		+ "<td>{data($u/e:Country)}</td>"
	    		+ "</tr></table></td>}"
	    		+ "<td><table border='1' solid='true'><tr>"
	    		+ "<td>Name</td><td>Credits</td><td>Tutor</td><td>Speciality</td>"
	    		+ "</tr><tr>"
	    		+ "<td>{data($c/e:DestinyUniversity/e:Name)}</td>"
	    		+ "<td>{data($c/e:DestinyUniversity/e:Contact_email)}</td>"
	    		+ "<td>{data($c/e:DestinyUniversity/e:Country)}</td>"
	    		+ "</tr></table></td>"
	    		+ "<td>{data($c/fr:Accomodation)}</td></tr>"
	    		+ "}"
	    		+ "</table>"+
	    		"</html>";
	    
	    XQResultSequence rs = xqe.executeQuery(xqueryString);
	 
	    try {
	        
	        BufferedWriter bufferedWriter =
	            new BufferedWriter(new FileWriter(fileName));
	        
	        // write the result of the query in the file
	        while(rs.next()) {
	        	String line = rs.getItemAsString(null);
	        	bufferedWriter.write(line);
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
