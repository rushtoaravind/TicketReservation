package com.company.ticketreservation.service;


import com.company.ticketreservation.dao.TicketReserveDAO;
import com.company.ticketreservation.model.AuditoriumMaster;
import com.company.ticketreservation.model.DataConfig;
import com.company.ticketreservation.model.UserReservation;
import com.company.ticketreservation.service.TicketReservationService;
import com.sun.xml.internal.rngom.digested.DListPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Created by a1096282 on 12/9/15.
 */
@Service("ticketReservationService")
public class TicketReservationServiceImpl implements TicketReservationService {

  @Autowired
  TicketReserveDAO ticketReserveDAO;

  /**
   * This method is used to get the auditorium details
   * @return Map
   */
  public List<AuditoriumMaster> getAuditoriumDetails(){
    List<AuditoriumMaster> auditoriumList=null;
    auditoriumList=ticketReserveDAO.getAuditoriumDetails();
    return auditoriumList;
  }

  /**
   * This method is used to get the data config required for the application
   * @return
   */
  public List<DataConfig> getConfigDetails(){
    List<DataConfig> dataConfigList=null;
    dataConfigList=ticketReserveDAO.getConfigDetails();
    return dataConfigList;
  }
  /**
   *This method is used to find the number of seats available in a venue level
   * @param venueLevel a numeric venue level identifier to limit the search
   * @return int
   */
  public int numSeatsAvailable(Integer venueLevel){
    return ticketReserveDAO.getSeatsAvailable(venueLevel);
  }


  /**
   * Find and hold the best available seats for a customer
   *
   * @param numSeats the number of seats to find and hold
   * @param minLevel the minimum venue level
   * @param maxLevel the maximum venue level
   * @param customerEmail unique identifier for the customer
   * @return Map identifying the specific seats and related
  information
   */
  public Map findAndHoldSeats(int numSeats, Integer minLevel,
      Integer maxLevel, String customerEmail,Map tierCost){
    Map reservedData=null;
    reservedData=ticketReserveDAO.findAndHoldSeats(numSeats,minLevel,maxLevel,customerEmail,tierCost);
    return reservedData;
  }

  /**
   * Commit seats held for a specific customer
   *
   * @param seatHoldId the seat hold identifier
   * @return a reservation confirmation code
   */
  public void reserveSeats(int seatHoldId){
    ticketReserveDAO.reserveSeats(seatHoldId);
  }

  /**
   * This method is used to delete the seats which were on hold due for customer confirmation
   * @param reservationId
   */
  public void deleteReservationHold(Integer reservationId){

    ticketReserveDAO.deleteReservationHold(reservationId);

  }
}
