package java.account.resource;

import java.account.model.Account;
import java.account.model.AccountInfo;
import java.account.model.AccountInfoCollectionResponse;
import java.account.model.TransactionCollectionResponse;
import java.account.service.AccountInfoService;
import java.account.service.TransactionService;
import java.account.util.FutureUtil;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;


@Path("customers")
public class AccountResource {
	
	private ExecutorService executor = Executors.newFixedThreadPool(20);
	
	@Inject
	private AccountInfoService accountInfoService;
	
	@Inject
	private TransactionService transactionService;

    @GET
    @Path("/{customerNumber}/accounts/{accountNumber}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void retrieveAccount(
			@Suspended final AsyncResponse asyncResponse,
			@PathParam("customerNumber") String customerNumber,
			@PathParam("accountNumber") String accountNumber) {
        
    	CompletableFuture<AccountInfo> accountInfoResponse = 
    			FutureUtil.toCompletable(accountInfoService.retrieveAccountInfo(customerNumber, accountNumber), 
    					executor);
    	
    	CompletableFuture<TransactionCollectionResponse> transcationsResponse = 
    			FutureUtil.toCompletable(transactionService.retrieveTransactions(accountNumber), 
    					executor);
    	
    	CompletableFuture<Account> response = 
    			accountInfoResponse.thenCombine(transcationsResponse, (acc, trans) -> combine(acc, trans));
    	
    	try{
    		Account account = response.get();
    		asyncResponse.resume(account);
    		
    	}catch(InterruptedException | ExecutionException e){
    		asyncResponse.resume(e);
    	}
    	
    }
    
    private Account combine(AccountInfo accInfo, TransactionCollectionResponse trans){
    	Account account = new Account();
		account.setAccountNumber(accInfo.getAccountNumber());
		account.setNickName(accInfo.getNickName());
		account.setTransactions(trans.getTransactions());
    	return account;
    }
    
    @GET
    @Path("/{customerNumber}/accounts")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void retrieveAccounts(
			@Suspended final AsyncResponse asyncResponse,
			@PathParam("customerNumber") String customerNumber) {
    	
    	try {
    		CompletableFuture<AccountInfoCollectionResponse> accountInfoCollResponse = 
        			FutureUtil.toCompletable(accountInfoService.retrieveAccountInfoCollection(customerNumber), 
        					executor);
    		
    		List<Account> accounts = accountInfoCollResponse.get().getAccounts().stream()
    		.map(new Function<AccountInfo, Account>(){

				@Override
				public Account apply(AccountInfo accountInfo) {
					
					CompletableFuture<TransactionCollectionResponse> transcationsResponse = 
			    			FutureUtil.toCompletable(transactionService.retrieveTransactions(
			    					accountInfo.getAccountNumber()),  executor);
					Account account = null;
					try {
						 account = combine(accountInfo, transcationsResponse.get());
					}catch(InterruptedException | ExecutionException e){
			    		asyncResponse.resume(e);
			    	}
					return account;
				}
    			
    		}).collect(Collectors.toList());
    		
    		asyncResponse.resume(accounts);
    		
    	}catch(InterruptedException | ExecutionException e){
    		asyncResponse.resume(e);
    	}
    	
    }
}
