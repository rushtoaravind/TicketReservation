import com.company.ticketreservation.manager.TicketReservationManager;
import com.company.ticketreservation.model.AuditoriumMaster;
import com.company.ticketreservation.model.SeatRepository;
import com.company.ticketreservation.service.TicketReservationService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Class used for testing the TicketReservationManager
 */

public class TicketReservationManagerTest {

  public TicketReservationService getMockReservationService() {
    return mockReservationService;
  }

  public void setMockReservationService(TicketReservationService mockReservationService) {
    this.mockReservationService = mockReservationService;
  }

  TicketReservationService mockReservationService=null;
  TicketReservationManager ticketReservationManager=null;
  List auditoriumList=null;
  List seatRepositoryList=null;
  Map<Integer,String> tierNameMap=new HashMap();
  Map reservedDetailsMap=new HashMap();

  @Before
  public void setUp() throws Exception {

    ticketReservationManager=new TicketReservationManager();
    mockReservationService=mock(TicketReservationService.class);
    this.setMockReservationService(mockReservationService);
    auditoriumList=setAuditoriumListData();
    seatRepositoryList=setSeatRepositoryList();
    tierNameMap=setTierName();
    reservedDetailsMap.put("seatRepositoryList",seatRepositoryList);
  }

  @Test
  public void validateVenueLevelTest(){
    String venueDetails="1:Orchestra|2:Main|3:Balcony1|4:Balcony2";

    assertTrue(ticketReservationManager.validateVenueLevel("1",venueDetails));
    assertTrue(ticketReservationManager.validateVenueLevel("2",venueDetails));
    assertTrue(ticketReservationManager.validateVenueLevel("3",venueDetails));
    assertTrue(ticketReservationManager.validateVenueLevel("4",venueDetails));
    assertTrue(ticketReservationManager.validateVenueLevel("0",venueDetails));

    assertFalse(ticketReservationManager.validateVenueLevel("5",venueDetails));
    assertFalse(ticketReservationManager.validateVenueLevel("-1",venueDetails));
    assertFalse(ticketReservationManager.validateVenueLevel(null,venueDetails));
  }

  @Test
  public void validateMinimumVenueLevelTest(){
    int maxDBVenueLevel=4;

    assertTrue(ticketReservationManager.validateMinimumVenueLevel("0", maxDBVenueLevel));
    assertTrue(ticketReservationManager.validateMinimumVenueLevel("1",maxDBVenueLevel));
    assertTrue(ticketReservationManager.validateMinimumVenueLevel("2",maxDBVenueLevel));
    assertTrue(ticketReservationManager.validateMinimumVenueLevel("3",maxDBVenueLevel));
    assertTrue(ticketReservationManager.validateMinimumVenueLevel("4",maxDBVenueLevel));

    assertFalse(ticketReservationManager.validateMinimumVenueLevel("-1",maxDBVenueLevel));
    assertFalse(ticketReservationManager.validateMinimumVenueLevel(null, maxDBVenueLevel));
    assertFalse(ticketReservationManager.validateMinimumVenueLevel("5", maxDBVenueLevel));
  }

  @Test
  public void validateMaximumVenueLevelTest(){
    int maxDBVenueLevel=4;
    String minDBVenueLevel="1";

    assertTrue(ticketReservationManager.validateMaximumVenueLevel("0",maxDBVenueLevel,minDBVenueLevel));
    assertTrue(ticketReservationManager.validateMaximumVenueLevel("2",maxDBVenueLevel,minDBVenueLevel));
    assertFalse(ticketReservationManager.validateMaximumVenueLevel("1",maxDBVenueLevel,minDBVenueLevel));

  }

  @Test
  public void validateBookingSeatCountTest(){
    int maxTicketValue=5;

    assertTrue(ticketReservationManager.validateBookingSeatCount(maxTicketValue,"1"));
    assertTrue(ticketReservationManager.validateBookingSeatCount(maxTicketValue,"2"));
    assertTrue(ticketReservationManager.validateBookingSeatCount(maxTicketValue,"3"));
    assertTrue(ticketReservationManager.validateBookingSeatCount(maxTicketValue,"4"));
    assertTrue(ticketReservationManager.validateBookingSeatCount(maxTicketValue,"5"));

    assertFalse(ticketReservationManager.validateBookingSeatCount(maxTicketValue, "-1"));
    assertFalse(ticketReservationManager.validateBookingSeatCount(maxTicketValue, "0"));
    assertFalse(ticketReservationManager.validateBookingSeatCount(maxTicketValue, "6"));
    assertFalse(ticketReservationManager.validateBookingSeatCount(maxTicketValue, null));

  }

