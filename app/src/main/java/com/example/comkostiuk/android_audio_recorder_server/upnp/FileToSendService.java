package com.example.comkostiuk.android_audio_recorder_server.upnp;

import com.example.comkostiuk.android_audio_recorder_server.xml.LecteurXml;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;
import org.xml.sax.SAXException;

import java.beans.PropertyChangeSupport;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by mkostiuk on 21/06/2017.
 */


@UpnpService(
        serviceType = @UpnpServiceType(value = "FileToSendService", version = 1),
        serviceId = @UpnpServiceId("FileTosendService")
)
public class FileToSendService {

    private final PropertyChangeSupport propertyChangeSupport;

    public FileToSendService() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpStateVariable(name = "PathFile")
    private String pathFile = "";

    @UpnpAction(name = "SetPathFile")
    public void setPathFile(@UpnpInputArgument(name = "NewPathFileValue") String p) throws IOException, SAXException, ParserConfigurationException {
        pathFile = p;
        if (!pathFile.equals("")) {
            LecteurXml l = new LecteurXml(pathFile);
            getPropertyChangeSupport().firePropertyChange("path", "", l.getChemin());
        }
    }
}
