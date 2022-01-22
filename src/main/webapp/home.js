
/**
 * TODO when window loads -
 * 1. Load all expense reports for the index table
 * 2. Set name to welcome
 * 3. Add navbar click listeners
 */

 const AWS_SERVER = "18.223.187.100:8080";
 const LOCAL_HOST = "localhost:9001";

 // CHANGE THIS FOR DIFFERENT DEPLOYMENT ENVIRONMENTS
 let CURRENT_SERVER = LOCAL_HOST;

window.onhashchange = (event) => {
    if (!sessionStorage['currentUser']) {
        location.href = `http://${CURRENT_SERVER}/ExpenseReimbursement`;
    }
}

window.onload = (event) => {
    if (!sessionStorage['currentUser']) {
        location.href = `http://${CURRENT_SERVER}/ExpenseReimbursement`;
    }
    loadReimbursementExpenses();
    document.getElementById("logout").onclick = handleLogout;
    document.getElementById('composeButton').onclick = composeButtonHandler;
    document.getElementById('acceptButton').onclick = acceptButtonHandler;
    document.getElementById('denyButton').onclick = denyButtonHandler;
    document.getElementById('statusType').onchange = filterTable;
    loadNameWelcome();
    enableCompose();
    enableStatusFilter();
}

function filterTable(event) {
    let statusType = document.getElementById('statusType');
    let rows = document.getElementById('expenseData').getElementsByTagName('tr');

    Array.from(rows).forEach(function(ele, index, arr) {
        if (statusType.value && ele.children[2].innerText != statusType.value) {
            ele.classList.add('d-none');
        } else {
            ele.classList.remove('d-none');
        }
    });
}

function enableStatusFilter() {
    if (sessionStorage['userRole'] != 'employee') {
        document.getElementById('statusType').classList.remove('d-none');
    }
}

// Loads welcome header with first name
function loadNameWelcome() {
    let header = document.getElementById("nameWelcome");
    header.innerText = `Welcome ${sessionStorage['userFirstName']}`
}

/**
 * Asynchrounous loader function for the reimbursements attached to the account
 * Also adds an onclick listener for each row
 */
async function loadReimbursementExpenses() {
    let response = await fetch(`http://${CURRENT_SERVER}/ExpenseReimbursement/api/allexpenses`)
        .then(resp => resp.json())
        .then(data => { buildExpenseTable(data); });
}

// Builds the expense table "table of contents" with all available expenses to the user
function buildExpenseTable(data) {
    let tbody = document.getElementById("expenseData");
    tbody.innerHTML = '';
    data.map((val, index, arr) => {
        let trow = document.createElement("tr");
        trow.classList.add("clickable-row");
        trow.onclick = handleRowClick;
        let colId = document.createElement("th");
        let colAmount = document.createElement("td");
        let colStatus = document.createElement("td");


        colId.innerText = val.id;
        colAmount.innerText = val.amount;
        colStatus.innerText = val.status.status;

        trow.appendChild(colId);
        trow.appendChild(colAmount);
        trow.appendChild(colStatus);
        tbody.appendChild(trow);
    });
}

// Navbar button listener for logout
function handleLogout() {
    console.log("Handling logout");
    sessionStorage.clear();
    location.href = `http://${CURRENT_SERVER}/ExpenseReimbursement/logout`;
}

// Callback for all the rows in the "table of contents"
function handleRowClick(event) {
    // Hide form for submitting if present
    composeFormHide();

    expenseViewShow();

    let parentRow = event.target.parentNode;
    let idChild = parentRow.firstChild.innerText;
    let postObj = {
        'id': idChild
    }
    loadSpecificExpense(postObj);
}

/**
 * Loads a specific expense object given the id
 * @param {javascript object with one property id: idNumber} expenseId 
 */
async function loadSpecificExpense(expenseId) {
    let response = await fetch(`http://${CURRENT_SERVER}/ExpenseReimbursement/api/expensedetail`, {
        method: 'POST',
        body: JSON.stringify(expenseId)
    })
        .then(resp => resp.json())
        .then(data => { loadExpenseView(data); });
}

