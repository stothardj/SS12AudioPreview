import java.util.Vector;
 
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
 
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class SeatParse{

  private String xml_filename;

  public SeatParse(String xml_filename) 
  {
    this.xml_filename = xml_filename; 
  }

 public Vector sparse() {
    final Vector seats = new Vector();
    Vector llevel, rrow;
    String[] ttuple;
  try {
 
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();
 
    DefaultHandler handler = new DefaultHandler() {
 
    Vector level, row; 
    String[] tuple;

    String tag;
 
     public void startElement(String uri, String localName,
        String qName, Attributes attributes)
        throws SAXException {
 
        tag = qName;
     }
 
     public void endElement(String uri, String localName,
          String qName)
          throws SAXException {
 
          if (qName.equalsIgnoreCase("seat")) {
            row.add(tuple);
          } else if (qName.equalsIgnoreCase("row")) {
            level.add(row);
          } else if (qName.equalsIgnoreCase("level")) {
            seats.add(level);
          }
     }
 
     public void characters(char ch[], int start, int length)
         throws SAXException {
          if (tag.equalsIgnoreCase("id")) {
            tuple = new String[4];
            tuple[0] = new String(ch, start, length);
          } else if (tag.equalsIgnoreCase("x")) {
            tuple[1] = new String(ch, start, length);
          } else if (tag.equalsIgnoreCase("y")) {
            tuple[2] = new String(ch, start, length);
          } else if (tag.equalsIgnoreCase("z")) {
            tuple[3] = new String(ch, start, length);
          } else if (tag.equalsIgnoreCase("level")) {
            level = new Vector();
          } else if (tag.equalsIgnoreCase("row")) {
            row = new Vector();
          }     
          tag = "";
      }

    }; 
    saxParser.parse(this.xml_filename, handler);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return seats;
  }
}
