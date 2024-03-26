var form = document.getElementById("myForm"),
    userName = document.getElementById("name"),
    id = document.getElementById("id"),
    lastNameElement = document.getElementById("lastName"),
    age = document.getElementById("age"),
    password = document.getElementById("password"),
    email = document.getElementById("email"),

    userInfo = document.getElementById("data")


let getData;

async function test() {
    let response = await fetch("/user/data");

    if (response.ok) { // если HTTP-статус в диапазоне 200-299
        // получаем тело ответа (см. про этот метод ниже)
        getData = await response.json();
        showInfo()
    } else {
        alert("Ошибка HTTP: " + response.status);
    }
}

test();

function showInfo() {
    let createElement = `<tr class="employeeDetails">
            <td>${getData.id}</td>
            <td>${getData.name}</td>
            <td>${getData.lastName}</td>
            <td>${getData.age}</td>
            <td>${getData.email}</td>
            <td>${JSON.stringify(getData.roles)}</td>
        </tr>`

    userInfo.innerHTML += createElement
}
