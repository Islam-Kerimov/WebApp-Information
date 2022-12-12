package ru.develonica.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.develonica.model.entity.TaskProperties;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Корневой класс запланированных задач.
 */
@Slf4j
public abstract class ScheduledTask<T> implements Runnable {

    private final TaskProperties taskProperties;

    private final ObjectMapper objectMapper;

    private final Class<T> clazz;

    protected ScheduledTask(TaskProperties taskProperties, Class<T> clazz) {
        this.taskProperties = taskProperties;
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
        this.clazz = clazz;
    }

    /**
     * Запуск задачи.
     */
    @Override
    public void run() {
        try {
            process();
        } catch (JsonProcessingException jpe) {
            log.error(jpe.getCause().getMessage());
        } catch (MalformedURLException mue) {
            log.error("Error work with URL: " + mue.getCause().getMessage());
        } catch (JAXBException jaxbe) {
            log.error("Error work with JAXB API: " + jaxbe.getCause().getMessage());
        } catch (IOException ioe) {
            log.error("Error work with Jackson: " + ioe.getCause().getMessage());
        }
    }

    /**
     * Десериализация опций из базы данных в объект Options.
     *
     * @return объект, который хранит опции.
     * @throws JsonProcessingException при работе с Jackson.
     */
    protected T getOptions() throws JsonProcessingException {
        String options = objectMapper.writeValueAsString(taskProperties.getOptions());
        return objectMapper.readValue(options, clazz);
    }

    protected abstract void process() throws IOException, JAXBException;
}
