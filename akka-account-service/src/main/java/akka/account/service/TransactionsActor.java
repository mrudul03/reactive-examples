package akka.account.service;

import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import akka.account.model.TransactionCollectionResponse;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class TransactionsActor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    
    private Client transactionsServiceClient;
	private WebTarget transactionsServiceTarget;

	public void initializeRestClients() {

		transactionsServiceClient = ClientBuilder.newClient();
		transactionsServiceTarget = transactionsServiceClient.target("http://localhost:8080/transactionservice");
		
		System.out.println("Initiakized clients and targets...");
	}

    public static Props mkProps() {
        return Props.create(AccountInfoActor.class);
    }

    @Override
    public void preStart() {
    	initializeRestClients();
    	System.out.println("TransactionsActor initialized clients");
    }

    @Override
    public void onReceive(Object message) throws Exception {
    	System.out.println("Received message:");
        if (message instanceof AccountDto) {
        	AccountDto dto = (AccountDto)message;
        	
            //log.debug("received message: " + (Integer)message);
        	Future<TransactionCollectionResponse> transFuture = transactionsServiceTarget
    				.path("/accounts/"+dto.getAccountNumber()+"/transactions").request().async()
    				.get(TransactionCollectionResponse.class);
        	
        	System.out.println("Got transactions transFuture..."+transFuture);
        	getSender().tell(transFuture, getSelf());
        	
        } else {
            unhandled(message);
        }

    }

}
