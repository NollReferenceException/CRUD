var form = document.getElementById("myForm"),
    userName = document.getElementById("name"),
    id = document.getElementById("id"),
    lastNameElement = document.getElementById("lastName"),
    age = document.getElementById("age"),
    roles = document.getElementById("roles"),
    password = document.getElementById("password"),
    email = document.getElementById("email"),

    newUserform = document.getElementById("nmyForm"),
    newUserName = document.getElementById("nname"),
    newLastNameElement = document.getElementById("nlastName"),
    newAge = document.getElementById("nage"),
    newPassword = document.getElementById("npassword"),
    newEmail = document.getElementById("nemail"),

    submitBtn = document.querySelector(".submit"),
    userInfo = document.getElementById("data"),
    modal = document.getElementById("userForm"),
    modalTitle = document.querySelector("#userForm .modal-title"),
    newUserBtn = document.querySelector(".newUser")

let getData;

async function test() {
    let response = await fetch("/admin/users");

    if (response.ok) { // если HTTP-статус в диапазоне 200-299
        // получаем тело ответа (см. про этот метод ниже)
        getData = await response.json();
        getData.forEach((element) => {
            element.roles = element.roles.map(o => o.role)
            element.allRoles = element.allRoles.map(o => o.role)
        })
        showInfo()
    } else {
        alert("Ошибка HTTP: " + response.status);
    }
}

test();
// let getData = await fetch("/admin")

let isEdit = false, editId

newUserBtn.addEventListener('click', () => {
    submitBtn.innerText = 'Submit'
    modalTitle.innerText = "Fill the Form"
    // isEdit = false
    newUserform.reset()
})


// file.onchange = function(){
//     if(file.files[0].size < 1000000){  // 1MB = 1000000
//         var fileReader = new FileReader();
//
//         fileReader.onload = function(e){
//             imgUrl = e.target.result
//             imgInput.src = imgUrl
//         }
//
//         fileReader.readAsDataURL(file.files[0])
//     }
//     else{
//         alert("This file is too large!")
//     }
// }


function showInfo() {
    document.querySelectorAll('.employeeDetails').forEach(info => info.remove())
    getData.forEach((element, index) => {
        let createElement = `<tr class="employeeDetails">
            <td>${element.id}</td>
            <td>${element.name}</td>
            <td>${element.lastName}</td>
            <td>${element.age}</td>
            <td>${element.email}</td>
            <td>${element.roles}</td>

            <td>
                <button class="btn btn-primary" onclick="editInfo(${index}, 
                '${element.id}',
                '${element.name}', 
                '${element.age}', 
                '${element.email}',
                '${element.lastName}',
                '${element.roles}',
                '${element.allRoles}')" 
                data-bs-toggle="modal" data-bs-target="#userForm"><i class="bi bi-pencil-square"></i></button>

                <button class="btn btn-danger" onclick="deleteInfo(${index}, ${element.id})"><i class="bi bi-trash"></i></button>
            </td>
        </tr>`
        index++;
        userInfo.innerHTML += createElement
    })
}

function editInfo(index, Id, name, Age, Email, lastName, Roles, AllRoles) {
    isEdit = true
    id.value = Id
    editId = index
    userName.value = name
    age.value = Age
    email.value = Email
    lastNameElement.value = lastName

    var allRolesData = AllRoles.split(",")
    var rolesData = Roles.split(",")
    var $select = $('#roles');
    $select.empty()
    $(allRolesData).each(function (index, o) {
        var $option = $("<option/>").attr("value", o).text(o).prop('selected', rolesData.includes(o));
        $select.append($option);
    });

    submitBtn.innerText = "Update"
    modalTitle.innerText = "Update The Form"
}


async function deleteInfo(index, id) {
    if (confirm("Are you sure want to delete?")) {
        const response = await jsonRequest("", "/admin/users/" + id, "DELETE");
        const movies = await response;
        console.log(movies);
        if (response.ok) {
            getData.splice(index, 1)
            showInfo()
        }
    }
}

async function jsonRequest(data, url, type) {
    try {
        const response = await fetch(url, {
            method: type,
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        console.log("Success:", response);
        return response;
    } catch (error) {
        console.error("Error:", error);
    }
}

newUserform.addEventListener('submit', async (e) => {
    e.preventDefault()

    const information = {
        name: newUserName.value,
        lastName: newLastNameElement.value,
        age: newAge.value,
        email: newEmail.value,
        password: newPassword.value
    }

    const response = await jsonRequest(information, "/register", "POST")
    const data = await response.json()

    data.roles = data.roles.map(o => o.role);
    data.allRoles = data.allRoles.map(o => o.role);

    getData.push(data)

    submitBtn.innerText = "Submit"
    modalTitle.innerHTML = "Fill The Form"

    showInfo()

    newUserform.reset()
    console.log("CREATED")
})

form.addEventListener('submit', async (e) => {
    e.preventDefault()

    const information = {
        id: id.value,
        name: userName.value,
        lastName: lastNameElement.value,
        age: age.value,
        email: email.value,
        password: password.value,
        roles: Array.from(roles.selectedOptions).map(s => s.value)
    }

    isEdit = false

    await jsonRequest(information, "/admin/users", "PUT")
    getData[editId] = information

    showInfo()

    console.log("UPDATED")
})
