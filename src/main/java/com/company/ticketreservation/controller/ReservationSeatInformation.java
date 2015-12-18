package com.company.ticketreservation.controller;

import com.company.ticketreservation.dao.TicketReserveDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by a1096282 on 12/11/15.
 */
public class ReservationSeatInformation {

  public static ApplicationContext applicationContext=null;
  public static TicketReserveDAO ticketReserveDAO=null;

  public static void main(String[] args){

    applicationContext=new ClassPathXmlApplicationContext("classpath:reservation-config.xml");
    ticketReserveDAO=(TicketReserveDAO)applicationContext.getBean("ticketReserveDAO");
    System.out.println(" Insertion started");
    ticketReserveDAO.insertSeatDetails();
    System.out.println(" Insertion Complete");
  }
}
