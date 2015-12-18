/*
package com.company.ticketreservation.controller;

import com.company.ticketreservation.model.AuditoriumMaster;
import com.company.ticketreservation.model.DataConfig;
import com.company.ticketreservation.model.SeatRepository;
import com.company.ticketreservation.model.UserReservation;
import com.company.ticketreservation.service.TicketReservationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

*//*

*/
/**
 * This class is a ticket reservation system for the auditorium
 *//*
*/
/*

public class TicketReservationCopy {

  List<AuditoriumMaster> auditoriumList=null;
  int holdTime=0;
  int maxTicketCount=0;
  int maxDBVenueLevel=0;
  StringBuffer venueBuffer=null;

  Map<Integer,Double> tierCost=new HashMap();
  Map<Integer,String> tierName=new HashMap();

  Timer timer = new Timer();
  Scanner inputScanner=null;

  public static void main(String[] args){

    ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:reservation-config.xml");

   // applicationContext=new ClassPathXmlApplicationContext("classpath:reservation-config.xml");
    TicketReservationService ticketReservationService=(TicketReservationService)applicationContext.getBean("ticketReservationService");
    //start the application
    new TicketReservationCopy().loadStartupDetails();

  }

  public void loadStartupDetails() {

    Map auditoriumDetailsMap=null;
    List<DataConfig> dataConfigList=null;
    String seatAvailabilityOption=null;

    inputScanner = new Scanner(System.in);


    auditoriumDetailsMap=getAuditoriumDetails(tierCost,tierName);
    auditoriumList=(List)auditoriumDetailsMap.get("auditoriumList");
    dataConfigList=(List)auditoriumDetailsMap.get("dataConfigList");

    for(DataConfig dataConfig:dataConfigList){
      if("RESERVATION_TIMEOUT".equalsIgnoreCase(dataConfig.getDataText())){
        holdTime=Integer.valueOf(dataConfig.getDataValue());
      }
      else{
        maxTicketCount=Integer.valueOf(dataConfig.getDataValue());
      }
    }
    //start the application after loading the details
    startApplication(auditoriumList, tierCost, tierName);
  }

  *//*

*/
/**
   * This method is used to start the ticket reservation application
   *
   * @param auditoriumList
   * @param tierCost
   * @param tierName
   *//*
*/
/*

  private void startApplication(List<AuditoriumMaster> auditoriumList,Map<Integer, Double> tierCost, Map<Integer, String> tierName) {

    String inputOption=null;
    System.out.println("\nWelcome to the Ticket Reservation system");
    System.out.println("\nPlease select the option:");
    System.out.print("1)Number of seats available 2)Booking :");
    try {
      inputOption = inputScanner.nextLine().trim();
      if (validateInteger(inputOption)) {
        if ("1".equals(inputOption)) {
          checkSeatAvailability(auditoriumList);
        } else if ("2".equals(inputOption)) {
          displayVenueLevels(auditoriumList, venueBuffer);
          bookTickets(tierCost, tierName);
        } else {
          System.out.println("\nError: Invalid Option. Please enter either 1 or 2 to proceed further");
          System.out.println("-------------------------------------------------------------------------------");
          //clearStaticValues();
          startApplication(auditoriumList, tierCost, tierName);
        }
      } else {
        System.out.println("\nError: Invalid Option. Please enter either 1 or 2 to proceed further");
        System.out.println("-------------------------------------------------------------------------------");
        //clearStaticValues();
        startApplication(auditoriumList, tierCost, tierName);
      }
    }
    catch(BufferOverflowException exception ){
       CharBuffer charBuf = CharBuffer.allocate(100);
      charBuf.clear();
      exception.printStackTrace();;
      System.out.println("\nError: Unexpected Error. Please start your reservation again");
      startApplication(auditoriumList, tierCost, tierName);
    }
    catch(Exception exception ){
      exception.printStackTrace();;
      System.out.println("\nError: Unexpected Error. Please start your reservation again");
      //startApplication(auditoriumList, tierCost, tierName);
    }
  }

  *//*

*/
/**
   *This method is used to hold and book the tickets.
   * @param tierCost
   * @param tierName
   *
   *//*
*/
/*

  private void bookTickets(Map<Integer, Double> tierCost, Map<Integer, String> tierName) {

    String inputOption=null;
    Map reservedMap=null;
    String numberOfSeats=null;
    String minimumVenueLevel=null;
    String maximumVenueLevel=null;
    String userName=null;
    String customerEmail=null;


    System.out.print("\nPlease enter the number of tickets you wish to book: ");
    numberOfSeats= inputScanner.nextLine().trim();

    if(validateInteger(numberOfSeats) && Integer.valueOf(numberOfSeats)>0) {
      if (Integer.valueOf(numberOfSeats) <= Integer.valueOf(maxTicketCount)) {
        holdTicketsForBooking(tierCost, tierName, numberOfSeats);
      } else {
        System.out.println("You can book a maximum of " + maxTicketCount + " tickets.");
        bookTickets(tierCost, tierName);
      }
    }
    else
    {
      System.out.println("Error: Invalid option.");
      System.out.println("-------------------------------------------------------------------------------");
      bookTickets(tierCost, tierName);
    }
  }

  *//*

*/
/**
   * Method used to hold the tickets for booking
   * @param tierCost
   * @param tierName
   * @param numberOfSeats
   *//*
*/
/*

  private void holdTicketsForBooking(Map<Integer, Double> tierCost, Map<Integer, String> tierName, String numberOfSeats) {

    String minimumVenueLevel=null;
    String maximumVenueLevel=null;
    String customerEmail=null;
    String inputOption=null;
    Map reservedMap=null;
    int reserveHoldTime=0;

    System.out.print("\nPlease enter the minimum venue level. Press 0 to book in any venue: ");
    minimumVenueLevel = inputScanner.nextLine().trim();

    if(validateInteger(minimumVenueLevel)) {
      if (Integer.valueOf(minimumVenueLevel) > maxDBVenueLevel) {
        System.out.print("\nError: Please enter venue level from 1 to " + maxDBVenueLevel + ". Please start the booking again: ");
        System.out.println("-------------------------------------------------------------------------------");
        holdTicketsForBooking(tierCost, tierName, numberOfSeats);
      } else {
        //if minimum venue level is entered as 0 maximum venue level will be skipped
        if(Integer.valueOf(minimumVenueLevel)==0){
          maximumVenueLevel="0";
        }else {
          System.out.print("\nPlease enter the maximum venue level. Press 0 to skip the maximum venue level: ");
          maximumVenueLevel = inputScanner.nextLine().trim();
        }
        if (Integer.valueOf(maximumVenueLevel)==0 || (validateInteger(maximumVenueLevel) && maxVenueValidation(minimumVenueLevel,maximumVenueLevel))) {

          System.out.print("\nPlease enter the user email address: ");
          customerEmail = inputScanner.nextLine().trim();

          if (null != customerEmail && emailValidator(customerEmail)) {
            reservedMap = findAndHoldSeats(Integer.valueOf(numberOfSeats), Integer.valueOf(minimumVenueLevel), Integer.valueOf(maximumVenueLevel), customerEmail, tierCost);

            List<SeatRepository> seatRepositoryList = (List) reservedMap.get("seatRepositoryList");

            //if seatRepository list size is 0 no seats are available
            if (null!=seatRepositoryList && seatRepositoryList.size()>0){

            UserReservation userReservation = (UserReservation) reservedMap.get("userReservation");
            int reservationId = userReservation.getReservationId();
            StringBuffer reservedSeat = new StringBuffer();
            if (null != seatRepositoryList && seatRepositoryList.size() > 0) {
              for (SeatRepository seatRepository : seatRepositoryList) {

                if (null != reservedSeat && reservedSeat.length() == 0) {
                  reservedSeat = reservedSeat.append(tierName.get(seatRepository.getTierId())).append("-").append(seatRepository.getSeatId());
                } else {
                  reservedSeat = reservedSeat.append(",").append(tierName.get(seatRepository.getTierId())).append("-").append(seatRepository.getSeatId());
                }
              }
            }
            System.out.println("Your reserved Seat Numbers: " + reservedSeat + " Total cost- $" + reservedMap.get("reservationCost"));
            reserveHoldTime = holdTime / (60 * 1000);
            System.out.print("\nPress 1 to confirm the booking or any other key to cancel booking.You have " + reserveHoldTime + " minute(s) to confirm your reservation: ");

            //schedule the timer
            timer.schedule(new ReservationTimer(reservationId), holdTime);

            //Tickets finalized
            if (timer != null) {
              try {
                inputOption = inputScanner.nextLine().trim();
                if ("1".equals(inputOption)) {
                  System.out.println("\nYour tickets are being finalized.Please wait.....");
                  timer.cancel();
                  ticketReservationService.reserveSeats(reservationId);
                  System.out.println("\nThanks for booking the tickets. Please find the reservation details below");
                  System.out.println("Reserved Seat Numbers- " + reservedSeat + " Total cost: $" + reservedMap.get("reservationCost"));
                  System.out.println("Reservation amount- " + reservedMap.get("reservationCost"));
                  System.out.println("-------------------------------------------------------------------------------");
                  //timer=null;
                  timer = new Timer();
                  //clearStaticValues();
                  startApplication(auditoriumList, tierCost, tierName);
                } else {
                  ticketReservationService.deleteReservationHold(reservationId);
                  System.out.println("\nTickets cancelled. Thanks for visiting Ticket reservation system.");
                  System.out.println("-------------------------------------------------------------------------------");
                  timer.cancel();
                  timer = new Timer();
                  //clearStaticValues();
                  startApplication(auditoriumList, tierCost, tierName);
                }
              } catch (Exception exception) {
                exception.printStackTrace();
                ticketReservationService.deleteReservationHold(reservationId);
                System.out.println("\nTickets cancelled. Thanks for visiting Ticket reservation system.");
                System.out.println("-------------------------------------------------------------------------------");
                timer.cancel();
                timer = new Timer();
                //clearStaticValues();
                startApplication(auditoriumList, tierCost, tierName);
              }
            }
          }
          else {
              System.out.println("\nSorry no seats are available for your current selection. Please check the availability before booking.");
              checkSeatAvailability(auditoriumList);
          }
        }else {
            System.out.println("\nError: Please enter a valid email address. Please start the booking again.");
            System.out.println("-------------------------------------------------------------------------------");
            holdTicketsForBooking(tierCost, tierName, numberOfSeats);
        }
      }
        else{
          System.out.println("\nError: Please enter venue level from 1 to " + maxDBVenueLevel + ". Please start the booking again.");
          System.out.println("-------------------------------------------------------------------------------");
          holdTicketsForBooking(tierCost, tierName, numberOfSeats);
        }

      }
    }
    else{
      System.out.println("\nError :Please enter venue level from 1 to " + maxDBVenueLevel + ". Please start the booking again.");
      System.out.println("-------------------------------------------------------------------------------");
      holdTicketsForBooking(tierCost, tierName, numberOfSeats);
    }
  }

  *//*

*/
/**
   * Method used to validate the maximum venue
   *
   * @param minVenueLevel
   * @param maximumVenueLevel
   * @return boolean
   *//*
*/
/*

   boolean maxVenueValidation(String minVenueLevel,String maximumVenueLevel){
    if(Integer.valueOf(maximumVenueLevel)>Integer.valueOf(minVenueLevel) && Integer.valueOf(maximumVenueLevel)<=maxDBVenueLevel)
    {
      return true;
    }
    else
      return false;
  }
  *//*

*/
/**
   * This method is used to find the seat availability for the given venue level
   *
   * @param auditoriumList
   *//*
*/
/*

  private void checkSeatAvailability(List<AuditoriumMaster> auditoriumList) {

    String seatAvailabilityOption=null;
    StringBuffer venueBuffer=null;

    venueBuffer = displayVenueLevels(auditoriumList, venueBuffer);

    System.out.print("\nPlease enter the venue level to know the seats available or 0 to know the complete availability:");
    seatAvailabilityOption=inputScanner.nextLine().trim();

    if(null!=seatAvailabilityOption && null!=venueBuffer &&  (venueBuffer.toString().contains(seatAvailabilityOption.toString()) || "0".equalsIgnoreCase(seatAvailabilityOption)))
    {
      if("0".equalsIgnoreCase(seatAvailabilityOption)){
        getSeatsAvailable(0);
      }
      else{
        getSeatsAvailable(new Integer(seatAvailabilityOption));
      }

    }
    else{
      System.out.println("\nError: Invalid option. Please select the options again.");
      System.out.println("-------------------------------------------------------------------------------");
      checkSeatAvailability(auditoriumList);
    }
  }

  *//*

*/
/**
   * This method is used to list the venue level details
   *
   * @param auditoriumList
   * @param venueBuffer
   * @return
   *//*
*/
/*

  private StringBuffer displayVenueLevels(List<AuditoriumMaster> auditoriumList, StringBuffer venueBuffer) {
    System.out.println ("\n----------------------------------------");
    System.out.println ("Seat Availability - Auditorium Details");
    System.out.println ("----------------------------------------");
    System.out.printf("%-20s%-10s%n", "Venue", "Venue Level");

    if (null!=auditoriumList && auditoriumList.size()>0){
      venueBuffer=new StringBuffer();
      for(AuditoriumMaster auditoriumMaster:auditoriumList){
        System.out.printf("%-20s%-10d%n", "" + auditoriumMaster.getTierName(), auditoriumMaster.getTierId());
        if(null==venueBuffer || venueBuffer.length()==0){
          venueBuffer=venueBuffer.append(auditoriumMaster.getTierId()).append(":").append(auditoriumMaster.getTierName());
        }
        else{
          venueBuffer=venueBuffer.append("|").append(auditoriumMaster.getTierId()).append(":").append(auditoriumMaster.getTierName());
        }
      }
    }
    return venueBuffer;
  }

  *//*

*/
/**
   * This method is used to hold the seats for the given user details
   * @param numSeats
   * @param minVenueLevel
   * @param maxVenueLevel
   * @param customerEmail
   * @param tierCost
   * @return
   *//*
*/
/*

  private  Map findAndHoldSeats(int numSeats,Integer minVenueLevel,Integer maxVenueLevel,String customerEmail,Map tierCost){
    Map reservedData=null;
    reservedData=ticketReservationService.findAndHoldSeats(numSeats,minVenueLevel,maxVenueLevel,customerEmail,tierCost);
    return reservedData;
  }
  *//*

*/
/**
   * This method is used to list the auditorium details
   *
   * @return
   *//*
*/
/*

  private Map getAuditoriumDetails(Map tierCost,Map tierName){

    Map auditoriumDetailsMap=null;
    auditoriumDetailsMap= ticketReservationService.getAuditoriumDetails();
    auditoriumList=(List)auditoriumDetailsMap.get("auditoriumList");

    System.out.println ("\n----------------------------------------");
    System.out.println ("Ticket Reservation - Auditorium Details");
    System.out.println ("----------------------------------------");
    System.out.printf("%-20s%-10s%-10s%n", "Venue Name", "Fees", "Capacity");


    if (null!=auditoriumList && auditoriumList.size()>0){
      for(AuditoriumMaster auditoriumMaster:auditoriumList){
        System.out.printf("%-20s%-10.0f%-10d%n", "" + auditoriumMaster.getTierName(), auditoriumMaster.getTierFees(), auditoriumMaster.getTierTotalCapacity());
        tierCost.put(auditoriumMaster.getTierId(),auditoriumMaster.getTierFees());
        tierName.put(auditoriumMaster.getTierId(),auditoriumMaster.getTierName());
        //set the maximum venue level for validation
        maxDBVenueLevel=auditoriumMaster.getTierId();
      }
    }
   return auditoriumDetailsMap;
  }

  *//*

*/
/**
   * This method is used to get the seats available for the venue.
   * @param venueLevel
   *//*
*/
/*

  private void getSeatsAvailable(Integer venueLevel){
    int seatAvailable=ticketReservationService.numSeatsAvailable(venueLevel);
    if(seatAvailable>0) {
      System.out.println("\nSeats available for Venue Level " + venueLevel + " :" + ticketReservationService.numSeatsAvailable(venueLevel));
      bookTickets(tierCost, tierName);
    }
    else{
      System.out.println("\nSorry currently no seats are available for your current selection. Please try different selection.");
      checkSeatAvailability(auditoriumList);
    }
  }

  *//*

*/
/**
   * Timer class to release the seat after set time
   *//*
*/
/*

   class ReservationTimer extends TimerTask {
    int reservationId;

    public ReservationTimer(){}
    public ReservationTimer(int reservationId)
    {
      this.reservationId = reservationId;
    }
    @Override
    public void run() {
      timer = null;
      ticketReservationService.deleteReservationHold(reservationId);
      System.out.println("\nTime exceeded for reservation. Please start the booking again.");
      timer = new Timer();
      //clearStaticValues();
      startApplication(auditoriumList,tierCost,tierName);
    }
  }

  *//*

*/
/**
   * Used to validate if the input is integer
   * @param inputValue
   * @return
   *//*
*/
/*

  public boolean validateInteger(String inputValue){
    try{
        Integer.valueOf(inputValue);
        return true;
    }
    catch(NumberFormatException exception){
      return false;
    }
  }

  *//*

*/
/**
   * Method used to validate customer email
   * @param customerEmail
   * @return
   *//*
*/
/*

  public  boolean emailValidator(String customerEmail){
     Pattern pattern;
     Matcher matcher;
     final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
     pattern = Pattern.compile(EMAIL_PATTERN);
    matcher = pattern.matcher(customerEmail);
     return matcher.matches();
  }

}
*//*

package com.company.ticketreservation.controller;

import com.company.ticketreservation.manager.TicketReservationManager;
import com.company.ticketreservation.model.AuditoriumMaster;
import com.company.ticketreservation.model.DataConfig;
import com.company.ticketreservation.model.SeatRepository;
import com.company.ticketreservation.model.UserReservation;
import com.company.ticketreservation.service.TicketReservationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

*/
/**
 * This class is a ticket reservation system for the auditorium
 *//*

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

  Timer timer = new Timer();

  */
