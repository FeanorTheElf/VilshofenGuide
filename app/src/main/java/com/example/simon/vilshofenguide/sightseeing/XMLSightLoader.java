package com.example.simon.vilshofenguide.sightseeing;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 06.10.2016.
 */
public class XMLSightLoader extends DefaultHandler {

    private ConfigurableSightManager m;

    private String lastRouteEnglishName;
    private String lastRouteGermanName;
    private List<Sight> lastRouteSights = new LinkedList<>();

    private int lastSightId;
    private String lastSightNameEN;
    private String lastSightNameDE;
    private double lastSightLat;
    private double lastSightLng;
    private String lastSightNameHTML;
    private double lastSightVisitDuration;
    private Map<Category, Integer> lastSightPoints = new HashMap<>();

    public void startDocument() throws SAXException {
        m = ConfigurableSightManager.getEmptySightManager();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) {
        if (qName.equals("sight")) {
            lastSightPoints.clear();
            this.lastSightId = Integer.parseInt(attributes.getValue("id"));
            this.lastSightNameEN = attributes.getValue("english_name");
            this.lastSightNameDE = attributes.getValue("german_name");
            this.lastSightLat = Double.parseDouble(attributes.getValue("Lat"));
            this.lastSightLng = Double.parseDouble(attributes.getValue("Lng"));
            this.lastSightNameHTML = attributes.getValue("html_name");
            this.lastSightVisitDuration = Double.parseDouble(attributes.getValue("visit_duration"));
        }else if (qName.equals("points")){
            lastSightPoints.put(Category.valueOf(attributes.getValue("cat")),
                    Integer.parseInt(attributes.getValue("value")));
        }else if (qName.equals("route")){
            lastRouteEnglishName = attributes.getValue("english_name");
            lastRouteGermanName = attributes.getValue("german_name");
            lastRouteSights.clear();
        }else if (qName.equals("waypoint")){
            lastRouteSights.add(m.getSightById(Integer.parseInt(attributes.getValue("id"))));
        }else if (qName.equals("param")){
            double[] param = new double[attributes.getLength() - 1];
            param[0] = Double.parseDouble(attributes.getValue("a"));
            param[1] = Double.parseDouble(attributes.getValue("b"));
            m.setParam(Category.valueOf(attributes.getValue("cat")), param);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException{
        if (qName.equals("sight")){
            m.addSight(new Sight(lastSightId, new HashMap<>(lastSightPoints),
                    this.lastSightLat, this.lastSightLng,
                    this.lastSightNameEN, this.lastSightNameDE,
                    this.lastSightNameHTML, this.lastSightVisitDuration));
        }else if (qName.equals("route")){
            m.addRoute(new NamedRoute(new Path(new LinkedList<>(this.lastRouteSights)),
                    this.lastRouteEnglishName,
                    this.lastRouteGermanName));
        }
    }

    public SightManager getSightManager(){
        return m;
    }
}