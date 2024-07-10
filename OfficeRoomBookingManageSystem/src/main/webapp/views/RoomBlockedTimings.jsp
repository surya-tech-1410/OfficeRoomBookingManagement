<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Hourly Lines</title>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <style>
    /* Resetting default margin and padding */
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: Arial, sans-serif;
      font-size: 14px;
      background-color: #F0F0F0; /* Very light grey */
    }
    
    #office, #room {
    width: 200px; /* Adjust the width as needed */
    height: 40px; /* Adjust the height as needed */
    padding: 8px; /* Adjust padding as needed */
    font-size: 16px; /* Adjust font size as needed */
    border: 1px solid #ccc; /* Border style */
    border-radius: 5px; /* Rounded corners */
    margin-right: 10px; /* Margin between elements */
}

    .container {
      height: 100vh;
      position: relative;
      padding-top: 50px; /* Add padding to accommodate the filter */
    }

    /* Styling for the filter section */
    .filter-section {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      background-color: #fff; /* White background for the filter */
      z-index: 1000; /* Ensure the filter appears on top of other elements */
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Add shadow for depth */
      padding: 10px 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .filter-section h2 {
      font-size: 18px;
      margin-right: 20px;
    }

    /* Styling for the back button */
    .back-button {
      background-color: #007bff; /* Blue background color */
      color: #fff; /* White text color */
      border: none;
      padding: 10px 20px;
      cursor: pointer;
      border-radius: 5px;
      transition: background-color 0.3s;
    }

    .back-button:hover {
      background-color: #0056b3; /* Darker blue on hover */
    }

    /* Styling for the room and date filters */
    .filter {
      display: flex;
      align-items: center;
    }

    .filter select {
      margin-right: 10px;
      padding: 8px;
      font-size: 14px;
      border: 1px solid #ccc;
      border-radius: 5px;
    }

    /* Styling for hour lines */
    .hour-lines {
      position: absolute;
      top: 0;
      left: 100px;
      right: 0;
      bottom: 0;
    }

    .hour-line {
      position: absolute;
      width: calc(100% - 100px);
      border-bottom: 0.5px solid rgba(0, 0, 0, 0.3);
    }

    /* Styling for hour labels */
    .hour-label {
      position: absolute;
      top: 0;
      left: 0;
      width: 100px;
      text-align: right;
      padding-right: 5px;
      line-height: 1.5;
    }

    /* Styling for the shaded intervals */
    .shaded-interval {
      position: absolute;
      background-color: rgba(255, 0, 0, 0.3); /* Semi-transparent red color for shading */
      cursor: pointer; /* Change cursor to pointer on hover */
    }

    /* Styling for the vertical line */
    .vertical-line {
      position: absolute;
      background-color: #000; /* Black color for the vertical line */
      width: 1px; /* Width of the vertical line */
      height: 1440px; /* Adjusted height to match the height of the container minus 2px */
      top: 1px; /* Positioning the vertical line */
      left: 200px; /* Positioning it to the right of hour labels */
    }

    .avail {
      margin-left: 20px;
    }

    .size {
      padding-top: 5px;
      padding-bottom: 5px;
    }
  </style>
</head>
<body>
<div class="filter-section">
  <div class="filter">
    <input type="text" id="office" value="<%= session.getAttribute("officeName") %>" readonly> <!-- Office name is readonly -->
    <select name="room" id="room" >
      <!-- Options will be populated dynamically -->
    </select>
    <input type="date" id="date-filter" class="avail size">
    <button id="show-availability" class="back-button avail">Show Availability</button>
  </div>
  <h2>Hourly Schedule</h2>
  <button class="back-button" onclick="goBack()">Back</button>
</div>
<div class="container">
  <div class="hour-lines">
    <!-- Hour lines will be dynamically added here -->
  </div>
  <div class="vertical-line"></div>
  <!-- Shaded intervals -->
</div>

<script>
  // Function to create hour lines and labels dynamically
  function createHourLinesAndLabels() {
    const container = document.querySelector('.hour-lines');
    const hours = 24; // Total hours in a day
    
    for (let i = 0; i < hours; i++) {
      const hourLine = document.createElement('div');
      hourLine.classList.add('hour-line');
      hourLine.style.left = '100px';
      hourLine.style.top = (i * 60) + 'px'; // Each hour line is 60px apart
      container.appendChild(hourLine);
      
      const hourLabel = document.createElement('div');
      hourLabel.classList.add('hour-label');
      hourLabel.textContent = formatHour(i);
      hourLabel.style.top = (i * 60) + 'px'; // Position the label at the hour line
      container.appendChild(hourLabel);
    }
  }

  // Function to format hour in 24-hour format
  function formatHour(hour) {
    return ('0' + hour).slice(-2) + ':00';
  }

  // Function to calculate pixel values and shade interval
  function shadeInterval(startHour, startMinute, endHour, endMinute, meetingInfo) {
    const startPixel = (startHour * 60 + startMinute); // 4.5 pixels per minute
    const endPixel = (endHour * 60 + endMinute);
    const intervalHeight = endPixel - startPixel;
    
    const shadedInterval = document.createElement('div');
    shadedInterval.classList.add('shaded-interval');
    shadedInterval.style.top = startPixel + 'px';
    shadedInterval.style.left = '200px';
    shadedInterval.style.width = 'calc(100% - 100px)';
    shadedInterval.style.height = intervalHeight + 'px';
    
    const paragraph = document.createElement("p");
    paragraph.textContent = meetingInfo;
    
    shadedInterval.appendChild(paragraph);
    
    document.querySelector('.container').appendChild(shadedInterval);
  }

  // Call the function to create hour lines and labels
  createHourLinesAndLabels();

  // Define blockedTimings data as JSON
  var blockedTimings = ${blockedTimings};

  // Iterate over blockedTimings and call shadeInterval function
  for (var meetingName in blockedTimings) {
    if (blockedTimings.hasOwnProperty(meetingName)) {
      var timings = blockedTimings[meetingName];
      shadeInterval(parseInt(timings[0]), parseInt(timings[1]), parseInt(timings[2]), parseInt(timings[3]), timings[4]);
    }
  }

  // Function to go back to the previous page
  function goBack() {
    window.history.back();
  }

  $(document).ready(function() {
    var officeName = $('#office').val();
    console.log(officeName)
     // Store the initial roomName
   
      var initialRoomName = '<%= session.getAttribute("roomName") %>'; // Store the initial roomName
      var initialDate = '<%= session.getAttribute("date") %>'; // Store the initial date
      console.log(initialDate);  
      $('#date-filter').val(initialDate);
       console.log(initialRoomName);
       
    // Function to populate rooms based on the selected office
    function populateRooms(officeName) {
      $.ajax({
        url: 'http://localhost:8080/Office/rooms/' + officeName, // Adjust the URL as needed
        method: 'GET',
        success: function(rooms) {
          $('#room').empty(); // Clear previous options
          rooms.forEach(function(room) {
            var option = $('<option></option>')
              .text(room.roomName)
              .val(room.roomName); // Assuming roomName is the value to be sent
            $('#room').append(option);
            $('#room').val(initialRoomName);
          });
        },
        error: function(error) {
          console.error('Error fetching rooms:', error);
        }
      });
    }

    // Event listener for changing the room selection
    $('#room').change(function() {
        // Clear the date input when selecting another roomName
        if ($('#room').val() !== initialRoomName) {
            $('#date-filter').val('');
        }
    });

    // Populate rooms initially
    populateRooms(officeName);

    // Event listener for the "Show Availability" button
    $('#show-availability').click(function() {
      var roomName = $('#room').val();
      var date = $('#date-filter').val();
      

      // Check if date is empty
      if (date.trim() === "") {
        alert("Please provide a valid date.");
        return; // Exit the function
      }

      // Data to be sent in the request
      var requestData = {
        officeName: officeName,
        roomName: roomName,
        date: date
      };

      // AJAX request
      $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/Room/getCalendarTimings/' + officeName + '/' + roomName,
        contentType: 'application/json',
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
