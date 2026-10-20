Auto Service Management System
About the Project
This is a Java project for managing car repair orders in an auto service. It contains the main business logic for cars, clients, mechanics, repair work, and repair orders.

The project is still in development. It currently focuses on the main classes and their rules. It does not have a finished user interface or a database.
Features
- Store basic information about clients and their cars.
- Create repair orders and track their status.
- Add repair work and parts costs to an order.
- Assign a mechanic when the order is approved.
- Check if a mechanic is available and has the required skills.
- Mark repair work as completed.
- Complete or cancel repair orders when the rules allow it.
- Keep a list of events for important order changes.
- Calculate the cost of work, parts, and the full order.
- Apply an extra fee to urgent orders and add a profit percentage.
- Save repair orders in a simple in-memory repository.

Technologies
- Java 21 — the programming language.
- Maven — manages the project and its dependencies.
- JUnit 5 — used for unit tests.

Main Classes
- Car — stores information about a car and its owner.
- Client — stores a client's name.
- Mechanic — stores a mechanic's name, skills, and availability.
- Work — describes repair work, its cost, parts cost, and completion state.
- RepairOrder — manages the repair process, status, mechanic, work items, and event history.
- PricePolicy — calculates the final price, including extra amounts for urgent work and profit.
- InMemoryRepairOrderRepository — stores orders in memory while the program is running.

Requirements
Before you start, install:
- Java Development Kit (JDK) 21 or later.
- Apache Maven.
- An IDE such as IntelliJ IDEA (optional).

How to Open the Project
1. Download or clone this project.
2. Open the project folder in IntelliJ IDEA.
3. Wait for Maven to load the dependencies from pom.xml.
4. Make sure the project uses JDK 21.

Current Limitations
- The CarServiceDemo main method is only a placeholder and is not ready for normal use.
- Repair orders are stored in memory, so they are not saved after the program stops.
- There is no finished command-line menu, graphical interface, or database connection yet.

Future Improvements
- Add a working command-line or graphical interface.
- Connect the project to a database.
- Add features to search, update, and list repair orders.
- Improve the application entry point and add more usage examples.