/**
   *
   * @param args
   *//*

  public static void main(String[] args){
    //start the application
    new TicketReservation().loadApplication();
  }

  */
/**
   *
   *//*

  public void loadApplication() {

    ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:reservation-config.xml");
    ticketReservationManager=(TicketReservationManager)applicationContext.getBean("ticketManager");
    ticketReservationService=(TicketReservationService)applicationContext.getBean("ticketReservationService");
    try {
      loadDetails();
    }
    catch(BufferOverflowException exception ){
      CharBuffer charBuf = CharBuffer.allocate(100);
      charBuf.clear();
      exception.printStackTrace();;
      System.out.println("\nError: Unexpected Error. Please start your reservation again");
      loadDetails();
    }
  }

  */
/**
   *
   *//*

  private void loadDetails() {

    boolean startBookingValid=false;
    System.out.println("\nWelcome to the Ticket Reservation system");
    auditoriumDetailsList=getAuditoriumDetails(tierCost,tierName);

    dataConfigList=ticketReservationManager.getDataConfigDetails();

    for(DataConfig dataConfig:dataConfigList){
      if("RESERVATION_TIMEOUT".equalsIgnoreCase(dataConfig.getDataText())){
        holdTime=Integer.valueOf(dataConfig.getDataValue());
      }
      else{
        maxTicketCount=Integer.valueOf(dataConfig.getDataValue());
      }
    }
    // Start Booking start
    String inputOption=null;
    int seatsAvailable=0;

    System.out.print("\nPlease enter 1 to display the venue levels for booking:\t");

    inputScanner = new Scanner(System.in);
    inputOption = inputScanner.nextLine().trim();
    startBookingValid=ticketReservationManager.validateStartBooking(inputOption, auditoriumDetailsList, venueDetails);

    if(startBookingValid){
      System.out.print("\nPlease enter the venue level to know the seats available or 0 to know the complete availability:\t");
      String venueLevel=null;
      boolean venueLevelValidation;
      venueLevel=inputScanner.nextLine().trim();
      venueLevelValidation=ticketReservationManager.validateVenueLevel(venueLevel, venueDetails);

    }else{

    }
  }

  */
