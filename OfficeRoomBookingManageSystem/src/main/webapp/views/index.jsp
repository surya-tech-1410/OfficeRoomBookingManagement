<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.learn.Spring.model.Booking"%>
<%@ page import="com.learn.Spring.model.User"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<!DOCTYPE html>
<html>
<head>
    <title>Office Room Booking</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.6.0/css/bootstrap.min.css">
    <style>
        /* Your CSS styles */
        body {
            background-image: url("http://localhost:8080/img/office.jpeg");
            /* Replace with your desired background image */
            background-size: cover;
            background-position: center;
            color: #ffffff;
        }

        .container {
            margin-top: 100px;
        }

        .form-wrapper {
            position: relative;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
            padding: 20px;
            border-radius: 10px;
            background-color: rgba(0, 0, 0, 0.5);
        }

        h3 {
            text-align: center;
            margin-bottom: 30px;
        }

        .form-group {
            margin-bottom: 25px;
        }

        label {
            font-weight: bold;
        }

        .participants-list {
            list-style: none;
            padding: 0;
        }

        .participant-item {
            display: inline-block;
            background-color: #ffffff;
            color: #000000;
            padding: 5px;
            border-radius: 5px;
            margin-right: 5px;
            position: relative;
        }

        .participant-item .remove-participant {
            position: absolute;
            top: -5px;
            right: -5px;
            background-color: #ff0000;
            color: #ffffff;
            padding: 2px;
            border-radius: 50%;
            font-size: 12px;
            cursor: pointer;
        }

        .book-room-btn {
            position: absolute;
            bottom: -60px;
            left: 50%;
            transform: translateX(-50%);
        }
    </style>
