package akka.account.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import scala.concurrent.duration.Duration;
import akka.account.service.AccountInfoActor;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;

//@ApplicationPath("/")
public class AccountServiceConfig extends ResourceConfig {

	private ActorSystem system;

	public AccountServiceConfig() {

		system = ActorSystem.create("AccountSystem");
		system.actorOf(
				AccountInfoActor.mkProps().withRouter(new RoundRobinPool(5)),
				"accountInfoRouter");

		system.actorOf(
				AccountInfoActor.mkProps().withRouter(new RoundRobinPool(5)),
				"transactionsRouter");
		
		register(new AbstractBinder() {
			protected void configure() {
				bind(system).to(ActorSystem.class);
			}
		});

		// register(new JacksonJsonProvider().
		// configure(SerializationFeature.INDENT_OUTPUT, true));
		packages("akka.account.resource");
		System.out.println("Constructor AccountServiceConfig called");

	}

	@PreDestroy
	private void shutdown() {
		system.shutdown();
		system.awaitTermination(Duration.create(15, TimeUnit.SECONDS));
	}

}
