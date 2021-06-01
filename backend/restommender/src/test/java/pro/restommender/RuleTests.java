package pro.restommender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import pro.restommender.dto.Search;
import pro.restommender.model.RelevantRestaurants;
import pro.restommender.model.Restaurant;
import pro.restommender.repository.RestaurantRepository;

// @RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test.properties")
public class RuleTests {

  private KieContainer kContainer;

  // @MockBean
  @Autowired
  private RestaurantRepository restaurantRepository;

  @BeforeEach
  public void init() {
    KieServices ks = KieServices.Factory.get();
    this.kContainer = ks.newKieContainer(ks.newReleaseId("pro", "drools-kjar", "0.0.1-SNAPSHOT"));
  }

  // @Test
  // public void testKieSessionShoudReturnTrue() {
  //   KieSession kieSession = kContainer.newKieSession("ksession-rules");
  //   assertTrue(true);
  // }

  /*
	 * Pozivamo pravila za lokaciju.
	 * 
	 * Trazimo sve lokacije koje se nalaze u centru grada (< 1).
	 */
	@Test
	void searchLocation_inCenter_true() {

    KieSession kieSession = kContainer.newKieSession("ksession-rules");

		Search s = new Search();
		s.setLocation(0.6);

    List<Restaurant> restaurants = restaurantRepository.findAll();

		RelevantRestaurants rr = new RelevantRestaurants();
		rr.setRelevantRestaurants(restaurants);

		kieSession.getAgenda().getAgendaGroup("location").setFocus();
		kieSession.insert(rr);
		kieSession.insert(s);
		int num = kieSession.fireAllRules();

    kieSession.dispose();

		System.out.println("Fired rules: " + num);
		System.out.println(rr.getRelevantRestaurants().size());

		assertTrue(rr.getRelevantRestaurants().size() == 1);
    assertEquals(num, 1);
	}

}