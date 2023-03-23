# Automation2
CREATE A WEB ORDER


1. Launch Chrome browser.
2. Navigate to http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx
3. Login using username Tester and password test
4. Click on Order link
5. Enter a random product quantity between 1 and 100
6. Click on Calculate and verify that the Total value is correct. 
   The logic of calculating is as follows:
   Price per unit is 100.  The discount of 8 % is applied to quantities of 10+.
   So for example, if the quantity is 8, the Total should be 800.
   If the quantity is 20, the Total should be 1840.
   If the quantity is 77, the Total should be 7084. And so on.

   
EXTRA: As an extra challenge, for steps 6-10 download 1000 row of corresponding realistic data from mockaroo.com in a csv format and load it to your program and use the random row of data from there each time. 

11. Select the card type randomly. On each run your script should select a random type. (You need to put all checkboxes into a list and retrieve a random element from the list and click it)
12. Enter the random card number: 
      If Visa is selected, the card number should be a visa number that starts with 4.
      If MasterCard is selected, card number should be a mastercard number that starts with 5.
      If American Express is selected, card number should be an amex number that starts with 3.
13. Enter a valid expiration date (newer than the current date)
14. Click on Process
15. Verify that “New order has been successfully added” message appeared on the page.
16. Click on View All Orders link.
17. The placed order details appears on the first row of the orders table. Verify that the entire information contained on the row (Name, Product, Quantity, etc) matches the previously entered information in previous steps.
18. Log out of the application.
