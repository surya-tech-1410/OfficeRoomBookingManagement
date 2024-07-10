<%@ page import="com.learn.Spring.model.Room"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.springframework.web.servlet.support.RequestContext"%>
<%@ page import="org.springframework.web.servlet.tags.form.FormTag"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Room Booking</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <div class="row">
        <div class="col-12">
            <div class="d-flex justify-content-between align-items-center bg-primary p-3 mb-4">
                <a href="/Book/userBookList" class="btn btn-danger">My Bookings</a>
                <h1 class="text-light mb-0">Room Booking</h1>
                <a href="/logout" class="btn btn-danger">Logout</a>
            </div>
        </div>
    </div>
		<% String officeName = (String)request.getAttribute("officeName"); %>
	    <form:form action="/Office/filterRooms" method="post" modelAttribute="Office" class="mb-4">
        <div class="form-group">
            <label for="officeName">Office Name:</label>
            <select id="officeName" name="officeName" class="form-control">
                <%-- Office names will be populated dynamically via AJAX --%>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Filter</button>
    </form:form>
		<hr>
		<h2>Available Rooms:</h2>
			<div class="row">
				<% List <Room> rooms = (List<Room>) request.getAttribute("rooms"); 
         if (rooms!= null && rooms.size() > 0) { 
				int i =0 ; 
				while (i < rooms.size()) { %>
				<div class="col-md-4 mb-4">
					<div class="card">
						<div class="card-body">
							<h5 class="card-title"><%= rooms.get(i).getRoomName() %></h5>
							<p class="card-text">
								Availability:
								<%= rooms.get(i).isAvailable() %></p>
							<form:form action="/Book/bookDetails/${officeName}" method="post" modelAttribute="Room">
								<form:hidden path="roomName" id ="roomName"
									value="<%= rooms.get(i).getRoomName() %>" />
								<button type="submit" class="btn btn-success">Book Room</button>
								<button type="button" class="btn btn-success roomBookedList" value = <%= rooms.get(i).getRoomName() %> >Check Availability</button>
							</form:form>
						</div>
					</div>
				</div>
				<% if (i % 3 == 2 || i == rooms.size() - 1) { %>
			</div>
			<div class="row">
				<% } %>
				<%  i++;  } }  else{ %>
				<p>No rooms Available</p>
				<%} %>
			</div>
			
	</div>

		<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	<script>

	$(document).ready(function() {

		$.ajax({
            type: 'GET',
            url: '/Office/offices',
            success: function (response) {
                var offices = response;
                var options = '';
                for (var i = 0; i < offices.length; i++) {
                    options += '<option value="' + offices[i].officeName + '">' + offices[i].officeName + '</option>';
                }
                $('#officeName').html(options);
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });

		
		 $(document).on('click', '.roomBookedList', function() {
	        var roomName = $(this).val();
	        var officeName = $('#officeName').val();
	        var requestData = {
	    	        date : null
	    	   }

	        $.ajax({
	            type: 'POST',
	            url: 'http://localhost:8080/Room/getCalendarTimings/' + officeName + '/' + roomName,
	            contentType:'application/json',
	            data: JSON.stringify(requestData),
	            success: function(response, status, xhr) {
	            	if (response === "Success") {
	                    // Backend tasks succeeded, navigate to another page or perform further actions
	                    window.location.href = "http://localhost:8080/Room/RoomBookedList";
	                }
		         
	                console.log("Booked List was fetched ...");
	            },
	            error: function(response, status, xhr) {
		            
	                console.log("Unable to fetch the Booked List ...");
	            }
	        });
	    });
	});
	

	</script>
</body>
</html>
