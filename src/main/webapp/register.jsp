<%@page import="java.awt.dnd.DropTargetAdapter"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!--external link for font awesome-->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <!--css file link-->
    <link rel="stylesheet" href="css/reg.css">
    <title>Cloth</title>
</head>
<body>

   <div class="reg_container">

    <form action="RegisterServlet" method="post" name="emp" 
      method="post" ENCTYPE="multipart/form-data">
        <h1>Fill Out The Registration From</h1>
        <!--firstname-->
        <div class="inputidv1">
            <i class="fa fa-cloud-upload"><br><h6 style="font-weight: lighter;">Upload an image</h6></i>
        </div>
        <div class="formdiv">
            <div class="inputidv">
                <label for="fname">First-Name</label>           
               <input type="text" placeholder="Enter your first-name" name="firstname" required>
            </div>
 
        <!--lastname-->

            <div class="inputidv">
                <label for="lname">Last-Name</label>
                <input type="text" placeholder="Enter your last-name" name="lastname" required>
            </div>

        <!--address-->
     
            <div class="inputidv">
                <label for="address">Address</label>
                <input type="text" placeholder="Enter your address" name="address" required>
            </div>
   
        <!--phonenumber-->
   
            <div class="inputidv">
                <label for="phn">Phone-Number</label>
                <input type="tel" placeholder="Enter your phone-number" name="phone_number" required>
            </div>
 
        <!--email-->
    
            <div class="inputidv">
                <label for="email">E-Mail</label>
                <input type="email" placeholder="Enter your email" name="email" required>
            </div>

        <!--password-->

            <div class="inputidv">
                <label for="password">Password</label>
                <input type="password" placeholder="Enter your passowrd" name="password" required>
            </div>
    </div>
    <div class="conform">
         <!--confirmation message-->
         <p style="font-size: 15px;  color: rgb(12, 12, 0);">By submiting the form and clicking the Sign Up,<BR>
            you agree to our Terms and Conditions also accept the use of cookies.</p>
    </div>
       
    <div class="btn">
        <button type="submit">SIGN UP</button>
    </div>
    </form>
   </div>


</body>
</html>