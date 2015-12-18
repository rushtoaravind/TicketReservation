package com.company.ticketreservation.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by a1096282 on 12/11/15.
 */
@Entity(name="SeatRepository")
@Table(name="SEAT_RESERVATION_REPOSITORY")
public class SeatRepository {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @Column(name="ID")
  public int id;

  @Column(name="TIER_ID")
  private int tierId;

  @Column(name="SEAT_ID")
  private int seatId;

  @Column(name="SEAT_AVAILABLE")
  private String seatAvailable;

  @Column(name="RESERVATION_ID")
  private int reservationId;

  @Column(name = "CREATED_TIME_STAMP")
  private Date createdTimeStamp;

  @Column(name = "UPDATED_TIME_STAMP")
  private Date updatedTimeStamp;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getTierId() {
    return tierId;
  }

  public void setTierId(int tierId) {
    this.tierId = tierId;
  }

  public int getSeatId() {
    return seatId;
  }

  public void setSeatId(int seatId) {
    this.seatId = seatId;
  }

  public String getSeatAvailable() {
    return seatAvailable;
  }

  public void setSeatAvailable(String seatAvailable) {
    this.seatAvailable = seatAvailable;
  }

  public int getReservationId() {
    return reservationId;
  }

  public void setReservationId(int reservationId) {
    this.reservationId = reservationId;
  }

  public Date getCreatedTimeStamp() {
    return createdTimeStamp;
  }

  public void setCreatedTimeStamp(Date createdTimeStamp) {
    this.createdTimeStamp = createdTimeStamp;
  }

  public Date getUpdatedTimeStamp() {
    return updatedTimeStamp;
  }

  public void setUpdatedTimeStamp(Date updatedTimeStamp) {
    this.updatedTimeStamp = updatedTimeStamp;
  }
}
