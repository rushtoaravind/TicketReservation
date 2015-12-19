package com.company.ticketreservation.dao;

import com.company.ticketreservation.model.AuditoriumMaster;
import com.company.ticketreservation.model.DataConfig;
import com.company.ticketreservation.model.SeatRepository;
import com.company.ticketreservation.model.UserReservation;

import org.hibernate.*;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.tuple.Dom4jInstantiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * This class acts a data persistent layer for the Ticker reservation system.
 */
@Repository("ticketReserveDAO")
@Transactional
public class TicketReserveDAOImpl implements TicketReserveDAO {

  @Autowired
  private SessionFactory sessionFactory;

  private Query query;

  /**
   * This method is used to list the auditorium details
   *
   * @return List
   */
  public List<AuditoriumMaster> getAuditoriumDetails(){

    List<AuditoriumMaster> auditoriumList=null;
    Session session = sessionFactory.getCurrentSession();

    query = session.createQuery("from AuditoriumMaster");

    auditoriumList = query.list();
    return auditoriumList;
  }

  public List<DataConfig> getConfigDetails(){
    List<DataConfig> dataConfigList=null;
    Session session = sessionFactory.getCurrentSession();
    query = session.createQuery("from DataConfig");
    dataConfigList=query.list();
    return dataConfigList;
  }
  /**
   * This is method to insert the details of seatRepository
   */
  public void insertSeatDetails(){

    Session session = sessionFactory.getCurrentSession();
    //Transaction transaction=session.beginTransaction();

    List<AuditoriumMaster> auditoriumList=getAuditoriumDetails();
    for(AuditoriumMaster auditoriumMaster:auditoriumList){
      int totalCapacity=auditoriumMaster.getTierTotalCapacity();
      int tierId=auditoriumMaster.getTierId();
      for(int rowCount=1;rowCount<=totalCapacity;rowCount++){
        SeatRepository seatRepository=new SeatRepository();
        seatRepository.setTierId(tierId);
        seatRepository.setSeatId(rowCount);
        seatRepository.setSeatAvailable("N");
        seatRepository.setCreatedTimeStamp(new Date());
        seatRepository.setUpdatedTimeStamp(new Date());
        session.save(seatRepository);
        if (rowCount%50 == 0)
        {
          session.flush();
          session.clear();
        }
        else{
          session.flush();
          session.clear();
        }
      }
    }
    //transaction.commit();

  }

  /**
   *This method is used to find the number of seats available in a venue level
   * @param venueLevel a numeric venue level identifier to limit the search
   * @return int
   */
  public int getSeatsAvailable(Integer venueLevel){
    List<Long> seatRepositoryList=null;
    int seatsAvailable=0;
    Session session = sessionFactory.getCurrentSession();

    Criteria countCriteria = session.createCriteria(SeatRepository.class);
    countCriteria.add( Restrictions.eq("seatAvailable", "Y"));
    if(null!=venueLevel && venueLevel!=0) {
      countCriteria.add(Restrictions.eq("tierId", venueLevel));
    }
    countCriteria.setProjection(Projections.rowCount());
    seatRepositoryList = countCriteria.list();
    if(null!=seatRepositoryList){
      seatsAvailable = Integer.valueOf(((Long) seatRepositoryList.get(0)).toString());
    }
    return seatsAvailable;
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
      Integer maxLevel, String customerEmail, Map tierCost){

    //1. Get the list of seats available.
    //2. Insert into user_reservation with seats on hold
    //return the reservation id

    List<SeatRepository> seatRepositoryList=null;
    Session session = sessionFactory.getCurrentSession();
    Map reservedData=new HashMap();

    Criteria seatHoldCriteria=session.createCriteria(SeatRepository.class);
    seatHoldCriteria.add( Restrictions.eq("seatAvailable", "Y"));

    if(null!=minLevel && null!=maxLevel && minLevel!=0 && maxLevel!=0){
      seatHoldCriteria.add( Restrictions.and(Restrictions.ge("tierId", minLevel),Restrictions.le("tierId", maxLevel)));
    }
    else if(null!=minLevel && minLevel!=0) {
      seatHoldCriteria.add(Restrictions.eq("tierId", minLevel));
    }
    //seatHoldCriteria.add(Restrictions.in("tierId",tierIdList));
    seatHoldCriteria.setMaxResults(numSeats);
   // seatHoldCriteria.setProjection(Projections.rowCount());
    seatHoldCriteria.setProjection(Projections.projectionList().add(Projections.property("id"), "id").add(Projections.property("tierId"), "tierId").add(Projections.property("seatId"), "seatId"));
    seatHoldCriteria.setResultTransformer(Transformers.aliasToBean(SeatRepository.class));
    seatRepositoryList=seatHoldCriteria.list();

    if(null!=seatRepositoryList){
      Double reservationCost= Double.valueOf(0);
      for(SeatRepository seatRepository: seatRepositoryList){
        reservationCost=reservationCost+(Double)tierCost.get(seatRepository.getTierId());
     }

      //if the output is equal to the number of seats required proceed else required number of seats not available
      if(numSeats==seatRepositoryList.size()){

        //First insert the customer data and hold the ticket
        UserReservation userReservation=new UserReservation();
        userReservation.setCustomerEmailId(customerEmail);
        userReservation.setSeatOnHold("Y");
        userReservation.setSeatReserved("N");
        userReservation.setReservationAmount(reservationCost);
        userReservation.setCreatedTimeStamp(new Date());
        userReservation.setUpdatedTimeStamp(new Date());
        session.save(userReservation);

        reservedData.put("userReservation",userReservation);
        reservedData.put("seatRepositoryList",seatRepositoryList);
        reservedData.put("reservationCost",reservationCost);
        //Update the seatId with the reservationId
        for(SeatRepository seatRepository: seatRepositoryList){
          seatRepository.setReservationId(userReservation.getReservationId());
          seatRepository.setSeatAvailable("N");
          seatRepository.setCreatedTimeStamp(new Date());
          seatRepository.setUpdatedTimeStamp(new Date());
          session.update(seatRepository);
          if (numSeats%10 == 0)
          {
            session.flush();
            session.clear();
          }
          else{
            session.flush();
            session.clear();
          }
        }

      }
    }
    return reservedData;
  }

  /**
   * This method is used to delete the seats which were on hold due for customer confirmation
   * @param reservationId
   */
  public void deleteReservationHold(Integer reservationId){

    Session session = sessionFactory.getCurrentSession();

    UserReservation userReservation=new UserReservation();
    userReservation.setReservationId(reservationId);
    session.delete(userReservation);
    //session.clear();

    session = sessionFactory.getCurrentSession();
    query = session.createSQLQuery("update seat_reservation_repository  set reservation_id = 0,seat_available='Y',UPDATED_TIME_STAMP=:currentDate where reservation_id = :reservationId and id>=1");
    query.setParameter("reservationId",reservationId);
    query.setParameter("currentDate",new Date());
    query.executeUpdate();

  }

  /**
   * Commit seats held for a specific customer
   *
   * @param reservationId the seat hold identifier
   * @return a reservation confirmation code
   */
  public void reserveSeats(int reservationId){

    Session session = sessionFactory.getCurrentSession();
    query = session.createSQLQuery("update user_reservation  set seat_reserved = 'Y',UPDATED_TIME_STAMP=:currentDate where reservation_id = :reservationId");
    query.setParameter("reservationId",reservationId);
    query.setParameter("currentDate",new Date());
    query.executeUpdate();

  }
}
