package com.example.comkostiuk.android_audio_recorder_server.upnp;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;

import java.beans.PropertyChangeSupport;

/**
 * Created by comkostiuk on 20/04/2017.
 */

@UpnpService(
        serviceType = @UpnpServiceType(value = "FileSenderController"),
        serviceId = @UpnpServiceId("FileSenderController")
)
public class FileSenderController {

    private final PropertyChangeSupport propertyChangeSupport;

    public FileSenderController() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }


    @UpnpStateVariable(defaultValue = "null")
    private String file = null;

    @UpnpAction
    public void setFile(@UpnpInputArgument(name = "NewFileValue") String newFileValue) {
        file = newFileValue;

        getPropertyChangeSupport().firePropertyChange("File", null, file);
    }


}
