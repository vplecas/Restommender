package pro.restommender;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import pro.restommender.model.Message;
import pro.restommender.model.Reservation;
import pro.restommender.model.Restaurant;
import pro.restommender.model.User;

@SpringBootApplication
@EnableScheduling
public class RestommenderApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(RestommenderApplication.class, args);
		testReservationDiscount();
	}

	@Bean
	public KieContainer kieContainer() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("pro", "drools-kjar", "0.0.1-SNAPSHOT"));
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(10_000);
		return kContainer;
	}

	public static void testReservationDiscount() {
		User u = new User();
		Reservation r1 = new Reservation();
		r1.setUser(u);
		// Reservation r2 = new Reservation();
		// r2.setUser(u);
		// Reservation r3 = new Reservation();
		// r3.setUser(u);

		List<Reservation> reservations = new ArrayList<>();
		reservations.add(r1);
		// reservations.add(r2);
		// reservations.add(r3);
		u.setReservations(reservations);

		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("pro", "drools-kjar", "0.0.1-SNAPSHOT"));
		KieSession kieSession = kContainer.newKieSession();
		kieSession.getAgenda().getAgendaGroup("reservation-number-discount").setFocus();
		kieSession.insert(r1);
		int num = kieSession.fireAllRules();
		kieSession.dispose();

		System.out.println("----------------------");
		System.out.println("Fired rules: " + num);

	

	}


	public static void testLocation() {
		Restaurant r = new Restaurant();
		r.setLocation(7.2);


		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("pro", "drools-kjar", "0.0.1-SNAPSHOT"));
		KieSession kieSession = kContainer.newKieSession();
		kieSession.getAgenda().getAgendaGroup("location").setFocus();
		kieSession.insert(r);
		int num = kieSession.fireAllRules();
		kieSession.dispose();

		System.out.println("----------------------");
		System.out.println("Fired rules: " + num);
	}

	public static void testMessage() throws Exception{
		Message message = new Message();
		message.setMessage("Hello World");
		message.setStatus(Message.GOODBYE);

		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("pro", "drools-kjar", "0.0.1-SNAPSHOT"));
		KieSession kieSession = kContainer.newKieSession();
		kieSession.insert(message);
		Thread.sleep(1000);
		int num = kieSession.fireAllRules();
		Thread.sleep(1000);
		kieSession.dispose();

		System.out.println("----------------------");
		System.out.println("Fired rules: " + num);
	}

	/**
	 * pokretanje:
	 * 1. cd backend/drools-kjar
	 * 2. ./mvnw  clean install
	 * 
	 * 1. cd backend/restommender
	 * 2. ./mvnw  clean package
	 * 3. run restommender
	 */

}
