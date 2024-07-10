<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.learn.Spring.model.Booking" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.web.servlet.support.RequestContext"%>
<%@ page import="org.springframework.web.servlet.tags.form.FormTag"%>
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Booking List</title>
  <!-- Bootstrap CSS -->
  <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
  <style>
    /* Custom CSS for shading */
    .waitlist {
      background-color: rgba(255, 255, 0, 0.3);
    }

    /* Custom CSS for div with shading at borders */
    .custom-border {
      margin-top: 20px;
      border: 1px solid #ccc;
      border-radius: 10px;
      padding: 20px;
      box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.2);
      background-color: #f0f7ff; /* Light blue background color */
    }

    /* Custom CSS for background image */
    body {
      background-image: url("http://localhost:8080/img/office.jpeg");
      background-size: cover;
      background-repeat: no-repeat;
      background-attachment: fixed;
    }

    /* Custom CSS for table */
    table {
      width: 100%;
      border-collapse: collapse;
    }

    th, td {
      padding: 12px;
      text-align: left;
      border-bottom: 1px solid #ddd;
    }

    th {
      background-color: #f2f2f2; /* Light gray background color */
    }

    .btn {
      padding: 8px 16px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }

    .btn-primary {
      background-color: #007bff; /* Blue */
      color: white;
    }

    .btn-danger {
      background-color: #dc3545; /* Red */
      color: white;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="custom-border">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="d-inline-block mr-3">Booking List</h2>
            <a href="/home" class="btn btn-primary btn-sm">Back</a>
        </div>
      <table id="bookingTable">
        <thead>
          <tr>
            <th>S. No</th>
            <th>Meeting Info</th>
            <th>Start Time</th>
            <th>End Time</th>
              <th>Status</th>
            <th>Edit</th>
            <th>Cancel</th>
          </tr>
        </thead>
        <tbody>
          <% 
            // Retrieve list of booking objects from controller
            List<Booking> bookingList = (List<Booking>) request.getAttribute("bookingList");
            int count = 1;
            // Iterate over the list and populate table rows
            if (bookingList != null && !bookingList.isEmpty()) {
                for (int i = 0; i < bookingList.size(); i++) {
                    Booking booking = bookingList.get(i);
          %>
          <tr>
            <td><%= count++ %></td>
            <td><%= booking.getSubject() %></td>
            <td><%= booking.getStartTime() %></td>
            <td><%= booking.getEndTime() %></td>
            <td><%= booking.getStatus().getBooking_status() %></td>
            <td>
			<form:form action="/Book/edit/details" method="post" modelAttribute="Booking">
			    <form:hidden path="bookId" id="bookId" value="<%= booking.getBookId() %>"/>
			    <button type="submit" class="btn btn-primary">Edit</button>
			</form:form>
            </td>
            <td>
              <button type="button" class="btn btn-danger cancel-booking-btn" data-book-id="<%= booking.getBookId() %>">Cancel</button>
            </td>
          </tr>
          <% 
                }
            }
          %>
        </tbody>
      </table>
    </div>
  </div>

  <!-- jQuery -->
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script>
    $(document).ready(function() {
        $(".cancel-booking-btn").click(function() {
            var bookId = $(this).data("book-id");
            $.ajax({
                type: "POST",
                url: "/Book/cancel/DeleteBooking",
                data: { bookId: bookId },
                success: function(response,status) {
                    console.log("Booking canceled successfully");
                    console.log(response);
                    console.log(status);
                    window.location.href = response;
                    // You can update the UI or perform any other actions here
                },
                error: function(xhr, status, error) {
                    console.error("Error canceling booking:", error);
                    // You can display an error message or handle the error accordingly
                }
            });
        });
    });
  </script>
</body>
</html>
