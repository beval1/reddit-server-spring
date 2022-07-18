package com.beval.server.model.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
//this doesn't work with BaseEntityRepository
//@MappedSuperclass
public abstract class BaseEntity {
    //GenerationType.Identity doesn't work because of BaseEntityRepository
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;

}
