package com.ekar.counter.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "request_tb")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int consumerTasks;

    @NotNull
    private int producerTasks;

    @Column(nullable = false, updatable = false, name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date dateCreated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getConsumerTasks() {
        return consumerTasks;
    }

    public void setConsumerTasks(int consumerTasks) {
        this.consumerTasks = consumerTasks;
    }

    public int getProducerTasks() {
        return producerTasks;
    }

    public void setProducerTasks(int producerTasks) {
        this.producerTasks = producerTasks;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }


}
