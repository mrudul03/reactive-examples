reactive-examples
=================

This is a simple example. A customer can navigate through all his bank accounts and when they find an account which they want to see more information, they click on the account number, and then a new screen is opened with detailed information of the account and transactions for a given account.

There are two core services for account information and transactions

1. One service to get account information for a given account or all accounts for a given customer id. They could be retrieved from any backend system like a RDBMS. (rest-accountinfo-service)
2. One service to get all transactions for the given account. (rest-transaction-service)

The example also has a third composite service, which aggregates the results of the two core services and returns aggregaged results. The service invokes account information and also transaction service asynchronously and aggreagtes the results.

The composite service is written in different ways.

1. Using RxJava (rxjava-account-service): RxJava is a Java VM implementation of Reactive Extensions: a library for composing asynchronous and event-based programs by using observable sequences. With RxJava instead of pulling data from an structure, data is pushed to it which reacts with an event that are listened by a subscriber and acts accordantly. You can find more information in https://github.com/Netflix/RxJava.

2. Using Java 8 CompletableFuture (java-account-service)
3. Using Akka Actor
