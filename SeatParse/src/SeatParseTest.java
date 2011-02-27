import java.util.Vector;
 
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
 
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SeatParseTest {


  public static void main(String [] args) 
  {

    try {
      SeatParse sp = new SeatParse("rosebowl.xml");
      Vector seats = sp.sparse();

     Vector llevel, rrow;
     String[] ttuple;

     System.out.println(seats.size());
     for (int i = 0; i < seats.size(); i++) {
       System.out.println("levels");
       llevel = (Vector)(seats.lastElement());
       for (int j = 0; j < llevel.size(); ++j) {
         rrow =  (Vector)(llevel.lastElement());
         for (int k = 0; k < rrow.size(); ++k) {
           System.out.println("NEW SEAT");
           ttuple = (String[])(rrow.elementAt(k));
           System.out.println(ttuple[0]);
           System.out.println(ttuple[1]);
           System.out.println(ttuple[2]);
           System.out.println(ttuple[3]);
         }
       }
     }
 
   } catch (Exception e) {
      System.out.println("oh no!");
   }

  }


}
