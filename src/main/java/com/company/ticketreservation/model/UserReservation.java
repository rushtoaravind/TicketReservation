package com.company.ticketreservation.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by a1096282 on 12/12/15.
 */
@Entity(name = "UserReservation")
@Table(name="USER_RESERVATION")
public class UserReservation {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @Column(name = "RESERVATION_ID")
  private int reservationId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "CUSTOMER_EMAIL")
  private String customerEmailId;

  @Column(name = "SEAT_ON_HOLD")
  private String seatOnHold;

  @Column(name = "SEAT_RESERVED")
  private String seatReserved;

  @Column(name = "CREATED_TIME_STAMP")
  private Date createdTimeStamp;

  @Column(name = "UPDATED_TIME_STAMP")
  private Date updatedTimeStamp;

  @Column(name="RESERVATION_AMOUNT")
  private Double reservationAmount;

  @Transient
  private int tierId;

  @Transient
  private int seatId;

  @Transient
  private int seatRepositoryId;


 public int getReservationId() {
    return reservationId;
  }

  public void setReservationId(int reservationId) {
    this.reservationId = reservationId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getCustomerEmailId() {
    return customerEmailId;
  }

  public void setCustomerEmailId(String customerEmailId) {
    this.customerEmailId = customerEmailId;
  }

  public String getSeatOnHold() {
    return seatOnHold;
  }

  public void setSeatOnHold(String seatOnHold) {
    this.seatOnHold = seatOnHold;
  }

  public String getSeatReserved() {
    return seatReserved;
  }

  public void setSeatReserved(String seatReserved) {
    this.seatReserved = seatReserved;
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

  public Double getReservationAmount() {
    return reservationAmount;
  }

  public void setReservationAmount(Double reservationAmount) {
    this.reservationAmount = reservationAmount;
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

  public int getSeatRepositoryId() {
    return seatRepositoryId;
  }

  public void setSeatRepositoryId(int seatRepositoryId) {
    this.seatRepositoryId = seatRepositoryId;
  }

}
