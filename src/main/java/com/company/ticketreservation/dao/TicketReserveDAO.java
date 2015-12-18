package com.company.ticketreservation.dao;

import com.company.ticketreservation.model.AuditoriumMaster;
import com.company.ticketreservation.model.DataConfig;
import com.company.ticketreservation.model.SeatRepository;
import com.company.ticketreservation.model.UserReservation;

import java.util.List;
import java.util.Map;

/**
 * This class acts a data persistent layer for the Ticker reservation system.
 */
public interface TicketReserveDAO {
  /**
   *
   * @return
   */
  public List<AuditoriumMaster> getAuditoriumDetails();

  public List<DataConfig> getConfigDetails();

  /**
   *
   */
  public void insertSeatDetails();

  /**
   *
   * @param venueLevel
   * @return
   */
  public int getSeatsAvailable(Integer venueLevel);

  /**
   *
   * @param numSeats
   * @param minLevel
   * @param maxLevel
   * @param customerEmail
   * @param tierCost
   * @return
   */
  public Map findAndHoldSeats(int numSeats, Integer minLevel,
      Integer maxLevel, String customerEmail,Map tierCost);

  /**
   *
   * @param reservationId
   */
  public void deleteReservationHold(Integer reservationId);
  /**
   * Commit seats held for a specific customer
   *
   * @param seatHoldId the seat hold identifier
   * @return a reservation confirmation code
   */
  public void reserveSeats(int seatHoldId);

}
