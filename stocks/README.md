<br />
<p align="center">
  <h1 align="center">Stock Market Simulation</h1>

  <p align="center">
    < A stock market simulation application that allows trading of stocks by traders.>
  </p>
</p>

## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
  * [Running](#running)
* [Modules](#modules)
* [Notes](#notes)
* [Evaluation](#evaluation)
* [Extras](#extras)

## About The Project

This project is a stock market simulation application that provides traders with the ability to buy and sell stocks. It allows traders to monitor stock information, execute trading orders, and receive real-time updates about the stock market.

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher
* [Maven 3.6](https://maven.apache.org/download.cgi) or higher

### Installation

1. Navigate to the `stocks` directory
2. Clean and build the project using:
   sh
   mvn install


### Running

1. Initialize the Stock App:
- Open your terminal or command prompt.
- Navigate to the `stocks app` directory of the project.

  sh
  cd stocks/stocks-app/src/main/java/nl/rug/aoop
  java -jar StockAppMain.jar

2. Run the Trader App:
- After successfully initializing the Stock App, you can now run the Trader App to create trading bots and randomize buy and sell orders.
- Open your terminal or command prompt.
- Navigate to the `traders app` directory of the project.

  sh
  cd stocks/stocks-app/src/main/java/nl/rug/aoop
  java -jar TraderApp.jar


This step activates the trading bots, and they will start generating random buy and sell orders.

Please note that it's essential to first initialize the Stock App to create the trading environment, and then run the Trader App to observe the bots' trading activities. The instructions provided ensure a smooth and sequential execution of the program.

## Modules

The Stock Market Simulation project is divided into several modules, each serving a specific purpose and contributing to the overall functionality of the program:

1. Command Module:

- Command Interface: This module defines the command interface, which serves as a foundation for various commands used in the simulation.

- Command Message Handler: The Command Message Handler class provides functionality to register and execute commands. It plays a crucial role in the interaction between different components of the system.

2. Message Queue Module:

- Consumer: The Consumer class implements the MQConsumer and is responsible for polling messages from a message queue.

- Producer: The Producer class implements the MQProducer and enqueues messages, ensuring they meet specific character constraints.

- LocalDateTimeAdapter: This class is a GSON type adapter for loading LocalDateTime objects.

- Message: The Message class represents individual messages and provides methods for converting them to and from JSON format.

- MessageQueue: This interface outlines the fundamental operations for managing messages in a queue.

- OrderedQueue: OrderedQueue is an implementation of MessageQueue that takes the time of messages into account, enabling ordered message retrieval.

- UnorderedQueue: UnorderedQueue is another implementation of MessageQueue, not considering the message time order.

- CommandMessageHandler: This class handles messages in the form of commands, forwarding them to the appropriate handlers.

- MqPutCommand: MqPutCommand is a specific command class that puts messages into the message queue.

- NetConsumer: NetConsumer is a consumer class designed to work with network-based messages, continuously polling the message queue in the exchange.

- NetProducer: NetProducer is a class responsible for sending input messages to the server through a client.

- TSMessageQueue: TSMessageQueue represents a thread-safe message queue, ensuring safe and efficient message storage and retrieval.

3. Networking Module:

- Client Handler: The Client Handler is responsible for handling client connections, managing incoming messages, and ensuring smooth communication between the clients and the server.

- Client: The Client module represents a client that can connect to the server, send and receive messages, and participate in the stock market simulation.

- Server: The Server component hosts and manages the stock market simulation, acting as the central point for client connections and message exchange.

- NetworkMessage: The NetworkMessage class represents a network message with a header and a body. It offers methods to convert a NetworkMessage to JSON and create a NetworkMessage object from its JSON representation. This class is fundamental for handling network communication between different components of the application.

4. Stock Market UI Module:

- SimpleViewFactory: Initializes a Swing-based user interface for the stock exchange simulation, utilizing the TerminalFrame for displaying UI elements.

- StockExchangeDataModel: Represents the data model of a stock exchange, offering access to information about stocks and traders on the exchange.

- StockDataModel: Represents a single stock's data, including properties like the stock symbol, company name, available funds, owned stocks, shares outstanding, market capitalization, and share price.

- TraderDataModel: Represents a trader's data, including ID, name, available funds, and a collection of owned stocks.

- SimpleView package: This additional package initializes tables and frames for the user interface, enhancing the visual representation of stocks and traders in the application.

5. Stock App Module:

- The Stock App module implements the user interface, loads data related to each stock and trader, and manages the user experience. It provides information about the stocks' prices, trader details, and other stock market data.
- InitializeView: Initializes the Stock App by starting the server and setting up the view, we call this class in StockAppMain such that this class does all the initializing and we can run through the StockAppMain.
- BuyLimitOrderCommand: Represents a command to execute a buy limit order, allowing a trader to buy a specified quantity of shares of a stock if the stock's price is at or below a specified limit price.
- SellLimitOrderCommand: Represents a command to execute a sell limit order, allowing a trader to sell a specified quantity of shares of a stock if the stock's price is at or above a specified limit price.
- Exchange: Represents the central component of the trading system, keeping track of all stocks, traders, and resolving orders.

6. Trader App Module:

- In this module, we focus on the trading aspect of the simulation. It periodically receives information about each trader, both human and bot traders. Bot traders continuously update their activities based on randomly executed buy and sell commands. This module is crucial for simulating trading activities in the stock market.

Each of these modules plays a unique role in making the Stock Market Simulation a comprehensive and interactive program. They work together to ensure a seamless user experience, facilitate communication between clients, and provide a real-time view of the stock market and trader activities.


## Design


1. Observer Pattern:
  - Where Used: The Observer pattern is used to define a one-to-many dependency between objects, so that when one object changes state, all its dependents are notified and updated automatically.
  - Benefit: This pattern allows for loose coupling between components, ensuring that updates are propagated efficiently while keeping the system maintainable.

2. Command Pattern:
  - Where Used: In our application it was used for defining various commands like buying and selling stocks.
  - Benefit: It allows you to separate the sender of a request from its receiver, providing flexibility, extensibility, and the ability to support undo/redo operations, which is valuable in a stock exchange simulation.

3. Factory Method Pattern:
  - Where Used: The Factory Method pattern provides an interface for creating objects, but allows subclasses to alter the type of objects that will be created. It was used for creating instances of traders or stocks with different properties.
  - Benefit: It promotes loose coupling and flexibility in object creation, making it easier to add new types of traders or stocks without changing existing code.

## Evaluation

1. Stability and Successes:
- Observer Pattern for Real-time Updates: The application employs the Observer pattern to provide real-time updates to clients, ensuring that changes in stock prices, trader portfolios, and market conditions are efficiently propagated to users. This contributes to the system's responsiveness.
- Command Pattern for Extensibility: The Command pattern, particularly for buying and selling orders, enhances the extensibility of the system. It allows for different types of orders to be added without significant changes to the existing codebase.

2. Areas for Improvement:
- Realistic Trading Strategies: To make the stock exchange simulation more authentic we should have considered implementing more advanced trading strategies and algorithms. Currently, the system randomly generates buy and sell orders; introducing intelligent trading strategies can add complexity and realism to the simulation.
- User Interface Enhancement: The user interface provided by the SimpleViewFactory could be improved for a more user-friendly experience. Enhancements like advanced charting, transaction history, and order book displays could provide a richer user experience.
- When displaying the owned stocks of the trader in the ui the user can hardly see all the stocks, this could have been improved by adding a button where if you click on it you can view all the owned stocks.

3. Limits:
- While it is good to look for areas of improvement it's also important to evaluate what limits this project had, we were a group of 2 members, so implementing realistic algorithms within the time constraint we were in was going to be difficult.

If there were more time, additional features like automated trading bots, user authentication, and a more interactive and visually appealing user interface could be implemented. Overall, the implementation serves as a solid foundation for a stock market simulation, but further refinements and enhancements could make it an even more comprehensive and realistic tool for understanding stock market dynamics and trading strategies.

## Usage

To use the code all you need to do is run the code and everything will be done automatically.

## Roadmap

In the future, we plan to enhance the project in the following ways:
- Realistic Trading Strategies: We aim to introduce more realistic trading strategies and algorithms, making the stock exchange simulation more authentic.
- User Interface Enhancement: We intend to improve the user interface provided by the SimpleViewFactory. Features like advanced charting, transaction history, and order book displays will be added to provide a richer and more user-friendly experience.
- Improved Owned Stocks Display: We will add a feature to allow users to view all the stocks they own, addressing the issue of limited visibility.

## Contributing

We welcome contributions to this project. If you have ideas, bug fixes, or enhancements, please feel free to make your changes, and create a pull request. We value community collaboration and will review and merge contributions that align with the project's goals.

## Contact

If you have any questions or suggestions regarding the project, you can contact us at [ohassan100100@gmail.com] or [j.borcel@student.rug.nl].

## Important Note:

We are not responsible for any issues that may occur on your system while using this project. Please use the software responsibly and at your own risk.