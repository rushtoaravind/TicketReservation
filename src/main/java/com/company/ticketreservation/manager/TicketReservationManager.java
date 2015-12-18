package com.company.ticketreservation.manager;

import com.company.ticketreservation.model.AuditoriumMaster;
import com.company.ticketreservation.model.DataConfig;
import com.company.ticketreservation.model.SeatRepository;
import com.company.ticketreservation.model.UserReservation;
import com.company.ticketreservation.service.TicketReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains the logical validations for Ticket Reservation System
 */
@Component("ticketManager")
public class TicketReservationManager {

  @Autowired
  TicketReservationService ticketReservationService;

  /**
   * Used to get the auditorium details to be listed to the user
   * @return List
   */
  public List<AuditoriumMaster> getAuditoriumDetails() {
    List<AuditoriumMaster> auditoriumDetailsList = ticketReservationService.getAuditoriumDetails();
    return auditoriumDetailsList;
  }

  /**
   * Used to get the DB configured values
   * @return
   */
  public List<DataConfig> getDataConfigDetails() {
    List<DataConfig> dataConfigList = ticketReservationService.getConfigDetails();
    return dataConfigList;
  }

  /**
   * Method used to check if the venue level entered is in auditorium
   *
   * @param inputVenue
   * @param venueDetails
   * @return
   */
  public boolean validateVenueLevel(String inputVenue,String venueDetails) {

    if(null!=inputVenue && null!=venueDetails &&  (venueDetails.contains(inputVenue.toString()) || "0".equalsIgnoreCase(inputVenue)))
    {
      return true;
    }
    else
      return false;
  }

  /**
   * Used to valiate the minimum value that can be entered
   *
   * @param venueLevel
   * @param maxDBVenueLevel
   * @return
   */
  public boolean validateMinimumVenueLevel(String venueLevel,int maxDBVenueLevel)
  {
    if(validateInteger(venueLevel) && Integer.valueOf(venueLevel) <= maxDBVenueLevel){
      return true;
    }
    else{
        return false;
      }
  }

  /**
   * Used to validate the maximum venue level that can be entered
   *
   * @param venueLevel
   * @param maxDBVenueLevel
   * @param minVenueLevel
   * @return
   */
  public boolean validateMaximumVenueLevel(String venueLevel,int maxDBVenueLevel,String minVenueLevel)
  {
    if("0".equals(venueLevel) || (validateInteger(venueLevel) && Integer.valueOf(venueLevel) <= maxDBVenueLevel
        && Integer.valueOf(venueLevel)> Integer.valueOf(minVenueLevel))){
      return true;
    }
    else{
      return false;
    }
  }

  /**
   * Method used to validate the user entered input seat count
   *
   * @param maxTicketValue
   * @param inputTicketCount
   * @return
   */
  public boolean validateBookingSeatCount(int maxTicketValue,String inputTicketCount){

    if(validateInteger(inputTicketCount) &&
        Integer.valueOf(inputTicketCount)>0 && Integer.valueOf(inputTicketCount) <= maxTicketValue){
      return true;
    }
    else{
      return false;
    }
  }

  /**
   * Method used to validate the customer email
   *
   * @param customerEmail
   * @return
   */
  public boolean validateCustomerEmail(String customerEmail){
   if(emailValidator(customerEmail)){
    return true;
  }else{
    return false;
  }
}

  /**
   * Calls the service to get the seats available for the given venue.
   * @param venueLevel
   * @return
   */
  public int seatsAvailable(Integer venueLevel)
  {
    int seatAvailable=ticketReservationService.numSeatsAvailable(venueLevel);
    return seatAvailable;
  }

  /**
   * Method used to display the venue details from the auditorium list
   * @param auditoriumList
   * @return
   */
  public String getVenueLevelsDetails(List<AuditoriumMaster> auditoriumList) {
    StringBuffer venueBuffer=new StringBuffer();
    if (null != auditoriumList && auditoriumList.size() > 0) {
      for (AuditoriumMaster auditoriumMaster : auditoriumList) {
        System.out.printf("%-20s%-10d%n", "" + auditoriumMaster.getTierName(), auditoriumMaster.getTierId());
        if (null == venueBuffer || venueBuffer.length() == 0) {
          venueBuffer = venueBuffer.append(auditoriumMaster.getTierId()).append(":").append(auditoriumMaster.getTierName());
        } else {
          venueBuffer = venueBuffer.append("|").append(auditoriumMaster.getTierId()).append(":").append(auditoriumMaster.getTierName());
        }
      }
    }
    return venueBuffer.toString();
  }

  /**
   * This method is used to get the reserved seat details after holding the seats
   *
   * @param reservedDetailsMap
   * @param tierName
   * @return
   */
  public String getReservedSeatDetails(Map reservedDetailsMap,Map<Integer,String> tierName) {

    StringBuffer reservedSeat=new StringBuffer();
    if (null != reservedDetailsMap) {

      List<SeatRepository> seatRepositoryList = (List) reservedDetailsMap.get("seatRepositoryList");

      if (null != seatRepositoryList && seatRepositoryList.size() > 0) {
        for (SeatRepository seatRepository : seatRepositoryList) {

          if (null != reservedSeat && reservedSeat.length() == 0) {
            reservedSeat = reservedSeat.append(tierName.get(seatRepository.getTierId())).append("-").append(seatRepository.getSeatId());
          } else {
            reservedSeat = reservedSeat.append(",").append(tierName.get(seatRepository.getTierId())).append("-").append(seatRepository.getSeatId());
          }
        }
      }

    }
    return reservedSeat.toString();
  }
  /**
   * Used to validate if the input is integer
   * @param inputValue
   * @return
   */
  public boolean validateInteger(String inputValue){
    try{
      if(Integer.valueOf(inputValue)>=0) {
        return true;
      }else{
        return false;
      }
    }
    catch(NumberFormatException exception){
      return false;
    }
  }

  /**
   * Method used to validate customer email
   * @param customerEmail
   * @return
   */
  public  boolean emailValidator(String customerEmail){
    if(null==customerEmail){
      return false;
    }else {
      Pattern pattern;
      Matcher matcher;
      final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
      pattern = Pattern.compile(EMAIL_PATTERN);
      matcher = pattern.matcher(customerEmail);
      return matcher.matches();
    }
  }
}