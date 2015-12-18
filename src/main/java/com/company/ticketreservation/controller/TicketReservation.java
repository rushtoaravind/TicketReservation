package com.company.ticketreservation.controller;

import com.company.ticketreservation.manager.TicketReservationManager;
import com.company.ticketreservation.model.AuditoriumMaster;
import com.company.ticketreservation.model.DataConfig;
import com.company.ticketreservation.model.SeatRepository;
import com.company.ticketreservation.model.UserReservation;
import com.company.ticketreservation.service.TicketReservationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is a ticket reservation system for the auditorium
 */

public class TicketReservation {

  TicketReservationManager ticketReservationManager=null;
  TicketReservationService ticketReservationService=null;

  Scanner inputScanner=null;
  List<AuditoriumMaster> auditoriumDetailsList=null;
  List<DataConfig> dataConfigList=null;
  Map<Integer,Double> tierCost=new HashMap();
  Map<Integer,String> tierName=new HashMap();

  String venueDetails=null;
  int holdTime=0;
  int maxTicketCount=0;
  int maxDBVenueLevel=0;

 /**
   *
   * @param args
   */
  public static void main(String[] args){
    //start the application
    new TicketReservation().loadApplication();
  }

  /**
   *
   */
  public void loadApplication() {

    //load the required beans
    ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:reservation-config.xml");
    ticketReservationManager=(TicketReservationManager)applicationContext.getBean("ticketManager");
    ticketReservationService=(TicketReservationService)applicationContext.getBean("ticketReservationService");
    try {
         loadDetails();
    }
    catch(Exception exception ){
      System.out.println("\nError: Unexpected Error. Please start your reservation again");
      loadDetails();
    }
  }

  /**
   *
   */
  private void loadDetails() {

    System.out.println("\nWelcome to the Ticket Reservation system");
    auditoriumDetailsList=getAuditoriumDetails(tierCost,tierName);
    dataConfigList=ticketReservationManager.getDataConfigDetails();

    //set the DB configured date into class level variables
    for(DataConfig dataConfig:dataConfigList){
      if("RESERVATION_TIMEOUT".equalsIgnoreCase(dataConfig.getDataText())){
        holdTime=Integer.valueOf(dataConfig.getDataValue());
      }
      else{
        maxTicketCount=Integer.valueOf(dataConfig.getDataValue());
      }
    }
    startBooking();
  }
  /**
   * This method is used to list the venue level details
   *
   * @param auditoriumList
   * @return
   */
  private void displayVenueLevelsInConsole(List<AuditoriumMaster> auditoriumList) {
    System.out.println ("\n----------------------------------------");
    System.out.println ("Seat Availability - Auditorium Details");
    System.out.println ("----------------------------------------");
    System.out.printf("%-20s%-10s%n", "Venue", "Venue Level");

  }


  /**
   * This method is used to list the auditorium details
   *
   * @return
   */
  private List<AuditoriumMaster> getAuditoriumDetails(Map tierCost,Map tierName){

    List<AuditoriumMaster> auditoriumDetailsList=null;
    auditoriumDetailsList= ticketReservationManager.getAuditoriumDetails();

    System.out.println ("\n----------------------------------------");
    System.out.println ("Ticket Reservation - Auditorium Details");
    System.out.println ("----------------------------------------");
    System.out.printf("%-20s%-20s%%n", "Venue Name", "Fees");

    //iterate through the auditorium details from DB and list it to user
    //Form maps to add the cost of each venue and the name of each venue for display purpose when booking the tickets.
    if (null!=auditoriumDetailsList && auditoriumDetailsList.size()>0){
      for(AuditoriumMaster auditoriumMaster:auditoriumDetailsList){
        System.out.printf("%-20s%-20.0f%n", "" + auditoriumMaster.getTierName(), auditoriumMaster.getTierFees());
        tierCost.put(auditoriumMaster.getTierId(),auditoriumMaster.getTierFees());
        tierName.put(auditoriumMaster.getTierId(),auditoriumMaster.getTierName());
        //set the maximum venue level for validation
        maxDBVenueLevel=auditoriumMaster.getTierId();
      }
    }
    return auditoriumDetailsList;
  }
  /**
   * Starts the booking for the user
   */
  private void startBooking(){

    String inputOption=null;
    boolean venueLevelValidation;
    int seatsAvailable=0;

    System.out.print("\nPlease enter 1 to display the venue levels for booking or 0 to exit:");

    inputScanner = new Scanner(System.in);
    inputOption = inputScanner.nextLine().trim();
    if (ticketReservationManager.validateInteger(inputOption)) {
      if ("1".equals(inputOption)) {
        displayVenueLevelsInConsole(auditoriumDetailsList);
        venueDetails=ticketReservationManager.getVenueLevelsDetails(auditoriumDetailsList);
        validateVenue(venueDetails);
      }
      else if ("0".equals(inputOption)) {
        System.out.println("\nThanks for visiting.");
        System.out.println("-------------------------------------------------------------------------------");
        System.exit(0);
      }else
      {
        System.out.println("\nError: Invalid Option.");
        System.out.println("-------------------------------------------------------------------------------");
        startBooking();
      }
    }else{
      System.out.println("\nError: Invalid Option.");
      System.out.println("-------------------------------------------------------------------------------");
      startBooking();
    }
  }

