package com.example.cinema.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "order")
public class Order extends AuditableEntity {

}
