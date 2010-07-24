package com.rameses.system;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="sys_date_bean")
public class DateBean implements Serializable {
    
    @Id
    private Long id = new Long(1);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