/**
   * This method is used to list the auditorium details
   *
   * @return
   *//*

  private List<AuditoriumMaster> getAuditoriumDetails(Map tierCost,Map tierName){

    List<AuditoriumMaster> auditoriumDetailsList=null;
    auditoriumDetailsList= ticketReservationManager.getAuditoriumDetails();

    System.out.println ("\n----------------------------------------");
    System.out.println ("Ticket Reservation - Auditorium Details");
    System.out.println ("----------------------------------------");
    System.out.printf("%-20s%-10s%-10s%n", "Venue Name", "Fees", "Capacity");


    if (null!=auditoriumDetailsList && auditoriumDetailsList.size()>0){
      for(AuditoriumMaster auditoriumMaster:auditoriumDetailsList){
        System.out.printf("%-20s%-10.0f%-10d%n", "" + auditoriumMaster.getTierName(), auditoriumMaster.getTierFees(), auditoriumMaster.getTierTotalCapacity());
        tierCost.put(auditoriumMaster.getTierId(),auditoriumMaster.getTierFees());
        tierName.put(auditoriumMaster.getTierId(),auditoriumMaster.getTierName());
        //set the maximum venue level for validation
        maxDBVenueLevel=auditoriumMaster.getTierId();
      }
    }
    return auditoriumDetailsList;
  }
  */
