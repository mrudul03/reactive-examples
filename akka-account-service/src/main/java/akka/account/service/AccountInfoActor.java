package akka.account.service;

import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import akka.account.model.AccountInfo;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class AccountInfoActor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    
    private Client accountInfoServiceClient;
	private WebTarget accountInfoServiceTarget;

	public void initializeRestClients() {

		accountInfoServiceClient = ClientBuilder.newClient();
		accountInfoServiceTarget = accountInfoServiceClient.target("http://localhost:8080/accountinfoservice");

		System.out.println("Initiakized clients and targets...");
	}

    public static Props mkProps() {
        return Props.create(AccountInfoActor.class);
    }

    @Override
    public void preStart() {
    	initializeRestClients();
    	System.out.println("AccountInfoActor initialized clients");
    }

    @Override
    public void onReceive(Object message) throws Exception {
    	System.out.println("Received message:");
        if (message instanceof AccountDto) {
        	AccountDto dto = (AccountDto)message;
        	
            //log.debug("received message: " + (Integer)message);
        	Future<AccountInfo> accountInfoFuture = accountInfoServiceTarget.
    				path("/customers/"+dto.getCustomerNumber()+"/accounts/"+dto.getAccountNumber()).
    				request().async().get(AccountInfo.class);
        	
        	System.out.println("Got accout info accountInfoFuture..."+accountInfoFuture);
        	getSender().tell(accountInfoFuture, getSelf());
        	
        } else {
            unhandled(message);
        }

    }

}
