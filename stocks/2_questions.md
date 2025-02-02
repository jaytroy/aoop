# Question 1

In the assignment, you had to create a `MessageHandler` interface. Please answer the following two questions:

1. Describe the benefits of using this `MessageHandler` interface. (~50-100 words)

Using the MessageHandler interface allows us to define different behaviours for different types of message handlers across multiple modules through some handleMessage() method. This allows us to decouple modules from one another, as well as providing a way to abstract and generalize our code.
2. Instead of creating an implementation of `MessageHandler` that invokes a command handler, we could also pass the command handler to the client/server directly without the middle man of the `MessageHandler` implementation. What are the implications of this? (~50-100 words)

This would end up in us coupling the modules, which we do not want. For example, Networking should stay independent of any other modules to ensure potential future re-usability. In order to pass the command handler directly into the client/server, we'd need to add a dependency on the command module into networking. By implementing a CommandHandler via a MessageHandler, we can bypass this dependency by passing the MessageHandler, defined in Networking, as a parameter to either client or server.

___

___

# Question 2

One of your colleagues wrote the following class:

```java
public class RookieImplementation {

    private final Car car;

    public RookieImplementation(Car car) {
        this.car = car;
    }

    public void carEventFired(String carEvent) {
        if("steer.left".equals(carEvent)) {
            car.steerLeft();
        } else if("steer.right".equals(carEvent)) {
            car.steerRight();
        } else if("engine.start".equals(carEvent)) {
            car.startEngine();
        } else if("engine.stop".equals(carEvent)) {
            car.stopEngine();
        } else if("pedal.gas".equals(carEvent)) {
            car.accelerate();
        } else if("pedal.brake".equals(carEvent)) {
            car.brake();
        }
    }
}
```

This code makes you angry. Briefly describe why it makes you angry and provide the improved code below.

___

**Answer**: This code makes me angry because it is ugly and bad!! I can use a switch statement to remove the hard-to-read if block.

Improved code:

```java
class ProfessionalImplementation {

    private final Car car;

    public ProfessionalImplementation(Car car) {
        this.car = car;
    }
    
    public void carEventFired(String carEvent) {
        switch(carEvent) {
            case "steer.left":
                car.steerLeft();
                break; 
            case "steer.right": 
                car.steerRight();
                break; 
            case "engine.start":
                car.startEngine();
                break;
            case "engine.stop":
                car.stopEngine();
                break;
            case "pedal.gas":
                car.accelerate();
                break; 
            case "pedal.brake":
                car.brake();
                break;
        }
    }
}
```
___

# Question 3

You have the following exchange with a colleague:

> **Colleague**: "Hey, look at this! It's super handy. Pretty simple to write custom experiments."

```java
class Experiments {
    public static Model runExperimentA(DataTable dt) {
        CommandHandler commandSequence = new CleanDataTableCommand()
            .setNext(new RemoveCorrelatedColumnsCommand())
            .setNext(new TrainSVMCommand());

        Config config = new Options();
        config.set("broadcast", true);
        config.set("svmdatatable", dt);

        commandSequence.handle(config);

        return (Model) config.get("svmmodel");
    }

    public static Model runExperimentB() {
        CommandHandler commandSequence = new CleanDataTableCommand()
            .setNext(new TrainSGDCommand());

        Config config = new Options();
        config.set("broadcast", true);
        config.set("sgddatatable", dt);

        commandSequence.handle(config);

        return (Model) config.get("sgdmodel");
    }
}
```

> **Colleague**: "I could even create this method to train any of the models we have. Do you know how Jane did it?"

```java
class Processor {
    public static Model getModel(String algorithm, DataTable dt) {
        CommandHandler commandSequence = new TrainSVMCommand()
            .setNext(new TrainSDGCommand())
            .setNext(new TrainRFCommand())
            .setNext(new TrainNNCommand());

        Config config = new Options();
        config.set("broadcast", false);
        config.set(algorithm + "datatable", dt);

        commandSequence.handle(config);

        return (Model) config.get(algorithm + "model");
    }
}
```

> **You**: "Sure! She is using the command pattern. Easy indeed."
>
> **Colleague**: "Yeah. But look again. There is more; she uses another pattern on top of it. I wonder how it works."

1. What is this other pattern? What advantage does it provide to the solution? (~50-100 words)

2. You know the code for `CommandHandler` has to be a simple abstract class in this case, probably containing four methods:
- `CommandHandler setNext(CommandHandler next)` (implemented in `CommandHandler`),
- `void handle(Config config)` (implemented in `CommandHandler`),
- `abstract boolean canHandle(Config config)`,
- `abstract void execute(Config config)`.

Please provide a minimum working example of the `CommandHandler` abstract class.

___

**Answer**:

1.
The other pattern Jane is using on top of the command pattern is the Chain of Responsibility pattern. She chains commands together using setNext(CommandHandler next). This function checks whether the current Commandhandler can execute the command. If not, it passes it on to the next command handler. This provides the advantage of decoupling code. Instead of constantly needing to call a command manually, commands can call each other sequentially based on given criteria.

2.
```java
abstract class CommandHandler {
    CommandHandler next;
    
    CommandHandler setNext(CommandHandler next) {
        this.next = next;
        return next;
    }
    
    void handle(Config config) {
        if (canHandle(config)) {
            execute(config);
        } else if (next != null) {
            next.handle(config);
        }
    }
    
    abstract boolean canHandle(Config config);
    abstract void execute(Config config);
}
```
___
