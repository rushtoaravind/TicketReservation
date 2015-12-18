package com.company.ticketreservation.model;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by a1096282 on 12/10/15.
 */
@Entity(name = "AuditoriumMaster")
@Table(name="AUDITORIUM_MASTER")
public class AuditoriumMaster {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @Column(name="TIER_ID")
  private int tierId;

  @Column(name="TIER_NAME")
  private String tierName;

  @Column(name="TIER_ROWS")
  private int tierRows;

  @Column(name="TIER_ROW_CAPACITY")
  private int tierRowCapacity;

  @Column(name="TIER_TOTAL_CAPACITY")
  private int tierTotalCapacity;

  @Column(name="TIER_FEES")
  private Double tierFees;

  public int getTierId() {
    return tierId;
  }

  public void setTierId(int tierId) {
    this.tierId = tierId;
  }

  public String getTierName() {
    return tierName;
  }

  public void setTierName(String tierName) {
    this.tierName = tierName;
  }

  public int getTierRows() {
    return tierRows;
  }

  public void setTierRows(int tierRows) {
    this.tierRows = tierRows;
  }

  public int getTierRowCapacity() {
    return tierRowCapacity;
  }

  public void setTierRowCapacity(int tierRowCapacity) {
    this.tierRowCapacity = tierRowCapacity;
  }

  public int getTierTotalCapacity() {
    return tierTotalCapacity;
  }

  public void setTierTotalCapacity(int tierTotalCapacity) {
    this.tierTotalCapacity = tierTotalCapacity;
  }

  public Double getTierFees() {
    return tierFees;
  }

  public void setTierFees(Double tierFees) {
    this.tierFees = tierFees;
  }
}

