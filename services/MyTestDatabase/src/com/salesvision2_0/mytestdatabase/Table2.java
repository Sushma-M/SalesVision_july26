/*Copyright (c) 2015-2016 wavemaker-com All Rights Reserved.This software is the confidential and proprietary information of wavemaker-com You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the source code license agreement you entered into with wavemaker-com*/
package com.salesvision2_0.mytestdatabase;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Table2 generated by WaveMaker Studio.
 */
@Entity
@Table(name = "`TABLE2`")
public class Table2 implements Serializable {

    private Integer id;
    private Integer column2;
    private Table1 table1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`ID`", nullable = false, scale = 0, precision = 10)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "`COLUMN2`", nullable = true, scale = 0, precision = 10)
    public Integer getColumn2() {
        return this.column2;
    }

    public void setColumn2(Integer column2) {
        this.column2 = column2;
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "table2")
    public Table1 getTable1() {
        return this.table1;
    }

    public void setTable1(Table1 table1) {
        this.table1 = table1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table2)) return false;
        final Table2 table2 = (Table2) o;
        return Objects.equals(getId(), table2.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

