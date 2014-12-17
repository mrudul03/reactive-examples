package rest.transcation.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("accounts")
public class TranscationResource {

	@GET
	@Path("/{accountNumber}/transactions")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public TransactionCollectionResponse getTranscations(
			@PathParam("accountNumber") String accountNumber) {
		
		List<Transaction> transactions = new ArrayList<Transaction>();
		
		for(int i=0; i<3; i++){
			Transaction transaction = new Transaction(accountNumber, "FromAcc-"+i, "2014-12-12", 10);
			transactions.add(transaction);
		}
		
		TransactionCollectionResponse response = new TransactionCollectionResponse();
		response.setTransactions(transactions);
		return response;
	}
}
