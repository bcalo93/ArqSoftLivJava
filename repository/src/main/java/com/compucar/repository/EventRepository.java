package com.compucar.repository;

import com.compucar.model.Event;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class EventRepository {

    @Autowired
    @Qualifier("cassandraSession")
    private Session session;

    private Mapper<Event> mapper;

    private EventAccessor entityAccessor;

    @PostConstruct
    private void init() {
        MappingManager manager = new MappingManager(this.session);
        this.mapper = manager.mapper(Event.class);
        this.entityAccessor = manager.createAccessor(EventAccessor.class);
    }

    public void save(Event event) {
        this.mapper.save(event);
    }

    public List<String> getNamesByServiceCode(String serviceCode) {
        ResultSet resultSet = entityAccessor.getNamesByServiceCode(serviceCode);
        List<String> eventNames = new ArrayList<>();
        for (Row row : resultSet) {
            String name = row.getString("event_name");
            eventNames.add(name);
        }
        return eventNames;
    }

    public List<Event> getEventsByServiceCodeAndName(String serviceCode, String eventName) {
        ResultSet resultSet = entityAccessor.getByServiceCodeAndName(serviceCode, eventName);
        List<Event> result = mapper.map(resultSet).all();
        return result;
    }
}
