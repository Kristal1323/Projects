<?php
    header('Content-Type: application/json');

    function book($firstName, $lastName, $emailAddress, $phone, $roomType, $ticketType, $creditCardType, $creditCardNo, $cVV) {
        //TODO save booking information to database
        $conn = mysqli_connect("mysql", "phpservice", "phpservice", "yesteryears");

        if (!$conn) {
            die("Connection failed: " . mysqli_connect_error());
        }

        $intRoomType = (int)$roomType;
        $intTicketType = (int)$ticketType;
        $intCreditCardType = (int)$creditCardType;

        mysqli_query($conn, "BEGIN");


        $sql = "INSERT INTO customers (firstname, lastname, emailaddress, phone)
        VALUES ('$firstName', '$lastName', '$emailAddress', '$phone')";
        
        if (!mysqli_query($conn, $sql)) {
            die("Error: " . $sql . "<br>" . mysqli_error($conn));
        }
        $customer_id = mysqli_insert_id($conn); 

        $sql = "INSERT INTO bookings (customer_id, room_id, ticket_id, creditcard_id, cardnumber, cvv)
        VALUES ($customer_id, $intRoomType, $intTicketType, $intCreditCardType, '$creditCardNo', '$cVV')";
        
        if (!mysqli_query($conn, $sql)) {
            die("Error: " . $sql . "<br>" . mysqli_error($conn));
            mysqli_query($conn, "ROLLBACK");
        }

          mysqli_query($conn, "COMMIT");

          mysqli_close($conn);
    
        return $firstName . $lastName;
    }

    function email($firstName, $lastName, $emailAddress, $phone, $enquiry, $emailDescription) {
        $conn = mysqli_connect("mysql", "phpservice", "phpservice", "yesteryears");

        if (!$conn) {
            die("Connection failed: " . mysqli_connect_error());
        }

        mysqli_query($conn, "BEGIN");

        $sql = "INSERT INTO customers (firstname, lastname, emailaddress, phone)
        VALUES ('$firstName', '$lastName', '$emailAddress', '$phone')";

        if (!mysqli_query($conn, $sql)) {
            die("Error: " . $sql . "<br>" . mysqli_error($conn));
        }
        $customer_id = mysqli_insert_id($conn); 

        $sql = "INSERT INTO emails (customer_id, enquiry, email_description)
        VALUES ($customer_id, '$enquiry', '$emailDescription')";

        if (!mysqli_query($conn, $sql)) {
            die("Error: " . $sql . "<br>" . mysqli_error($conn));
            mysqli_query($conn, "ROLLBACK");
        }

        mysqli_query($conn, "COMMIT");

        mysqli_close($conn);

        
        return $firstName . $lastName;
    }

    $aResult = array();

    if( !isset($_POST['functionname']) ) { $aResult['error'] = 'No function name!'; }

    if( !isset($_POST['arguments']) ) { $aResult['error'] = 'No function arguments!'; }

    if( !isset($aResult['error']) ) {

        switch($_POST['functionname']) {
            case 'book':
               if( !is_array($_POST['arguments']) || (count($_POST['arguments']) < 2) ) {
                   $aResult['error'] = 'Error in arguments!';
               }
               else {
                   $firstName = $_POST['arguments'][0];
                   $lastName = $_POST['arguments'][1];
                   $emailAddress = $_POST['arguments'][2];
                   $phone = $_POST['arguments'][3];
                   $roomType = $_POST['arguments'][4];
                   $ticketType = $_POST['arguments'][5];
                   $creditCardType = $_POST['arguments'][6];
                   $creditCardNo = $_POST['arguments'][7];
                   $cVV = $_POST['arguments'][8];
                   $aResult['result'] = book($firstName, $lastName, $emailAddress, $phone, $roomType, $ticketType, $creditCardType, $creditCardNo, $cVV);
               }
               break; 
            case 'email':
                if( !is_array($_POST['arguments']) || (count($_POST['arguments']) < 2) ) {
                    $aResult['error'] = 'Error in arguments!';
                }
                else {
                    $firstName = $_POST['arguments'][0];
                    $lastName = $_POST['arguments'][1];
                    $emailAddress = $_POST['arguments'][2];
                    $phone = $_POST['arguments'][3];
                    $enquiry = $_POST['arguments'][4];
                    $emailDescription = $_POST['arguments'][5];
                    $aResult['result'] = email($firstName, $lastName, $emailAddress, $phone, $enquiry, $emailDescription);
                }
                break; 
            default:
               $aResult['error'] = 'Not found function '.$_POST['functionname'].'!';
               break;
        }

    }

    echo json_encode($aResult);

?>