  /**
   * Used to validate if the venue level entered is valid.
   *
   * @param venueDetails
   */
  private void validateVenue(String venueDetails) {
    String venueLevel=null;
    boolean venueLevelValidation;
    System.out.print("\nPlease enter the venue level to know the seats available or 0 to know the complete availability:");
    venueLevel=inputScanner.nextLine().trim();

    venueLevelValidation=ticketReservationManager.validateVenueLevel(venueLevel, venueDetails);
    if(venueLevelValidation){
      checkAvailability(venueLevel);
    }else{
      System.out.println("\nError: Invalid option.");
      System.out.println("-------------------------------------------------------------------------------");
      validateVenue(venueDetails);
    }
  }

  /**
   * Used to check the availability of seats in a given venue level
   * @param venueLevel
   */
  private void checkAvailability(String venueLevel) {
    int seatsAvailable=0;
    boolean validTicketCount=false;

    seatsAvailable=ticketReservationManager.seatsAvailable(Integer.valueOf(venueLevel));

    //if no seats are available prompt the user to try different selection
    if(seatsAvailable>0){
      System.out.println("\nSeats available for Venue Level " + venueLevel + " :" + seatsAvailable);
      validateTicketCountAndProceed();
    }
    else{
      System.out.println("\nSorry currently no seats are available for your current selection. Please try different selection.");
      validateVenue(venueDetails);
      checkAvailability(venueLevel);
    }
  }

  /**
   * used to validate the ticket count used wants to book and proceeds further.
   */
  private void validateTicketCountAndProceed() {
    boolean validTicketCount;
    String inputTickets=null;
    System.out.print("\nPlease enter the number of tickets you wish to book:");
    inputTickets= inputScanner.nextLine().trim();

    validTicketCount=ticketReservationManager.validateBookingSeatCount(maxTicketCount, inputTickets);
    if(validTicketCount)
    {
      getMinimumVenueLevel(inputTickets);
    }else{
      System.out.println("\nYou can book a maximum of " + maxTicketCount + " tickets. Please enter value from 1 to " + maxTicketCount);
      validateTicketCountAndProceed();
    }
  }

  /**
   * Used to get the minimum venue level and proceed with booking
   *
   * @param inputTickets
   */
  private void getMinimumVenueLevel(String inputTickets) {
    boolean isMinimumLevelValid;
    String minimumVenueLevel=null;
    System.out.print("\nPlease enter the minimum venue level. Press 0 to book in any venue:");
    minimumVenueLevel = inputScanner.nextLine().trim();
    isMinimumLevelValid=ticketReservationManager.validateMinimumVenueLevel(minimumVenueLevel, maxDBVenueLevel);
    if(isMinimumLevelValid){
      if("0".equals(minimumVenueLevel)){
        getCustomerEmail(inputTickets,"0","0");
      }else{
        getMaximumVenueLevel(inputTickets,minimumVenueLevel);
      }
    }else{
      System.out.println("\nError: Invalid Option. Please enter the minimum venue level again.");
      getMinimumVenueLevel(inputTickets);
    }
  }

  /**
   * Used to get the maximum venue level and proceed further with booking
   *
   * @param inputTickets
   * @param minimumVenueLevel
   */
  private void getMaximumVenueLevel(String inputTickets,String minimumVenueLevel){
    String maximumVenueLevel=null;
    boolean isMaxLevelValid=false;
    System.out.print("\nPlease enter the maximum venue level. Press 0 to skip the maximum venue level:");
    maximumVenueLevel = inputScanner.nextLine().trim();
    isMaxLevelValid=ticketReservationManager.validateMaximumVenueLevel(maximumVenueLevel, maxDBVenueLevel, minimumVenueLevel);
    if(isMaxLevelValid)
    {
      getCustomerEmail(inputTickets,minimumVenueLevel,maximumVenueLevel);

    }else{
      System.out.println("\nError: Invalid Option. Please enter the maximum venue level again.");
      getMaximumVenueLevel(inputTickets, minimumVenueLevel);
      System.out.print("");
    }
  }

