#### Department Project

Administrative Department = [
	* Dashboard
		* Charts
	* Generate Reports
		* Type 								[ Customer Sheet, Staff Sheet ]			:: ComboBox
	* Event
		* Upcoming event 	                [ Meetings ] 				            :: TableView
		* Stats		 	                    [ Employees, Departments, Projects ] 	:: Chart
	* Project Management
		* Display projects 	                [ Projects ] 				            :: TableView
		* Add projects		                [ ] 					                :: Button
	* Manage Users
		* Add			                    [ ]					                    :: Button
		* Modify		                    [ ]					                    :: Button
		* Delete		                    [ ]					                    :: Button
	* Manage Staff							[ ]
		* Add
		* Modify
		* Delete
	* Complaints
	* IT Support
	
]

Finance Department = [
	* Budget Management
		* Budget Item		                [ Budgets ]             		        :: TableView
		* Add budget item	                [ ]					                    :: Button
	* Financial Reporting
		* Display report	                [ Name, GeneratedDate ]					:: ListView || LinePlot
		* Generate report 		            [ DateRange, Format ]					:: Button
			* Report Type 					[ Balance Sheet ]
	* File Complaints
	* IT Support
		
]

Information Department = [
	* Monitoring
		* Hardware		                    [ Hardware status ] 			        :: Label
		* Software		                    [ Software status ] 			        :: Label
	* Help Desk
		* Display ticket	                [ Ticket ]	    			            :: TableView
		* Add ticket		                [ ]		    			                :: Button
	* File Complaints
]

All = [
	* Message Sending
]