/**
   *
   * @param venueDetails
   *//*

  private void validateVenue(String venueDetails) {
    String venueLevel=null;
    boolean venueLevelValidation;
    System.out.print("\nPlease enter the venue level to know the seats available or 0 to know the complete availability:\t");
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

  */
/**
   *
   * @param venueLevel
   *//*

  private void checkAvailability(String venueLevel) {
    int seatsAvailable;
    boolean validTicketCount;
    seatsAvailable=ticketReservationManager.seatsAvailable(Integer.valueOf(venueLevel));
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

  */
/**
   *
   *//*

  private void validateTicketCountAndProceed() {
    boolean validTicketCount;
    String inputTickets=null;
    System.out.print("\nPlease enter the number of tickets you wish to book:\t");
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

  */
/**
   *
   * @param inputTickets
   *//*

  private void getMinimumVenueLevel(String inputTickets) {
    boolean isMinimumLevelValid;
    String minimumVenueLevel=null;
    System.out.print("\nPlease enter the minimum venue level. Press 0 to book in any venue:\t");
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

  */
/**
   *
   * @param inputTickets
   * @param minimumVenueLevel
   *//*

  private void getMaximumVenueLevel(String inputTickets,String minimumVenueLevel){
    String maximumVenueLevel=null;
    boolean isMaxLevelValid=false;
    System.out.print("\nPlease enter the maximum venue level. Press 0 to skip the maximum venue level:\t");
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

  */
