<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Signup</title>
    <!-- Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* Custom CSS */
        body {
            background-color: #f8f9fa;
        }
        .container {
            max-width: 400px;
            margin: 0 auto;
            margin-top: 50px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Signup</h2>
        <form  method="post" id="signupForm">
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" class="form-control" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" class="form-control" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary">Signup</button>
        </form>
    </div>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        $("#signupForm").submit(function(event) {
            event.preventDefault(); // Prevent default form submission
            var formData = {
                userName: $("#username").val(),
                userEmail: $("#email").val(),
                userPassword: $("#password").val()
            };
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/User/signupUser",
                data: JSON.stringify(formData),
                success: function(response) {
                    window.location.replace("/loginForm"); // Redirect to login page after successful signup
                },
                error: function(xhr, status, error) {
                    console.error("Error:", error);
                }
            });
        });
    });
</script>
</body>
</html>
