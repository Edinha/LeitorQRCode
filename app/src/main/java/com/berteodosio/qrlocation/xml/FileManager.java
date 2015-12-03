package com.berteodosio.qrlocation.xml;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Xml;

import com.berteodosio.qrlocation.model.Place;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public final static String FILE_NAME = "/places.xml";               // arquivo
    public final static String ROOT_TAG = "place";                      // nó raiz
    public final static String CHILD_ONE_LOCATION = "location";         // nó location
    public final static String CHILD_ONE_ONE_LATITUDE = "latitude";     // nó latitude
    public final static String CHILD_ONE_TWO_LONGITUDE = "longitude";   // nó longitude
    public final static String CHILD_TWO_INFO = "info";                 // nó info

    public static void write(Place place, Context context) throws IOException {
        Resources resources = context.getResources();
        String appName = resources.getText(resources.getIdentifier("app_name", "string",
                context.getPackageName())).toString();

        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirPath = externalPath.concat("/").concat(appName);  // pasta onde ficará o xml
        File dir = new File(dirPath);

        if (!dir.exists()) {
            dir.mkdir();
        }

        boolean firstTime = false;
        String filePath = dirPath.concat(FILE_NAME);
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
            firstTime = true;
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);
        if (firstTime) {
            xmlSerializer.startDocument("UTF-8", true);     // cabeçalho do xml
        }

        // a partir daqui, a escrita é feita
        xmlSerializer.startTag(null, ROOT_TAG);
        xmlSerializer.startTag(null, CHILD_ONE_LOCATION);

        xmlSerializer.startTag(null, CHILD_ONE_ONE_LATITUDE);
        String latitude = String.valueOf(place.getLocation().getLatitude());
        xmlSerializer.text(latitude);
        xmlSerializer.endTag(null, CHILD_ONE_ONE_LATITUDE);

        xmlSerializer.startTag(null, CHILD_ONE_TWO_LONGITUDE);
        String longitude = String.valueOf(place.getLocation().getLongitude());
        xmlSerializer.text(longitude);
        xmlSerializer.endTag(null, CHILD_ONE_TWO_LONGITUDE);

        xmlSerializer.endTag(null, CHILD_ONE_LOCATION);

        xmlSerializer.startTag(null, CHILD_TWO_INFO);
        xmlSerializer.text(place.getText());
        xmlSerializer.endTag(null, CHILD_TWO_INFO);
        xmlSerializer.endTag(null, ROOT_TAG);

        xmlSerializer.endDocument();
        xmlSerializer.flush();

        String data = writer.toString();

        fileOutputStream.write(data.getBytes());    // escreve de fato no arquivo
        fileOutputStream.close();
    }

    public static List<Place> readPlaces(Context context) throws XmlPullParserException, IOException {
        List<Place> places = new ArrayList<>();
        Resources resources = context.getResources();
        String appName = resources.getText(resources.getIdentifier("app_name", "string",
                context.getPackageName())).toString();
        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = externalPath.concat("/").concat(appName).concat(FILE_NAME);
        File file = new File(path);

        if (!file.exists())
            return places;      // uma lista vazia

        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
        FileInputStream fileInputStream = new FileInputStream(file);
        xmlPullParser.setInput(fileInputStream, null);

        int eventCode = xmlPullParser.getEventType();

        // le o arquivo
        while (eventCode != XmlPullParser.END_DOCUMENT) {
            String tag = null;
            Place place = new Place();
            xmlPullParser.next();

            do {
                eventCode = xmlPullParser.next();
                if (eventCode == XmlPullParser.END_DOCUMENT)
                    return places;

                tag = xmlPullParser.getName();
                switch (eventCode) {
                    case XmlPullParser.START_TAG: {
                        if (tag.equalsIgnoreCase(CHILD_ONE_ONE_LATITUDE)) {
                            xmlPullParser.next();
                            place.setLocationLatitude(Double.parseDouble(xmlPullParser.getText()));
                        }
                        else if (tag.equalsIgnoreCase(CHILD_ONE_TWO_LONGITUDE)) {
                            xmlPullParser.next();
                            place.setLocationLongitude(Double.parseDouble(xmlPullParser.getText()));
                        }
                        else if (tag.equalsIgnoreCase(CHILD_TWO_INFO)) {
                            xmlPullParser.next();
                            place.setText(xmlPullParser.getText());
                        }
                    }

                }
            }
            while (!tag.equalsIgnoreCase(ROOT_TAG));

            places.add(place);
        }
        return places;
    }
}
