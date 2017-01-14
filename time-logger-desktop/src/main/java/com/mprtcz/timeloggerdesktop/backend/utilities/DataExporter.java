package com.mprtcz.timeloggerdesktop.backend.utilities;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.model.HoursData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by mprtcz on 2017-01-13.
 */
public class DataExporter {
    private static final String PATH = "data.xml";

    public static void exportData(HoursData.Hour[][] hoursArray) {
        try {
            Document document = createXMLDocument();
            Element rootElement = document.createElement("days");
            document.appendChild(rootElement);
            for (HoursData.Hour[] aHoursArray : hoursArray) {
                Element dayElement = document.createElement("day");
                dayElement.setAttribute("date", getLinesDate(aHoursArray));
                rootElement.appendChild(dayElement);
                Element hoursElement = document.createElement("hours");
                dayElement.appendChild(hoursElement);
                for (int j = 0; j < hoursArray[0].length; j++) {
                    Element hourElement = document.createElement("hour");
                    HoursData.Hour hour = aHoursArray[j];
                    if (hour != null) {
                        hourElement.setAttribute("id", hour.getDatetime().toLocalTime().toString());
                        hoursElement.appendChild(hourElement);
                        if (!hour.getActivitiesDuringThisHour().isEmpty()) {
                            Element activitiesElement = document.createElement("activities");
                            hourElement.appendChild(activitiesElement);
                            for (Activity activity :
                                    hour.getActivitiesDuringThisHour()) {
                                Element activityElement = document.createElement("activity");
                                activityElement.setAttribute("id", activity.getId().toString());
                                activityElement.setAttribute("name", activity.getName());
                                activitiesElement.appendChild(activityElement);
                            }
                        }
                    }
                }
            }
            saveFile(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getLinesDate(HoursData.Hour[] dayLine) {
        for (HoursData.Hour hour :
                dayLine) {
            if (hour != null) {
                return hour.getDatetime().toLocalDate().toString();
            }
        }
        return "Unknown";
    }

    private static void saveFile(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource dom = new DOMSource(document);
        StreamResult result = new StreamResult(new File(PATH));
        transformer.transform(dom, result);
    }

    private static Document createXMLDocument() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        return docBuilder.newDocument();
    }
}
