const loginForm = document.getElementById("login-form");

window.onload = async () => {
    const sessionRes = await fetch("http://localhost:8080/api/check-session");
    const sessionUserData = await sessionRes.json();

    if(sessionUserData.data){
        if(sessionUserData.data.roleId == 1){
            window.location = "http://localhost:8080/views/dashboard-view.html";
        } else if(sessionUserData.data.roleId == 2){
            window.location = "http://localhost:8080/views/manager-view.html";
        };
    }
};

loginForm.onsubmit = async (e) => {
    e.preventDefault();
    
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const user = await fetch("http://localhost:8080/api/login", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    });

    const userData = await user.json();

    if (userData.data.roleId == 1){
        window.location.href = "http://localhost:8080/views/dashboard-view.html";
    } else if (userData.data.roleId == 2){
        window.location.href = "http://localhost:8080/views/manager-view.html";
    };

    console.log(userData);
};