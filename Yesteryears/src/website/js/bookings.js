document.addEventListener("readystatechange", function(event) {
	if(event.target.readyState == "interactive") {
        
        document.querySelector("body").classList.add("js");

        //booking popup
		let bookButtons = document.querySelectorAll("#book-rooms .view-button a, #book-rooms-popup .close-button a");
		for(let button of bookButtons) {
			button.addEventListener("click", function(event) {
				let popupImage = document.getElementById("popup-image");
				let popupHeader = document.getElementById("popup-header");
				let popupPrice = document.getElementById("popup-price");
				let popupMaxGuests = document.getElementById("popup-max-guests");
				if (button.id === "prehistory")
				{
					popupHeader.innerText = "Prehistoric Era";
					popupImage.src = "images & files/prehistory_room.jpg"; 
					popupPrice.innerText = "$177.00";
					popupMaxGuests.innerText = "Maximun: 2 guests";
				}
				if (button.id === "classical")
				{
					popupHeader.innerText = "Classical Era";
					popupImage.src = "images & files/classical_room.jpeg";
					popupPrice.innerText = "$377.00";
					popupMaxGuests.innerText = "Maximun: 5 guests";
				}
				if (button.id === "middle-ages")
				{
					popupHeader.innerText = "The Middle Ages";
					popupImage.src = "images & files/middle_ages_room.jpg";
					popupPrice.innerText = "$277.00";
					popupMaxGuests.innerText = "Maximun: 3 guests";
				}
				if (button.id === "early-modern")
				{
					popupHeader.innerText = "Early Modern Era";
					popupImage.src = "images & files/early_modern_room.jpg";
					popupPrice.innerText = "$277.00";
					popupMaxGuests.innerText = "Maximun: 3 guests";
				}
				if (button.id === "modern")
				{
					popupHeader.innerText = "Modern Era";
					popupImage.src = "images & files/modern_room.jpg";
					popupPrice.innerText = "$377.00";
					popupMaxGuests.innerText = "Maximun: 5 guests";
				}
				document.body.classList.toggle("show-book-rooms-popup");
				event.preventDefault();
			});
		}

		// Booking Form Validation
		const formButton = document.querySelector("#bookings-tickets form button");

		if(document.querySelector("#bookings-tickets form") !== null) {
			document.querySelector("#bookings-tickets form").addEventListener("submit", function(event) {
				let firstName = document.querySelector("#fname").value;
				let lastName = document.querySelector("#lname").value;
				let emailAddress = document.querySelector("#email").value;
				let phone = document.querySelector("#phone").value;
				let creditCardNo = document.querySelector("#credit-card-no").value;
				let cVV = document.querySelector("#cvv").value;
				let complete = false;
	
				if(firstName != "" 
					&& lastName != "" 
					&& emailAddress != "" 
					&& validateEmail(emailAddress) 
					&& phone != "" && !isNaN(phone)
					&& creditCardNo != "" && !isNaN(creditCardNo)
					&& cVV != "" && !isNaN(cVV)) {

					complete = true;
				}
	
				if(firstName == "") {
					let formFirstName = document.querySelector("#fname");
					formFirstName.classList.add("error");
					let formFirstNameLabel = formFirstName.closest(".form-item").querySelector("label");
					formFirstNameLabel.classList.add("error");
				}
	
				if(lastName == "") {
					let formLastName = document.querySelector("#lname");
					formLastName.classList.add("error");
					let formLastNameLabel = formLastName.closest(".form-item").querySelector("label");
					formLastNameLabel.classList.add("error");
				}
	
				if(emailAddress == "" || !validateEmail(emailAddress)) {
					let formEmailAddress = document.querySelector("#email");
					formEmailAddress.classList.add("error");
					let formEmailAddressLabel = formEmailAddress.closest(".form-item").querySelector("label");
					formEmailAddressLabel.classList.add("error");
				}

				if(phone == "" || isNaN(phone)) {
					let formPhone = document.querySelector("#phone");
					formPhone.classList.add("error");
					let formPhoneLabel = formPhone.closest(".form-item").querySelector("label");
					formPhoneLabel.classList.add("error");
				}

				if(creditCardNo == "" || isNaN(creditCardNo)) {
					let formcreditCardNo = document.querySelector("#credit-card-no");
					formcreditCardNo.classList.add("error");
					let formCreditCardNoLabel = formcreditCardNo.closest(".form-item").querySelector("label");
					formCreditCardNoLabel.classList.add("error");
				}

				if(cVV == "" || isNaN(cVV)) {
					let formCVV = document.querySelector("#cvv");
					formCVV.classList.add("error");
					let formCVVLabel = formCVV.closest(".form-item").querySelector("label");
					formCVVLabel.classList.add("error");
				}
	
				if(complete) {
					let bookingText = document.getElementById("booking-text");
					let roomTypeElement = document.getElementById("room-types");
					let roomType = roomTypeElement.value;

					let ticketTypeElement = document.getElementById("ticket-types");
					let ticketType = ticketTypeElement.value;

					let creditCardTypeElement = document.getElementById("credit-card-types");
					let creditCardType = creditCardTypeElement.value;

					jQuery.ajax({
						type: "POST",
						url: 'process.php',
						dataType: 'json',
						data: {functionname: 'book', arguments: [firstName, lastName, emailAddress, phone, roomType, ticketType, creditCardType, creditCardNo, cVV]},
					
						success: function (obj, textstatus) {
									if( !('error' in obj) ) {
										fullName = obj.result;
										bookingText.innerText = "Thank you for booking with Yesteryears " + fullName + "! We've emailed you the receipt."
									}
									else {
										console.log(obj.error);
									}
								},
						error: function(obj, status) {
							console.log(obj)
						}
					});
				}
	
				event.preventDefault();
	
			});
	
			let formFields = document.querySelectorAll("#fname, #lname, #email, #phone, #credit-card-no, #cvv");
			for(let formField of formFields) {
				formField.addEventListener("keydown", function() {
					this.classList.remove("error");
					this.closest(".form-item").querySelector("label").classList.remove("error");
				});
			}
			
		}
        
    }
});

function validateEmail(email) {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}