  /**
   * Used to get the customer email, validate and proceed further.
   *
   * @param inputTickets
   * @param minimumVenueLevel
   * @param maximumVenueLevel
   */
  private void getCustomerEmail(String inputTickets,String minimumVenueLevel,String maximumVenueLevel){
    String customerEmail=null;
    boolean isCustomerEmailValid=false;
    Map reservedMapDetails=null;
    boolean isTicketsAvailable=false;
    String reservedSeatDetails=null;

    System.out.print("\nPlease enter the user email address:");
    customerEmail = inputScanner.nextLine().trim();
    isCustomerEmailValid=ticketReservationManager.validateCustomerEmail(customerEmail);
    if(isCustomerEmailValid)
    {
      reservedMapDetails = findAndHoldSeats(Integer.valueOf(inputTickets), Integer.valueOf(minimumVenueLevel), Integer.valueOf(maximumVenueLevel), customerEmail, tierCost);
      isTicketsAvailable=validateIfTicketsAreAvailable(reservedMapDetails);
      if(isTicketsAvailable){
        reservedSeatDetails=ticketReservationManager.getReservedSeatDetails(reservedMapDetails,tierName);
        System.out.println("Your reserved Seat Numbers: " + reservedSeatDetails + " Total cost- $" + reservedMapDetails.get("reservationCost"));
        UserReservation userReservation = (UserReservation) reservedMapDetails.get("userReservation");
        int reservationId = userReservation.getReservationId();
        if(reservationId!=0) {
          confirmTicketReservation(reservationId, reservedMapDetails, reservedSeatDetails);
        }else{
          System.out.println("\nError occurred.");
          System.out.println("-------------------------------------------------------------------------------");
          loadDetails();
        }

      }else{
        System.out.println("\nSorry no seats are available for your current selection. Please check the availability before booking.");
        validateVenue(venueDetails);
      }
    }
    else{
      System.out.println("\nError: Invalid email address.");
      System.out.println("-------------------------------------------------------------------------------");
      getCustomerEmail(inputTickets, minimumVenueLevel, maximumVenueLevel);
    }
  }

  /**
   * This method is used to hold the seats for the given user details
   * @param numSeats
   * @param minVenueLevel
   * @param maxVenueLevel
   * @param customerEmail
   * @param tierCost
   * @return
   */
  private  Map findAndHoldSeats(int numSeats,Integer minVenueLevel,Integer maxVenueLevel,String customerEmail,Map tierCost){
    Map reservedData=null;
    reservedData=ticketReservationService.findAndHoldSeats(numSeats,minVenueLevel,maxVenueLevel,customerEmail,tierCost);
    return reservedData;
  }

  /**
   * Used to check if the tickets are available
   *
   * @param reservedMapDetails
   * @return
   */
 private boolean validateIfTicketsAreAvailable(Map reservedMapDetails)
 {
  List<SeatRepository> seatRepositoryList = (List) reservedMapDetails.get("seatRepositoryList");

  if(null!=seatRepositoryList && seatRepositoryList.size()>0){
     return true;
   }
  else {
    return false;
  }
 }

  /**
   * Method used to finalize the ticket bookings
   *
   * @param reservationId
   * @param reservedMapDetails
   * @param reservedSeat
   */
 private void confirmTicketReservation(int reservationId,Map reservedMapDetails,String reservedSeat){
    String confirmationInput=null;
    int reserveHoldTime = holdTime /1000;
    System.out.print("\nPress 1 to confirm the booking or any other key to cancel booking.You have " + reserveHoldTime/60 + " minute(s) to confirm your reservation:");

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    long startTime = System.currentTimeMillis();
    try {

      while ((System.currentTimeMillis() - startTime) < reserveHoldTime * 1000 && !in.ready()) {
      }

    //Tickets finalized
    if (in.ready()) {
        confirmationInput = in.readLine().trim();
        if ("1".equals(confirmationInput)) {
          System.out.println("\nYour tickets are being finalized.Please wait.....");
          ticketReservationService.reserveSeats(reservationId);
          System.out.println("\nThanks for booking the tickets. Please find the reservation details below");
          System.out.println("Reservation Id : "  + reservationId);
          System.out.println("Reserved Seat Number(s): " + reservedSeat);
          System.out.println("Total cost:$ "  + reservedMapDetails.get("reservationCost"));
          System.out.println("-------------------------------------------------------------------------------");
          loadDetails();
        } else {
          ticketReservationService.deleteReservationHold(reservationId);
          System.out.println("\nTickets cancelled. Thanks for visiting Ticket reservation system.");
          System.out.println("-------------------------------------------------------------------------------");
          loadDetails();
        }
      }
    else{
      ticketReservationService.deleteReservationHold(reservationId);
      System.out.println("\nTime exceeded for reservation. Your tickets have been cancelled. Please start your booking again.");
      System.out.println("-------------------------------------------------------------------------------");
      loadDetails();
    }
    }
    catch (Exception exception) {
      ticketReservationService.deleteReservationHold(reservationId);
      System.out.println("\nError occurred.");
      System.out.println("-------------------------------------------------------------------------------");
      loadDetails();
    }
  }

}