</head>
<body>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <a href="/home" class="btn btn-primary mb-3">Return to Home</a>
            <div class="form-wrapper">
                <h3>Get Your Meeting Room</h3>
                
                <%
                
                
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String currentUserId = null;

                // Check if the user is authenticated
                if (authentication != null && authentication.isAuthenticated()) {
                    // Get the username or any other user details as needed
                     currentUserId = authentication.getName(); // Assuming username is used as user identifier
                     
                   }
           

                
                String officeName = (String) request.getAttribute("officeName");
                String roomName = (String) request.getAttribute("roomName");
                Booking booking = (Booking) request.getAttribute("booking");
                boolean isEdit = (booking != null);
                %>
                
                <form id="bookingForm">
                    <div class="form-group">
                        <label for="office">Office:</label>
                        <input type="text" class="form-control" id="office" name="office" readonly value="<%=officeName%>">
                    </div>

                    <div class="form-group">
                        <label for="room">Room:</label>
                        <input type="text" class="form-control" id="room" name="room" readonly value="<%=roomName%>">
                    </div>

                    <% if(isEdit) { %>
                        <input type="hidden" id="bookingId" name="bookId" value="<%=booking.getBookId()%>">
                    <% } %>

                    <div class="form-group">
                        <label for="subject">Subject:</label>
                        <input type="text" class="form-control" id="subject" name="subject" <% if(isEdit) { %> value="<%=booking.getSubject()%>" <% } %> >
                    </div>

                    <div class="form-group">
                        <label for="summary">Summary:</label>
                        <input type="text" class="form-control" id="summary" name="summary" <% if(isEdit) { %> value="<%=booking.getSummary()%>" <% } %> >
                    </div>

                    <div class="form-group">
                        <label for="startTime">Start Time:</label>
                        <input type="datetime-local" class="form-control" id="startTime" name="startTime" <% if(isEdit) { %> value="<%=booking.getStartTime()%>" <% } %> >
                    </div>

                    <div class="form-group">
                        <label for="endTime">End Time:</label>
                        <input type="datetime-local" class="form-control" id="endTime" name="endTime" <% if(isEdit) { %> value="<%=booking.getEndTime()%>" <% } %> >
                    </div>

				                 <div class="form-group" <% if(isEdit) { %> style="display: none;" <% } %>>
							    <label for="participants">Participants:</label>
							    <select class="form-control" name="participants" id="participants">
							        <% if(isEdit) {
							            List<User> participants = booking.getUsers();
							            for(User participant : participants) { %>
							                <option value="<%=participant.getUserId()%>" selected><%=participant.getUserName()%></option>
							        <%  }
							            } %>
							    </select>
				    <small>Select a participant and click 'Add' to include them</small> <br>
				    <ul class="participants-list" id="participantsList"></ul>
				</div>
                    <div class="form-group"  <% if(isEdit) { %> style="display: none;" <% } %>>
                        <button type="button" class="btn btn-primary" id="addParticipantBtn">Add</button>
                    </div>

                    <button type="submit" class="btn btn-success book-room-btn">
                        <% if(isEdit) { %>
                            Update Meet
                        <% } else { %>
                            Book Room
                        <% } %>
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.6.0/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function() {
        $.ajax({
            url: "http://localhost:8080/User/users",
            method: 'GET',
            success: function(users) {
                // Populate the participants dropdown with retrieved user names and emails
                $('#participants').empty(); // Clear previous options
                users.forEach(function(user) {
                    var option = $('<option></option>')
                        .attr('value', user.userEmail)
                        .text(user.userName + ' (' + user.userEmail + ')');
                    $('#participants').append(option);
                });
            },
            error: function(error) {
                console.error('Error fetching users:', error);
            }
        });

        var participantsList = [];

        $('#addParticipantBtn').click(function() {
            var selectedParticipant = $('#participants option:selected');
            var participantValue = selectedParticipant.val();
            var participantText = selectedParticipant.text();

            if (participantValue !== '') {
                participantsList.push({
                    userEmail: participantValue
                });
                var participantItem = $('<li class="participant-item"></li>').text(participantText);
                var removeBtn = $('<span class="remove-participant">&times;</span>');

                removeBtn.click(function() {
                    var index = participantsList.findIndex(function(item) {
                        return item.userId === participantValue;
                    });

                    if (index !== -1) {
                        participantsList.splice(index, 1);
                    }

                    participantItem.remove();
                });

                participantItem.append(removeBtn);
                $('#participantsList').append(participantItem);
            }
        });

        $('#bookingForm').submit(function(e) {
            // console.log("checker");
            e.preventDefault();

            var formData = {
                room: {
                    roomName: $('#room').val()
                },
                users: participantsList,
                subject: $('#subject').val(),
                summary: $('#summary').val(),
                startTime: $('#startTime').val(),
                bookId : $('#bookingId').val(),
                endTime: $('#endTime').val(),
                user: {
                    userEmail:"<%=currentUserId %>"  // will work it soon......
                }

            };

          

            //  console.log(formData);

            var OfficeName = $('#office').val()

            console.log("officeName : ", OfficeName);

            var url = '/Book/' + (<%= isEdit %> ? 'EditBooking' : 'bookRoom/' + OfficeName);

            $.ajax({
                type: 'POST',
                url: url,
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function(response, status, xhr) {
                    console.log(response);
                    console.log(status);
                    window.location.href = response;
                },
                error: function(error, status, xhr) {
                    console.log(error.responseText);

                    if(error.responseText.includes("Timings are overlapping")){
                        if(confirm("You will be in waitlist for this timings")){

                        	   var formData = {
                                       room: {
                                           roomName: $('#room').val()
                                       },
                                       startTime: $('#startTime').val(),
                                       endTime: $('#endTime').val(),
                                       user: {
                                           userEmail: "<%= currentUserId %>"
                                       }

                                   };
                        	 $.ajax({
                                 url: '/Waitlist/bookRoom/'+ OfficeName,
                                 method: 'POST',
                                 contentType: 'application/json',
                                 data: JSON.stringify(formData),
                                 success: function(response,status,xhr) {
                                	 window.location.href = "http://localhost:8080/home";
                                
                                 },
                                 error: function(error,status,xhr) {
                                     alert(error.responseText);
                                     window.location.href = "http://localhost:8080/home";
                                     console.error('Error fetching users:', error);
                                 }
                             });
                            
                        }

                        else{
                        	window.location.href = "http://localhost:8080/";
                           }
                   }
                    
                }
            });
        });
    });
</script>
</body>
</html>
