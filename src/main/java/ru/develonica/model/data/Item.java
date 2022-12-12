package ru.develonica.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Объекта новости для парсинга xml файла.
 */
@Getter
@Setter
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "link")
    private String newsUrl;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "category")
    private String category;

    @XmlElement(name = "pubDate")
    private String publicDate;
}
