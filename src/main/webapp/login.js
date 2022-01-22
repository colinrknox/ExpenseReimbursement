"use strict";

const AWS_SERVER = "18.223.187.100:8080";
const LOCAL_HOST = "localhost:9001";

// CHANGE THIS FOR DIFFERENT DEPLOYMENT ENVIRONMENTS
let CURRENT_SERVER = LOCAL_HOST;

window.onload = function (event) {
	document.getElementById("loginButton").onclick = loginButtonHandler;
	document.getElementById("loginPassword").onkeyup = textBoxEnterHandler;
	document.getElementById("loginUser").onkeyup = textBoxEnterHandler;

	if (sessionStorage['userFirstName']) {
		window.location = `http://${CURRENT_SERVER}/ExpenseReimbursement/home.html`;
	}
}

// Handler for hitting enter in the password text box
function textBoxEnterHandler(event) {
	if (event.keyCode == 13) {
		document.getElementById("loginButton").click();
	}
}

/**
 * Login AJAX request
 * @param {Event} event 
 */
function loginButtonHandler(event) {
	let userBox = document.getElementById("loginUser");
	let passBox = document.getElementById("loginPassword");

	let xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function () {
		if (xhttp.readyState == XMLHttpRequest.DONE && xhttp.status == 200) {
			// put user's id and name into the session
			let serverResponse = JSON.parse(xhttp.responseText);
			if (serverResponse.userFirstName && serverResponse.currentUser && serverResponse.userRole) {
				console.log(serverResponse);
				sessionStorage['userFirstName'] = serverResponse.userFirstName;
				sessionStorage['currentUser'] = serverResponse.currentUser;
				sessionStorage['userRole'] = serverResponse.userRole;

				window.location = `http://${CURRENT_SERVER}/ExpenseReimbursement/home.html`;
			} else {
				let loginFail = document.getElementById('loginFail');
				loginFail.classList.remove('d-none');
			}
		}
	}

	xhttp.open('POST', `http://${CURRENT_SERVER}/ExpenseReimbursement/login`);
	xhttp.send(JSON.stringify({
		"username": userBox.value,
		"password": passBox.value
	}));
}


