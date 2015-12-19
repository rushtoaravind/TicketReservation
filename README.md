# TicketReservation

Steps to download the application and running in your local PC (MAC instructions)

Installing git and jdk
------------------------
1. Try to run git from the Terminal the very first time. If you don't have it installed already, it will prompt you to install it. Install it from the App store.
2. Create a folder named TicketReservation in your local.
3. Initialize the git repository using "git init"
4. Get the code from repository using "git pull https://github.com/rushtoaravind/TicketReservation.git"
5. Install jdk if not installed. Type java in the terminal and it will prompt if the java is not installed. Install the jdk dmg.

Installing maven:
-----------------
1. ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)";
2. brew install maven

Installing mysql:
-----------------
1.  Download mysql from http://cdn.mysql.com//Downloads/MySQL-5.7/mysql-5.7.10-osx10.10-x86_64.dmg. Note down the password after the succesful install.
2.  Download mysql workbench from http://cdn.mysql.com//Downloads/MySQLGUITools/mysql-workbench-community-6.3.6-osx-x86_64.dmg
3.  Go to /usr/local/mysql and change the permission of data folder to mysql using sudo chown -R data. You can also change the permission of other folders as well.
4.  Open the Mysql prefernce pane and Click on start SQL.
5.  Open the MySQL community workbench. Login with the password which we got during initial install.
6.  When you try to open/test connection it will prompt to change the password. Make sure you have the userid/pwd as root:root.

Database and tables creation:
-------------------
1. Use the file DB changes.txt in the project folder to create the tables and insert the master data
2. We have table for seat repository for which the data can be inserted from java program.

Running the application from jar(packaged using maven):
--------------------------------
1. Get the code using  "git pull https://github.com/rushtoaravind/TicketReservation.git"
2. The application is packaged in jar file using maven and can be run just by downloading this file. Data base alone has to be created first.
3. After creating database, we need to insert the seat repository data by using
   java -cp TicketReservation-1.0-SNAPSHOT.jar com.company.ticketreservation.controller.ReservationSeatInformation
4. Once this step is complete we can start the application and book the tickets using
   java -cp TicketReservation-1.0-SNAPSHOT.jar com.company.ticketreservation.controller.TicketReservation

Running the application using maven command:
---------------------------------------------
1. Get the code using  "git pull https://github.com/rushtoaravind/TicketReservation.git"
2. Please make sure you have installed jdk,maven,mysql before starting application. Create the database and table using the document attached.
3. "mvn clean install" will compile and build the application and will run the test.
4. if you want to run the tests separately use "mvn surefire:test"
5. Insert the seat repository data using the command "mvn exec:java -Dexec.mainClass="com.company.ticketreservation.controller.ReservationSeatInformation" -Dexec.classpathScope=runtime"
6. You can run the application using "mvn exec:java -Dexec.mainClass="com.company.ticketreservation.controller.TicketReservation" -Dexec.classpathScope=runtime"

APPLICATION ASSUMPTIONS:
------------------------
1. The Reservation system does not have date based reservation system assuming this will be used once.
2. User cannot select the seat required. User can input the venue levels and the seat will be picked sequentially based on the availability.
3. Row based selection or storage is not available.
4. Reservation timeout and max ticket count is configurable.
5. User need not provide any inputs while starting the application and will be asked for inputs later.
6. Payment confirmation is not displayed or asked from the user assuming them as out of scope.
7. Email will not be triggered to the user after booking.
8. A user can book a max of 5(db configured) tickets based on value in DB. The same email can be used to book the tickets again.
9. After time out application will start from the beginning.
10.Need to start the application if it needs to reflect the changes for any data base configured value.
10. Junit test cases available to validate the user input. No Integration test cases are available.

Technology/tool Stack:
----------------------
1. Java
2. Spring framework
3. Hibernate
4. Mysql
5. Junit/Mockito
6. Maven

