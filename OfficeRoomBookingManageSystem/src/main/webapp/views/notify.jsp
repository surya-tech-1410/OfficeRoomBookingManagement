<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Redirect Example</title>
</head>
<body>
    <!-- Content for the first page -->
    <div id="content1">
        <h1>Welcome to Page 1</h1>
        <p>This page will redirect to another page after 5 seconds.</p>
    </div>

    <!-- Divs for success and error messages (initially hidden) -->
    <div id="successMessage" style="display: none; color: green;">
        <h2>Success!</h2>
        <p>Your request was successful.</p>
        <p id="successMessageContent"></p>
    </div>

    <div id="errorMessage" style="display: none; color: red;">
        <h2>Error!</h2>
        <p>There was an error processing your request.</p>
        <p id="errorMessageContent"></p>
    </div>

    <script>
        // Retrieve status code and message passed from the server-side
        
       
        
        var statusCode = "208";
        var message = "<%= "sucessfully integrated the page..." %>";

        // Convert statusCode to integer for comparison
        statusCode = parseInt(statusCode);

        // Check if the status code indicates success
        if (statusCode === 200 || statusCode === 201 || statusCode === 301) {
            document.getElementById("successMessage").style.display = "block";
            document.getElementById("successMessageContent").innerText = message;
        } else {
            document.getElementById("errorMessage").style.display = "block";
            document.getElementById("errorMessageContent").innerText = "Error: " + message;
        }

        // Redirect to the home page after 5 seconds (replace 'http://localhost:8080' with the actual URL)
        setTimeout(function() {
            window.location.href = "http://localhost:8080/login/notification/google";
        }, 5000); // 5000 milliseconds = 5 seconds
    </script>
</body>
</html>
