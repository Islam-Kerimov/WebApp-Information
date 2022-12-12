package ru.develonica.parser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.MalformedURLException;
import java.net.URL;

import static javax.xml.bind.JAXBContext.newInstance;

/**
 * XML парсер.
 *
 * @param <T> тип объекта для парсинга.
 */
public class XmlParser<T> {

    public Object parse(String url, Class<T> clazz) throws JAXBException, MalformedURLException {
        JAXBContext jaxbContext = newInstance(clazz);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        return unmarshaller.unmarshal(new URL(url));
    }
}
