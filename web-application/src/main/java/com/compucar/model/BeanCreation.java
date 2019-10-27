package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class BeanCreation extends DataEntity {
    LocalDateTime date;
    String name;
    String className;

    public BeanCreation() {
        this.date = LocalDateTime.now();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("***************************** Bean created *****************************");
        sb.append("\n");
        sb.append("Date: ");
        sb.append(date.toString());
        sb.append("\n");
        sb.append("Name: ");
        sb.append(name);
        sb.append("\n");
        sb.append("Class: ");
        sb.append(className);
        sb.append("\n");
        sb.append("************************************************************************");
        return sb.toString();
    }
}
