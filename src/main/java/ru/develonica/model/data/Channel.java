package ru.develonica.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Объекта списка новостей для парсинга xml файла.
 */
@Getter
@Setter
@XmlRootElement(name = "channel")
@XmlAccessorType(XmlAccessType.FIELD)
public class Channel {

    @XmlElement(name = "title")
    private String newsName;

    @XmlElement(name = "item")
    private List<Item> items = null;
}
