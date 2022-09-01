document.addEventListener("readystatechange", function(event) {
	if(event.target.readyState == "interactive") {
        
        document.querySelector("body").classList.add("js");
		
		// Email Form Validation
		const formButton = document.querySelector("#contact-us form button");

		if(document.querySelector("#contact-us form") !== null) {
			document.querySelector("#contact-us form").addEventListener("submit", function(event) {
				let firstName = document.querySelector("#fname").value;
				let lastName = document.querySelector("#lname").value;
				let emailAddress = document.querySelector("#email").value;
				let complete = false;
	
				if(firstName != "" && lastName != "" && emailAddress != "" && validateEmail(emailAddress)) {
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
	
	
				if(complete) {
					let emailText = document.getElementById("contact-text");
					let phone = document.getElementById("phone").value;
					let enquiry = document.getElementById("question").value;
					let emailDescription = document.getElementById("description").value;
					


					jQuery.ajax({
						type: "POST",
						url: 'process.php',
						dataType: 'json',
						data: {functionname: 'email', arguments: [firstName, lastName, emailAddress, phone, enquiry, emailDescription]},
					
						success: function (obj, textstatus) {
									if( !('error' in obj) ) {
										fullName = obj.result;
										emailText.innerText = "Thank you for your email " + fullName + ".  We will get back to you shortly."
									}
									else {
										console.log(obj.error);
									}
								}
					});
				}
	
				event.preventDefault();
	
			});
	
			let formFields = document.querySelectorAll("#fname, #lname, #email");
			for(let formField of formFields) {
				formField.addEventListener("keydown", function() {
					this.classList.remove("error");
					this.closest(".form-item").querySelector("label").classList.remove("error");
				});
			}
			
		}

		// FAQ View More 
		let faqViewButtons = document.querySelectorAll("#faq article .answers-button a");
		for(let button of faqViewButtons) {
			button.addEventListener("click", function(event) {
				this.parentElement.parentElement.classList.add("view");
				event.preventDefault();
			});
		}
	
	
    }
});

function validateEmail(email) {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}