const container = document.getElementById("reimbursement-container");
const logoutBtn = document.getElementById("logout");

window.onload = async () => {
    const sessionRes = await fetch("http://localhost:8080/api/check-session");
    const sessionUserData = await sessionRes.json();

    if(sessionUserData.data){
        if(sessionUserData.data.roleId != 2){
            window.location = "http://localhost:8080/";
        } else if(sessionUserData.data.roleId == 2){
            populateData(sessionUserData.data.id);
        }
    } else {
        window.location = "http://localhost:8080/";
    }
}

let populateData = async (managerId) => {
    container.innerHTML = "";
    const reimbursements = await fetch("http://localhost:8080/api/reimbursements");

    const reimbursementData = await reimbursements.json();
    
    reimbursementData.data.forEach((reimbursement) => {
        let card = document.createElement("div");
        card.className = "card";
        let cardBody = document.createElement("div");
        cardBody.className = "card-body";
        
        let type = document.createElement("i");
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

        let employee = document.createElement("p");
        employee.className = "author";
        employee.innerText = "Requestor: " + reimbursement.authorFirstName + " " + reimbursement.authorLastName;

        let description = document.createElement("p");
        description.className = "description";
        description.innerText = "- \"" + reimbursement.description + "\"";

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
            resolver.innerText = "Resolver: " + reimbursement.resolverFirstName + " " + reimbursement.resolverLastName;
        } else {
            resolver.innerText = "Resolver: ";
        }

        let status = document.createElement("i");
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

        let approveBtn = document.createElement("button");
        approveBtn.className = "btn btn-success fas fa-check approvalBtn";
        approveBtn.onclick = async () => {
            await fetch("http://localhost:8080/api/reimbursements", {
                method: "PATCH",
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({
                    id: reimbursement.id,
                    resolverId: managerId,
                    statusId: 1
                })
            });
        
            await populateData(managerId);
        };

        let denyBtn = document.createElement("button");
        denyBtn.className = "btn btn-danger fas fa-times approvalBtn";
        denyBtn.onclick = async () => {
            await fetch("http://localhost:8080/api/reimbursements", {
                method: "PATCH",
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({
                    id: reimbursement.id,
                    resolverId: managerId,
                    statusId: 3
                })
            });
        
            await populateData(managerId);
        };

        let statusTypeDiv = document.createElement("div");
        statusTypeDiv.className = "status-type";
        statusTypeDiv.appendChild(employee);
        statusTypeDiv.appendChild(status);
        statusTypeDiv.appendChild(type);
        statusTypeDiv.appendChild(submitted);

        let buttonsDiv = document.createElement("div");
        buttonsDiv.className = "buttons";
        buttonsDiv.appendChild(resolver);
        buttonsDiv.appendChild(approveBtn);
        buttonsDiv.appendChild(denyBtn);
        buttonsDiv.appendChild(resolved);

        cardBody.appendChild(statusTypeDiv);
        cardBody.appendChild(amount);
        cardBody.appendChild(description);
        cardBody.appendChild(buttonsDiv);

        card.appendChild(cardBody);

        container.appendChild(card);
    })
}

logoutBtn.onclick = async () => {
    await fetch("http://localhost:8080/api/logout");
    window.location = "http://localhost:8080/";
}