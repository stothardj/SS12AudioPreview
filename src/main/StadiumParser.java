package main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.obj.Vertex;
/**
 * Parse an xml file defining venues 
 * @author Jake Stothard
 *
 */
public class StadiumParser {
	public StadiumParser() {}
	public List<Venue> parse(URL url) throws ParserConfigurationException, SAXException, IOException {
		File fXmlFile = new File(url.getFile());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		NodeList venList = doc.getElementsByTagName("venue");
		List<Venue> ret = new LinkedList<Venue>();
		for( int venueNum = 0; venueNum < venList.getLength(); venueNum++) {
			Venue v = new Venue();
			Node venNode = venList.item(venueNum);
			if( venNode.getNodeType() == Node.ELEMENT_NODE) {
				Element venue = (Element) venNode;
				v.setName(getTagValue("venuename", venue));
				NodeList seatAreaList = venue.getElementsByTagName("seatarea");
				for( int seatAreaNum = 0; seatAreaNum < seatAreaList.getLength(); seatAreaNum++) {
					Node saNode = seatAreaList.item(seatAreaNum);
					if( saNode.getNodeType() == Node.ELEMENT_NODE) {
						Element seatArea = (Element) saNode;
						SeatArea sa = new SeatArea();
						sa.setName(getTagValue("areaname", seatArea));
						sa.setRows(Integer.parseInt(getTagValue("nrows", seatArea)));
						sa.setSeatsPerRow(Integer.parseInt(getTagValue("seatsprow", seatArea)));
						sa.setSlant(Integer.parseInt(getTagValue("slant", seatArea)));
						Element firstpos = (Element) seatArea.getElementsByTagName("firstspos").item(0);
						sa.setFirstPos(new Vertex(
								Float.parseFloat(getTagValue("xpos", firstpos)),
								Float.parseFloat(getTagValue("ypos", firstpos)),
								Float.parseFloat(getTagValue("zpos", firstpos))
						));
						sa.setSeatPadding(Integer.parseInt(getTagValue("seatpadding", seatArea)));
						sa.setRowPadding(Integer.parseInt(getTagValue("rowpadding", seatArea)));
						v.addSeatArea(sa);
					}
				}
			}
			ret.add(v);
		}
		return ret;
	}
	
	private String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}
	
}