function loadExpenseView(reimbObject) {
    console.log(reimbObject);
    sessionStorage['currentExpense'] = JSON.stringify(reimbObject);

    if (sessionStorage['userRole'] != 'employee' && reimbObject.status.status == 'pending') {
        enableButtons();
    } else {
        disableButtons();
    }

    let expenseRow = document.getElementById("expenseView");
    expenseRow.innerHTML = '';
    let expenseTable = document.createElement('table');

    let tableHeader = document.createElement('thead');
    let tHeaderProp = document.createElement('th');
    tHeaderProp.innerText = 'Properties';
    let tHeaderVal = document.createElement('th');
    tHeaderVal.innerText = 'Values';
    tableHeader.appendChild(tHeaderProp);
    tableHeader.appendChild(tHeaderVal);

    let tableBody = document.createElement('tbody');
    expenseTable.classList.add('table', 'table-hover', 'table-responsive', 'table-bordered', 'm-lg-1');

    for (property in reimbObject) {
        let value = reimbObject[property];
        let trObject = document.createElement('tr');
        let tdObjectProp = document.createElement('td');
        let tdObjectVal = document.createElement('td');
        if (property == 'author' && value) {
            tdObjectProp.innerText = 'Author Name';
            tdObjectVal.innerText = `${value.firstName} ${value.lastName}`;
        } else if (property == 'resolver' && value) {
            tdObjectProp.innerText = 'Resolver Name';
            tdObjectVal.innerText = `${value.firstName} ${value.lastName}`;
        } else if (property == 'status') {
            tdObjectProp.innerText = 'Status';
            tdObjectVal.innerText = `${value.status}`;
        } else if (property == 'type') {
            tdObjectProp.innerText = 'Expense Type';
            tdObjectVal.innerText = `${value.type}`;
        } else if (property == 'id') {
            tdObjectProp.innerText = 'Expense ID';
            tdObjectVal.innerText = value;
        } else if (property == 'amount') {
            tdObjectProp.innerText = 'Amount';
            tdObjectVal.innerText = value;
        } else if (property == 'submitted' && value) {
            tdObjectProp.innerText = 'Date Submitted';
            tdObjectVal.innerText = new Date(value);
        } else if (property == 'description') {
            tdObjectProp.innerText = 'Comments';
            tdObjectVal.innerText = value;
        } else if (property == 'resolved' && value) {
            tdObjectProp.innerText = 'Date Resolved';
            tdObjectVal.innerText = new Date(value);
        }

        if (value) {
            trObject.appendChild(tdObjectProp);
            trObject.appendChild(tdObjectVal);
            tableBody.appendChild(trObject);
        }
    }

    expenseTable.appendChild(tableHeader);
    expenseTable.appendChild(tableBody);
    expenseRow.appendChild(expenseTable);
}

// Enables the proper buttons after an expense report has been clicked
function enableButtons() {
    changeAcceptDenyButtonState(true);
}

function disableButtons() {
    changeAcceptDenyButtonState(false);
}

function changeAcceptDenyButtonState(state) {
    let acceptButton = document.getElementById('acceptButton');
    let denyButton = document.getElementById('denyButton');

    if (state) {
        acceptButton.classList.remove('d-none');
        denyButton.classList.remove('d-none');
    } else {
        acceptButton.classList.add('d-none');
        denyButton.classList.add('d-none');
    }
}

// Enables compose button for employees
function enableCompose() {
    let composeButton = document.getElementById('composeButton');
    console.log(sessionStorage['userRole']);
    if (sessionStorage['userRole'] == 'employee') {
        console.log("enabling compose button")
        composeButton.classList.remove('d-none');
    }
    if (sessionStorage['userRole'] != 'employee') {
        composeButton.classList.add('d-none');
    }
}

function composeButtonHandler(event) {
    expenseViewHide();
    composeFormShow();
}

function composeFormHide() {
    composeFormChangeState('hide');
}

function composeFormShow() {
    composeFormChangeState('show');
}

function composeFormChangeState(state) {
    let composeForm = document.getElementById('composeForm');

    composeForm.reset();

    if (state == 'show') {
        composeForm.classList.remove('d-none');
    } else {
        composeForm.classList.add('d-none');
    }

}

function expenseViewHide() {
    expenseViewChangeState('hide');
}

function expenseViewShow() {
    expenseViewChangeState('show');
}

function expenseViewChangeState(state) {
    let expenseView = document.getElementById('expenseView');

    if (state == 'show') {
        expenseView.classList.remove('d-none');
    } else {
        expenseView.classList.add('d-none');
    }
}

function expenseSubmitHandler(event) {
    event.preventDefault();
    console.log("Sending new expense");
    sendNewExpense();
    composeFormHide();
}

// TODO: IMPLEMENT
async function sendNewExpense() {
    let newExpense = {
        'amount': document.getElementById('expenseAmount').value,
        'submitted': Date.now(),
        'description': document.getElementById('expenseDescription').value,
        'type': document.getElementById('expenseType').value
    }
    let result = await fetch(`http://${CURRENT_SERVER}/ExpenseReimbursement/api/addexpense`, {
        method: 'POST',
        body: JSON.stringify(newExpense)
    });

    let jsonReponse = await result.json();

    console.log(jsonReponse);

    // Reload table of contents since something new should be added
    loadReimbursementExpenses();
}

function acceptButtonHandler() {
    updateStatusButton(2, 'approved');
}

function denyButtonHandler() {
    updateStatusButton(3, 'denied');
}

function updateStatusButton(newId, newStatus) {
    let expenseObj = JSON.parse(sessionStorage['currentExpense']);
    console.log('In updateStatusButton');

    expenseObj.status.id = newId;
    expenseObj.status.status = newStatus;
    expenseObj.resolved = Date.now();

    sendUpdatedStatus(expenseObj);

    expenseViewHide();

}

async function sendUpdatedStatus(expense) {
    console.log(`SENDING: ${JSON.stringify(expense)}`);
    let response = await fetch(`http://${CURRENT_SERVER}/ExpenseReimbursement/api/updatestatus`, {
        method: 'POST',
        body: JSON.stringify(expense)
    })
        .then(resp => resp.json())
        .then(data => { loadReimbursementExpenses(); });
}

(function () {
    'use strict'

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    let forms = document.querySelectorAll('.needs-validation')

    // Loop over them and prevent submission
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                    form.classList.add('was-validated');
                } else {
                    event.preventDefault();
                    expenseSubmitHandler(event);
                    form.classList.remove('was-validated');
                }
            }, false)
        })
})()
