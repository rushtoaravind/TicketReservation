package com.company.ticketreservation.service;

import com.company.ticketreservation.model.AuditoriumMaster;
import com.company.ticketreservation.model.DataConfig;
import com.company.ticketreservation.model.UserReservation;

import java.util.List;
import java.util.Map;

/**
 * This class acts as a service layer for the Ticket reservation system.
 */
public interface TicketReservationService {

  /**
   * This method is used to get the auditorium details
   * @return Map
   */
  public List<AuditoriumMaster> getAuditoriumDetails();

  /**
   * This method is used to get the data config required for the application
   * @return
   */
  public List<DataConfig> getConfigDetails();
  /**
   * The number of seats in the requested level that are neither held nor reserved
   *
   * @param venueLevel a numeric venue level identifier to limit the search
   * @return the number of tickets available on the provided level
   */
  public int numSeatsAvailable(Integer venueLevel);

  /**
   * Commit seats held for a specific customer
   *
   * @param seatHoldId the seat hold identifier
   * @return a reservation confirmation code
   */
  public void reserveSeats(int seatHoldId);

  /**
   * Find and hold the best available seats for a customer
   *
   * @param numSeats the number of seats to find and hold
   * @param minLevel the minimum venue level
   * @param maxLevel the maximum venue level
   * @param customerEmail unique identifier for the customer
   * @return a UserReservation object identifying the specific seats and related
  information
   */
  public Map findAndHoldSeats(int numSeats, Integer minLevel,
      Integer maxLevel, String customerEmail, Map tierCost);

  /**
   * This method is used to delete the seats which were on hold due for customer confirmation
   * @param reservationId
   */
  public void deleteReservationHold(Integer reservationId);
}
