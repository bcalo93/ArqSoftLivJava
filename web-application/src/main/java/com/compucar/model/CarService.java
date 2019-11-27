package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class CarService extends MaintenanceItem {
    private LocalDateTime date;
    private int serviceTime;
    private double cost;

    @ManyToOne
    @JoinColumn
    private Client client;

    @ManyToOne
    @JoinColumn
    private Mechanic mechanic;

    @ManyToOne
    @JoinColumn
    private Reader reader;

    @ManyToOne
    @JoinColumn
    private Workshop workshop;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "service_id")
    private List<Diagnose> diagnoses;

    public void applyDiscount(int percentage) {
        this.cost *= ((double) (100 - percentage) / 100);
    }

    public void addDiagnose(Diagnose diagnose) {
        if(diagnoses == null) {
            diagnoses = new ArrayList<>();
        }

        int index = diagnoses.indexOf(diagnose);
        if(index != -1) {
            diagnoses.get(index).setResult(diagnose.getResult());
        } else {
            diagnoses.add(diagnose);
        }
    }
}
