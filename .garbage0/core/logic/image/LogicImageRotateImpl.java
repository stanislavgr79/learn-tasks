package com.epam.aem.core.logic.image;

import com.day.image.Layer;
import org.osgi.service.component.annotations.Component;

@Component(service = LogicImage.class)
public class LogicImageRotateImpl implements LogicImage{

    @Override
    public Layer changeImageByConcreteFilterConfigEnvironment(Layer layer) {
            layer.rotate(180);
        return layer;
    }
}
