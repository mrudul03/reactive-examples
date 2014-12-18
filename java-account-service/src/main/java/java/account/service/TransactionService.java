package java.account.service;

import java.account.model.TransactionCollectionResponse;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.springframework.stereotype.Service;

@Service
public class TransactionService {
	
	private Client transactionsServiceClient;
	private WebTarget transactionsServiceTarget;
	
	@PostConstruct
	public void initializeRestClients() {

		transactionsServiceClient = ClientBuilder.newClient();
		transactionsServiceTarget = transactionsServiceClient.target("http://localhost:8080/transactionservice");
		
		System.out.println("Initiakized clients and targets...");
	}
	
	public Future<TransactionCollectionResponse> retrieveTransactions(String accountNumber){
		Future<TransactionCollectionResponse> response = transactionsServiceTarget
				.path("/accounts/"+accountNumber+"/transactions").request().async()
				.get(TransactionCollectionResponse.class);
		
		return response;
		
	}
}
