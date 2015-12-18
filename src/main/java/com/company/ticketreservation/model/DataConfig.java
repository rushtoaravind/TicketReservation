package com.company.ticketreservation.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * Created by a1096282 on 12/13/15.
 */
@Entity(name="DataConfig")
@Table(name="DATA_CONFIG")
public class DataConfig {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @Column(name="ID")
  private int id;

  @Column(name="DATA_TEXT")
  private String dataText;

  @Column(name="DATA_VALUE")
  private String dataValue;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDataText() {
    return dataText;
  }

  public void setDataText(String dataText) {
    this.dataText = dataText;
  }

  public String getDataValue() {
    return dataValue;
  }

  public void setDataValue(String dataValue) {
    this.dataValue = dataValue;
  }
}
