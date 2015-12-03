package com.example.edinha.qr;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 30/11/15.
 */
//todos os métodos devem ser chamados dentro de assync task
public class XMLManager {

    public final static String FILE_NAME = "/Localidades.xml";//Nome do arquivo XML
    public final static String MAIN_OBJECT_NAME = "LOCAL"; // primeira tag,corresponde a um obj Local
    public final static String INTERNAL_OBJECT_NAME = "LOCATION"; //objeto location
    public final static String INTERNAL_FIELD_ONE = "LATITUDE";//campo latitude
    public final static String INTERNAL_FIELD_TWO = "LONGITUDE";//campo longitude
    public final static String FIELD_ONE = "TEXT";//campo que equivale ao texto lido do QRCode
    public final static String APP_NAME = "app_name";

    //Salva no SD card no External Storage do aplicativo, estando disponível
    public static void writeLocal(Local local, Context context) throws IOException {
        Resources resources = context.getResources();
        String applicationName = resources.getText(resources.getIdentifier(APP_NAME, "string", context.getPackageName())).toString();
        String externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirPath = externalStorage.concat("/").concat(applicationName);
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

        FileOutputStream fileOutputStream = new FileOutputStream(file, true);//append
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        serializer.setOutput(writer);
        if (firstTime) {
            serializer.startDocument("UTF-8", true);
        }

        // inicia e fecha as tags de acordo com a ordem estabelecida
        serializer.startTag(null, MAIN_OBJECT_NAME);
        serializer.startTag(null, INTERNAL_OBJECT_NAME);
        serializer.startTag(null, INTERNAL_FIELD_ONE);
        serializer.text(String.valueOf(local.getLocation().getLatitude()));
        serializer.endTag(null, INTERNAL_FIELD_ONE);
        serializer.startTag(null, INTERNAL_FIELD_TWO);
        serializer.text(String.valueOf(local.getLocation().getLongitude()));
        serializer.endTag(null, INTERNAL_FIELD_TWO);
        serializer.endTag(null, INTERNAL_OBJECT_NAME);
        serializer.startTag(null, FIELD_ONE);
        serializer.text(local.getText());
        serializer.endTag(null, FIELD_ONE);
        serializer.endTag(null, MAIN_OBJECT_NAME);
        serializer.endDocument();
        serializer.flush();
        String data = writer.toString();
        fileOutputStream.write(data.getBytes());
        fileOutputStream.close();
    }

    // irá ler do XML já salvo as localizações e o texto, procurando pelas tags e parseando o XML
    public static List<Local> readAll(Context context) throws XmlPullParserException, IOException {
        List<Local> locals = new ArrayList<>();
        Resources resources = context.getResources();
        String applicationName = resources.getText(resources.getIdentifier(APP_NAME, "string", context.getPackageName())).toString();
        String externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = externalStorage.concat("/").concat(applicationName).concat(FILE_NAME);
        File file = new File(path);
        if (!file.exists()) {
            return locals;
        }

        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = xmlPullParserFactory.newPullParser();
        FileInputStream fileInputStream = new FileInputStream(file);
        parser.setInput(fileInputStream, null);
        int event = parser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {
            String tagName = null;
            Local local = new Local();
            parser.next();

            do {
                event = parser.next();
                if (event == XmlPullParser.END_DOCUMENT) {
                    return locals;

                }
                tagName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG: {
                        if (tagName.equalsIgnoreCase(INTERNAL_FIELD_ONE)) {
                            parser.next();
                            local.setLocationLatitude(Double.parseDouble(parser.getText()));

                        } else {

                            if (tagName.equalsIgnoreCase(INTERNAL_FIELD_TWO)) {
                                parser.next();
                                local.setLocationLongitude(Double.parseDouble(parser.getText()));
                            } else {

                                if (tagName.equalsIgnoreCase(FIELD_ONE)) {
                                    parser.next();
                                    local.setText(parser.getText());
                                }
                            }
                        }

                    }


                }

            }
            while (!tagName.equalsIgnoreCase(MAIN_OBJECT_NAME));

            locals.add(local);

        }
        return locals;

    }

}
