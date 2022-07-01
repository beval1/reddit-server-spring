package com.beval.server.model.entity;

import com.beval.server.model.enums.RoleEnum;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Entity
public class RoleEntity extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;
}
