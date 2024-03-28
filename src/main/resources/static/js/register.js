var newUserform = document.getElementById("nmyForm"),
    newUserName = document.getElementById("nname"),
    newLastNameElement = document.getElementById("nlastName"),
    newAge = document.getElementById("nage"),
    newPassword = document.getElementById("npassword"),
    newEmail = document.getElementById("nemail")


newUserform.addEventListener('submit', async (e) => {
    e.preventDefault()

    const information = {
        name: newUserName.value,
        lastName: newLastNameElement.value,
        age: newAge.value,
        email: newEmail.value,
        password: newPassword.value
    }


    const response = await postJSON(information, "/register")


    if (response.ok) {
        $(document).ready(function(){
            window.location.href = '/login';
        });
    } else {
        alert("Ошибка HTTP: " + response.status);
    }

    newUserform.reset()
    console.log("CREATED")
})

async function postJSON(data, url) {
    try {
        const response = await fetch(url, {
            method: "POST", // or 'PUT'
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        const result = await response;
        console.log("Success:", result);
        return result;
    } catch (error) {
        console.error("Error:", error);
    }
}

