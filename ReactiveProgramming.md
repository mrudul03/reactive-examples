
I was curious to learn Reactive Programming and wanted to learn the same with a real life use case. When I started looking for good material, I found few practical tutorials but they never explained building the whole architecture around it.

I have also been working with Mircoservices Architecture and constantly face the challenge of making the microservices scalable and less resource consuming. Due to the generic and granular nature of the Microservice, each service returns only a portion of functionality for a given user experience, requiring client applications to make multiple calls that need to be assembled in order to render a single user experience. This added network latency due to multiple server calls. Adding concurrency by making the services asynchronous was a key requirement. That’s when I started reading about Reactive Programming.

There are multiple posts on Reactive programming. Wikipedia says, “Reactive programming is a programming paradigm oriented around data flows and the propagation of change”. After going through various posts on Reactive Programming, the way I understood Reactive programming is, it is programming with asynchronous data flows.

Reactive Programming Principles (via manifesto)

1. Responsive: The application should be quick to reacts to users, even under load and in the presence of failures
2. Resilient and Scalable: The application should be resilient, in order to stay responsive under various conditions. They also should react to changes in the input rate by increasing or decreasing the resources allocated to service these inputs. Today’s applications have more integration complexity, as they are composed of multiple applications.
3. Message Driven: A message-drivenarchitecture is the foundation of scalable, resilient, and ultimately responsive systems.
A message-driven architecture is the foundation of Reactive applications. A message-driven application may be event-driven, actor-based, or a combination of the two. The main difference between messages and events is that messages are directed to destination/s while events happen and may be observed by zero or more observers.

Looking in to each of these message-driven application style

Event Driven Concurrency

An event-driven system is based on events, which are monitored by zero or more observers. The big difference between event-driven style and imperative style is that the caller does not block and hold onto a thread while waiting for a response.

Actor Based Concurrency

Actor-based concurrency is an extension of the message-passing architecture, where messages are directed to an Actor, which happens to be a recipient. An actor is a construct with the following properties:

A mailbox for receiving messages.
The actor’s logic, which relies on pattern matching to determine how to handle each type of message it receives.
Isolated state — rather than shared state — for storing context between requests.
Actors can pass messages back and forth, or even pass messages to them selves; an actor can pass a message to it self in order to finish processing a long-running request after it services other messages in its queue first. Actor based concurrency has a huge benefit of scaling across the network boundaries. This makes it easy to design, build and maintain highly scalable applications.

RxJava

RxJava is library for composing asynchronous and event-based programs using observable sequences for the Java VM.

Using RxJava, you can represent multiple asynchronous data streams that come from diverse sources, e.g., stock quote, tweets, computer events, web service requests, etc., and subscribe to the event stream using the Observer. A stream produces data at different points in time. An observer is notified whenever data in that stream and does something with it. You can map, filter, and reduce streams—that’s where the “functional” part comes in.

RxJava also adds Functional Programming flavor with operators to query, filter, project, aggregate, compose and perform time-based operations on multiple events.

Akka

Akka is toolkit and runtime for building highly concurrent, distributed, and fault tolerant applications on the JVM. Akka’s approach to handling concurrency is based on the Actor Model.

Actor Model is defined by three traits

A behavior (reacts to events)
A state Model
Send and receive (immutable) messages
Actor

Processes message sequentially
Multiple actors can work at the same time
No shared state (interaction between actors is purely asynchronous messages)
Akka

Supervisors provide exception handling
“Let it crash” model provides fault tolerance
Provides location transparency (abstracts the idea of where the actor lives)
Summary

Reactive programming isn’t just another trend but rather the paradigm for modern softwares to embrace and developers to learn. In order to achieve responsiveness, its import to address scalability and resilience and this can be done by adopting Reactive Programming model.

Both Akka and RxJava are awesome libraries to build reactive applications based on their traits.

RxJava

1. Event based
2. Responsive
3. Resilient/fault-tolerant (Provides variety of operators to recover from errors)
4. Scalable (by default Observable is synchronous and requires additional work for asynchronous)

Akka is Reactive

1. Message-based
2. Responsive
3. Resilient/fault-tolerant (Let it crash model)
4. Easily Scalable (Scaling across network boundaries)

I also recommend reading below posts, which were used as source for this information

Benjamin Erb’s Diploma Thesis, Concurrent Programming for Scalable Web Architectures and
Kevin Webber post, What is Reactive Programming?
ReacitveX RxJava
