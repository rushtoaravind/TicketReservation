# TicketReservation

Steps to download the application and running in your local PC (MAC instrutions)

1. Try to run git from the Terminal the very first time. If you don't have it installed already, it will prompt you to install it. Install it from the App store.
2. Create a folder named TicketReservation in your local.
3. Initialize the git repository using "git init"
4. Get the code from repository using "git pull https://github.com/rushtoaravind/TicketReservation.git"
5. Install jdk if not installed. Type java in the terminal and it will prompt if the java is not installed. Install the jdk dmg.

Installing maven:
1. ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)";
2. brew install maven

Installing mysql:
1.  Download mysql from http://cdn.mysql.com//Downloads/MySQL-5.7/mysql-5.7.10-osx10.10-x86_64.dmg. Note down the password after the succesful install.
2.  Download mysql workbench from http://cdn.mysql.com//Downloads/MySQLGUITools/mysql-workbench-community-6.3.6-osx-x86_64.dmg
3.  Go to /usr/local/mysql and change the permission to mysql using sudo chown -R data. You can also change the permission of other folders as well.
4.  Open the Mysql prefernce pane and Click on start SQL.
5.  Open the MySQL community workbench. Login with the password which we got during initial install.
6.  When you try to open/test connection it will prompt to change the password. Make sure you have the userid/pwd as root:root.
7.  
