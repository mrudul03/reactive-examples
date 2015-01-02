package rxjava.account.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rxjava.account.model.Account;
import rxjava.account.model.AccountInfo;
import rxjava.account.model.Transaction;

public class AccountService {
	private ExecutorService executor = Executors.newFixedThreadPool(20);
	private AccountInfoService accountInfoService = new AccountInfoService();
	private TransactionService transactionService = new TransactionService();

	public Observable<Account> retrieveAccounts(String customerNumber) {
		Observable<AccountInfo> accountInfoObs = accountInfoService.retrieveAccounts(customerNumber, executor);
		
		return accountInfoObs.flatMap(new Func1<AccountInfo, Observable<Account>>(){

			@Override
			public Observable<Account> call(AccountInfo accountInfo) {
				Observable<List<Transaction>> transObs = transactionService.retrieveTransactions(accountInfo.getAccountNumber(), executor);
				Observable<AccountInfo> accountInfoObs = Observable.just(accountInfo);
				return Observable.zip(accountInfoObs, transObs, new Func2<AccountInfo, List<Transaction>, Account>(){

					@Override
					public Account call(AccountInfo accInfo, List<Transaction> transactions) {
						Account account = new Account();
						account.setAccountNumber(accInfo.getAccountNumber());
						account.setNickName(accInfo.getNickName());
						account.setTransactions(transactions);
						return account;
					}
				});
			}
		});
	}
	
	

	public Observable<Account> retrieveAccount(String customerNumber, String accountNumber) {
		Observable<AccountInfo> accountInfo = accountInfoService
				.retrieveAccountInfo(customerNumber, accountNumber, executor);
		Observable<List<Transaction>> transcations = transactionService
				.retrieveTransactions(accountNumber, executor);
		
		Observable<Account> account = Observable.zip(
			accountInfo,
			transcations, new Func2<AccountInfo, List<Transaction>, Account>(){
		    	@Override
		        public Account call(AccountInfo accInfo, List<Transaction> xtns) {
		    		Account accountDetail = new Account();
		    		accountDetail.setAccountNumber(accInfo.getAccountNumber());
		    		accountDetail.setNickName(accInfo.getNickName());
		    		accountDetail.setTransactions(xtns);
		    		return accountDetail;
		    	}
		    });
		return account;
	}
}
