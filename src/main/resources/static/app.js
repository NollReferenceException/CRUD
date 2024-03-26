var form = document.getElementById("myForm"),
    userName = document.getElementById("name"),
    id = document.getElementById("id"),
    lastNameElement = document.getElementById("lastName"),
    age = document.getElementById("age"),
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
    let response = await fetch("/admin/data");

    if (response.ok) { // если HTTP-статус в диапазоне 200-299
        // получаем тело ответа (см. про этот метод ниже)
        getData = await response.json();
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
            <td>${JSON.stringify(element.roles)}</td>

            <td>
                <button class="btn btn-primary" onclick="editInfo(${element.id}, '${element.name}', '${element.age}', '${element.email}', '${element.lastName}', '${element.roles}')" data-bs-toggle="modal" data-bs-target="#userForm"><i class="bi bi-pencil-square"></i></button>

                <button class="btn btn-danger" onclick="deleteInfo(${element.id})"><i class="bi bi-trash"></i></button>
                            
            </td>
        </tr>`

        userInfo.innerHTML += createElement
    })
}


function readInfo(pic, name, age, city, email, phone) {
    document.querySelector('.showImg').src = pic,
        document.querySelector('#showName').value = name,
        document.querySelector("#showAge").value = age,
        document.querySelector("#showCity").value = city,
        document.querySelector("#showEmail").value = email,
        document.querySelector("#showPhone").value = phone
}


function editInfo(index, name, Age, Email, lastName, roles) {
    isEdit = true
    editId = index
    userName.value = name
    age.value = Age
    email.value = Email
    lastNameElement.value = lastName
    roles.value = roles

    submitBtn.innerText = "Update"
    modalTitle.innerText = "Update The Form"
}


async function deleteInfo(index) {
    if (confirm("Are you sure want to delete?")) {

        // localStorage.setItem("userProfile", JSON.stringify(getData))
        const response = await fetch("/admin/delete/" + index);
        const movies = await response.json();
        console.log(movies);
        if (response.ok) {
            getData.splice(index, 1)
            showInfo()
        }

    }
}

async function postJSON(data, url) {
    try {
        const response = await fetch(url, {
            method: "POST", // or 'PUT'
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        const result = await response.json();
        console.log("Success:", result);
    } catch (error) {
        console.error("Error:", error);
    }
}

async function getJSON(url) {
    const response = await fetch(url);
    const movies = await response.json();
    console.log(movies);
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

    // if(!isEdit){
    getData.push(information)
    // }
    // else{
    //     isEdit = false
    //     getData[editId] = information
    // }

    const response = await postJSON(information, "/admin/create")

    // localStorage.setItem('userProfile', JSON.stringify(getData))

    submitBtn.innerText = "Submit"
    modalTitle.innerHTML = "Fill The Form"

    showInfo()

    newUserform.reset()
    console.log("CREATED")

    // modal.style.display = "none"
    // document.querySelector(".modal-backdrop").remove()
})

form.addEventListener('submit', async (e) => {
    e.preventDefault()

    const information = {
        id: editId,
        name: userName.value,
        lastName: lastNameElement.value,
        age: age.value,
        email: email.value,
        password: password.value
    }

    // if(!isEdit){
    //     getData.push(information)
    // }
    // else{
    isEdit = false
    getData[editId] = information
    // }

    await postJSON(information, "/admin/update")

    // localStorage.setItem('userProfile', JSON.stringify(getData))

    // submitBtn.innerText = "Submit"
    // modalTitle.innerHTML = "Fill The Form"

    showInfo()

    console.log("UPDATED")

    // modal.style.display = "none"
    // document.querySelector(".modal-backdrop").remove()
})