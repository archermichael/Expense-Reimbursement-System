let registerForm = document.getElementById("register-form");

window.onload = async () => {
    const sessionRes = await fetch("http://localhost:8080/api/check-session");
    const sessionUserData = await sessionRes.json();

    if(!sessionUserData.data || sessionUserData.data.roleId != 2){
        window.location = "http://localhost:8080/";
    }
}

registerForm.onsubmit = async (e) => {
    e.preventDefault();

    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let email = document.getElementById("email").value;
    let roleId = document.getElementById("roleId").value;

    let user = await fetch("http://localhost:8080/api/register", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            username,
            password,
            firstName,
            lastName,
            email,
            roleId: parseInt(roleId)
        })
    });

    let userData = await user.json();
    console.log(userData);
}