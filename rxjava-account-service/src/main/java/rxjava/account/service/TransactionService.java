package rxjava.account.service;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;


import rx.Observable;
import rxjava.account.model.Transaction;
import rxjava.account.model.TransactionCollectionResponse;

public class TransactionService {
	
	private Client transactionsServiceClient;
	private WebTarget transactionsServiceTarget;
	
	public TransactionService(){
		initializeRestClients();
	}
	
	public void initializeRestClients() {

		transactionsServiceClient = ClientBuilder.newClient();
		transactionsServiceTarget = transactionsServiceClient.target("http://localhost:8080/transactionservice");
		
		System.out.println("Initiakized clients and targets...");
	}
	
	public Observable<List<Transaction>> retrieveTransactions(final String accountNumber, ExecutorService executor) {
		return Observable
			.create( (Observable.OnSubscribe<List<Transaction>>) subscriber -> {

				Runnable r = () -> {
					TransactionCollectionResponse response = transactionsServiceTarget.path("/accounts/"+accountNumber+"/transactions").request().get(TransactionCollectionResponse.class);
					subscriber.onNext(response.getTransactions());
					subscriber.onCompleted();
				};
				executor.execute(r);
			});
	}
}