/**
   *
   * @param inputTickets
   * @param minimumVenueLevel
   * @param maximumVenueLevel
   *//*

  private void getCustomerEmail(String inputTickets,String minimumVenueLevel,String maximumVenueLevel){
    String customerEmail=null;
    boolean isCustomerEmailValid=false;
    Map reservedMapDetails=null;
    boolean isTicketsAvailable=false;
    String reservedSeatDetails=null;

    System.out.print("\nPlease enter the user email address:\t");
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

  */
/**
   * This method is used to hold the seats for the given user details
   * @param numSeats
   * @param minVenueLevel
   * @param maxVenueLevel
   * @param customerEmail
   * @param tierCost
   * @return
   *//*

  private  Map findAndHoldSeats(int numSeats,Integer minVenueLevel,Integer maxVenueLevel,String customerEmail,Map tierCost){
    Map reservedData=null;
    reservedData=ticketReservationService.findAndHoldSeats(numSeats,minVenueLevel,maxVenueLevel,customerEmail,tierCost);
    return reservedData;
  }

  */
/**
   *
   * @param reservedMapDetails
   * @return
   *//*

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

  */
/**
   *
   * @param reservationId
   * @param reservedMapDetails
   * @param reservedSeat
   *//*

  private void confirmTicketReservation(int reservationId,Map reservedMapDetails,String reservedSeat){
    String confirmationInput=null;
    int reserveHoldTime = holdTime / (60 * 1000);
    System.out.print("\nPress 1 to confirm the booking or any other key to cancel booking.You have " + reserveHoldTime + " minute(s) to confirm your reservation:\t");
    //schedule the timer
    timer.schedule(new ReservationTimer(reservationId), holdTime);

    //Tickets finalized
    if (timer != null) {
      try {
        confirmationInput = inputScanner.nextLine().trim();
        if ("1".equals(confirmationInput)) {
          System.out.println("\nYour tickets are being finalized.Please wait.....");
          timer.cancel();
          ticketReservationService.reserveSeats(reservationId);
          System.out.println("\nThanks for booking the tickets. Please find the reservation details below");
          System.out.println("Reserved Id - "  + reservationId);
          System.out.println("Reserved Seat Numbers- " + reservedSeat + " Total cost: $" + reservedMapDetails.get("reservationCost"));
          System.out.println("Reserved amount- "  + reservedMapDetails.get("reservationCost"));
          System.out.println("Reservation amount- " + reservedMapDetails.get("reservationCost"));
          System.out.println("-------------------------------------------------------------------------------");
          timer = new Timer();
          loadDetails();
        } else {
          ticketReservationService.deleteReservationHold(reservationId);
          System.out.println("\nTickets cancelled. Thanks for visiting Ticket reservation system.");
          System.out.println("-------------------------------------------------------------------------------");
          timer.cancel();
          timer = new Timer();
          loadDetails();
        }
      } catch (Exception exception) {
        exception.printStackTrace();
        ticketReservationService.deleteReservationHold(reservationId);
        System.out.println("\nError occurred.");
        System.out.println("-------------------------------------------------------------------------------");
        timer.cancel();
        timer = new Timer();
        loadDetails();
      }
    }
  }

  */
/**
   * Timer class to release the seat after set time
   *//*

  class ReservationTimer extends TimerTask {
    int reservationId;

    public ReservationTimer(){}
    public ReservationTimer(int reservationId)
    {
      this.reservationId = reservationId;
    }
    @Override
    public void run() {
      ticketReservationService.deleteReservationHold(reservationId);
      System.out.println("\nTime exceeded for reservation. Please start the booking again.");
      timer.cancel();
      timer = null;
      loadDetails();
    }
  }


}
*/