  @Test
  public void validateCustomerEmailTest(){

    assertTrue(ticketReservationManager.emailValidator("cv@vvv.com"));
    assertTrue(ticketReservationManager.emailValidator("abc@gmail.com"));

    assertFalse(ticketReservationManager.emailValidator(""));
    assertFalse(ticketReservationManager.emailValidator(null));
    assertFalse(ticketReservationManager.emailValidator("123"));
    assertFalse(ticketReservationManager.emailValidator("sdc"));
    assertFalse(ticketReservationManager.emailValidator("sd@df"));
    assertFalse(ticketReservationManager.emailValidator("cv@vvv..."));
    assertFalse(ticketReservationManager.emailValidator("cv@vvv...com"));
  }

  @Test
  public void validateIntegerTest(){

    assertFalse(ticketReservationManager.validateInteger(null)) ;
    assertFalse(ticketReservationManager.validateInteger("Test")) ;
    assertFalse(ticketReservationManager.validateInteger("-1")) ;
    assertFalse(ticketReservationManager.validateInteger("11111111111111111111")) ;

    assertTrue(ticketReservationManager.validateInteger("0")) ;
    assertTrue(ticketReservationManager.validateInteger("1")) ;
  }

  @Test
  public void venueLevelDetailsTest(){

    assertEquals("1:Test|2:Test1", ticketReservationManager.getVenueLevelsDetails(auditoriumList));
    assertEquals("", ticketReservationManager.getVenueLevelsDetails(null));

  }

  @Test
  public void getReservedSeatDetailsTest(){

  assertNotEquals("Orchestra-1,Orchestra-2,Main-1,Balcony1-1,Balcony2-2", ticketReservationManager.getReservedSeatDetails(reservedDetailsMap, tierNameMap));
  assertEquals("Orchestra-1,Orchestra-2,Main-1,Balcony1-1,Balcony2-1", ticketReservationManager.getReservedSeatDetails(reservedDetailsMap, tierNameMap));
  assertNotEquals("Orchestra-1,Orchestra-2,Main-1,Balcony1-1,Balcony2-1", ticketReservationManager.getReservedSeatDetails(null, null));
  assertNotEquals("Orchestra-1,Orchestra-2,Main-1,Balcony1-1,Balcony2-2", ticketReservationManager.getReservedSeatDetails(reservedDetailsMap, tierNameMap));

  }

  private List setAuditoriumListData(){
    List<AuditoriumMaster> auditoriumList=new ArrayList();
    AuditoriumMaster auditoriumMaster=new AuditoriumMaster();
    auditoriumMaster.setTierFees(new Double(100));
    auditoriumMaster.setTierName("Test");
    auditoriumMaster.setTierId(1);
    auditoriumMaster.setTierTotalCapacity(20);
    auditoriumMaster.setTierRowCapacity(10);
    auditoriumMaster.setTierRows(10);

    AuditoriumMaster auditoriumMaster1=new AuditoriumMaster();
    auditoriumMaster1.setTierFees(new Double(50));
    auditoriumMaster1.setTierName("Test1");
    auditoriumMaster1.setTierId(2);
    auditoriumMaster1.setTierTotalCapacity(20);
    auditoriumMaster1.setTierRowCapacity(10);
    auditoriumMaster1.setTierRows(10);

    auditoriumList.add(auditoriumMaster);
    auditoriumList.add(auditoriumMaster1);

    return auditoriumList;
  }

  private List setSeatRepositoryList(){
    List<SeatRepository> seatRepositoryList=new ArrayList();

    SeatRepository seatRepository=new SeatRepository();
    seatRepository.setTierId(1);
    seatRepository.setSeatId(1);

    SeatRepository seatRepository1=new SeatRepository();
    seatRepository1.setTierId(1);
    seatRepository1.setSeatId(2);

    SeatRepository seatRepository2=new SeatRepository();
    seatRepository2.setTierId(2);
    seatRepository2.setSeatId(1);

    SeatRepository seatRepository3=new SeatRepository();
    seatRepository3.setTierId(3);
    seatRepository3.setSeatId(1);

    SeatRepository seatRepository4=new SeatRepository();
    seatRepository4.setTierId(4);
    seatRepository4.setSeatId(1);

    seatRepositoryList.add(seatRepository);
    seatRepositoryList.add(seatRepository1);
    seatRepositoryList.add(seatRepository2);
    seatRepositoryList.add(seatRepository3);
    seatRepositoryList.add(seatRepository4);

    return seatRepositoryList;
  }

  private Map setTierName(){
    Map<Integer,String> tierNameMap=new HashMap();
    tierNameMap.put(1,"Orchestra");
    tierNameMap.put(2,"Main");
    tierNameMap.put(3,"Balcony1");
    tierNameMap.put(4,"Balcony2");
    return tierNameMap;
  }
}
