const container = document.getElementById("reimbursement-container");
const logoutBtn = document.getElementById("logout");
const newReimburseForm = document.getElementById("new-reimbursement-form");

window.onload = async () => {
    const sessionRes = await fetch("http://localhost:8080/api/check-session");
    const sessionUserData = await sessionRes.json();

    if(!sessionUserData.data){
        window.location = "http://localhost:8080/";
    } else if(sessionUserData.data.roleId == 2){
        window.location = "http://localhost:8080/views/manager-view.html";
    }

    newReimburseForm.onsubmit = async (e) => {
        e.preventDefault();
        let amount = document.getElementById("amount").value;
        let description = document.getElementById("description").value;
        let typeId = document.getElementById("type").value;
    
        let amountRE = /^\s*-?(\d+(\.\d{1,2})?|\.\d{1,2})\s*$/
    
        if (!amountRE.test(amount)){
            console.log("invalid amount");
            return;
        } else if (typeId == ""){
            console.log("you must select a type")
            return;
        } else {
            await fetch("http://localhost:8080/api/reimbursements", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({
                    amount: amount,
                    authorId: sessionUserData.data.id,
                    description: description,
                    typeId: typeId
                })
            })
        }

        window.location = "http://localhost:8080/views/dashboard-view.html";
    }

    logoutBtn.onclick = async () => {
        await fetch("http://localhost:8080/api/logout");
        window.location = "http://localhost:8080/";
    }

    const reimbursements = await fetch(`http://localhost:8080/api/reimbursements?id=${sessionUserData.data.id}`);

    const reimbursementData = await reimbursements.json();
    
    reimbursementData.data.forEach((reimbursement) => {
        let card = document.createElement("div");
        card.className = "card";
        let cardBody = document.createElement("div");
        cardBody.className = "card-body";
        
        let type = document.createElement("i");
        type.className = "type";
        switch(reimbursement.typeId){
            case 1: 
                type.className = "fas fa-house-user type";
                break;
            case 2:
                type.className = "fas fa-plane type";
                break;
            case 3:
                type.className = "fas fa-utensils type";
                break;
            case 4:
                type.className = "fas fa-marker type";
        }

        let amount = document.createElement("p");
        amount.className = "amount";
        amount.innerText = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(reimbursement.amount);

        let description = document.createElement("p");
        description.className = "description";
        description.innerText = "Description:\n\t" + reimbursement.description;

        let resolved = document.createElement("p");
        resolved.className = "resolved";
        if (reimbursement.resolved != null){
            resolved.innerText = "Resolved: " + new Date(reimbursement.resolved).toLocaleString();
        } else {
            resolved.innerText = "Resolved: ";
        }

        let resolver = document.createElement("p");
        resolver.className = "resolver";
        if (reimbursement.resolverId != 0){
            resolver.innerText = "Resolved By: " + reimbursement.resolverFirstName + " " + reimbursement.resolverLastName;
        } else {
            resolver.innerText = "Resolved By: ";
        }

        let status = document.createElement("i");
        status.className = "status";
        switch (reimbursement.statusId){
            case 1:
                status.className = "fas fa-check status green";
                break;
            case 2:
                status.className = "fas fa-question status grey";
                break;
            case 3:
                status.className = "fas fa-times status red";
        }

        let submitted = document.createElement("p");
        submitted.className = "submitted";
        submitted.innerText = "Submitted: " + new Date(reimbursement.submitted).toLocaleString();


        let statusTypeDiv = document.createElement("div");
        statusTypeDiv.className = "status-type";
        statusTypeDiv.appendChild(status);
        statusTypeDiv.appendChild(type);
        statusTypeDiv.appendChild(submitted);

        let buttonsDiv = document.createElement("div");
        buttonsDiv.className = "buttons";
        buttonsDiv.appendChild(resolver);
        buttonsDiv.appendChild(resolved);

        cardBody.appendChild(statusTypeDiv);
        cardBody.appendChild(amount);
        cardBody.appendChild(description);
        cardBody.appendChild(buttonsDiv);

        card.appendChild(cardBody);

        container.appendChild(card);
    })
}