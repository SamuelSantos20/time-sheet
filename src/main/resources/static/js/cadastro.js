const form = document.querySelector("#cadastro-form");
const IfirstName = document.querySelector(".firstName");
const IlastName = document.querySelector(".lastName");
const Iemail = document.querySelector(".email");
const Idepartment = document.querySelector(".department");
const Iposition = document.querySelector(".position");

function showAlert(message, type = "danger") {
    const alertContainer = document.getElementById('alert-container');
    if (!alertContainer) {
        console.error("Alert container not found");
        return;
    }
    alertContainer.innerHTML = `
        <div class="custom-alert alert-${type}">
            ${message}
            <span class="close-btn" onclick="this.parentElement.style.display='none';">×</span>
        </div>
    `;
}

function cadastrar() {
    const token = localStorage.getItem("token");
    console.log("Token:", token);
    if (!token) {
        showAlert("Nenhum token encontrado. Faça login novamente.", "danger");
        window.location.href = "login.html";
        return;
    }

    fetch("http://localhost:8080/employee", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify({
            firstName: IfirstName.value,
            lastName: IlastName.value,
            email: Iemail.value,
            department: Idepartment.value,
            position: Iposition.value,
        }),
    }).then(function (response) {
        if (response.ok) {
            console.log("Usuário cadastrado com sucesso!");
            showAlert("Usuário cadastrado com sucesso!", "success");
            limparCampos();
        } else if (response.status === 401) {
            console.error("Erro: Token inválido ou sessão expirada.");
            showAlert("Sessão expirada. Faça login novamente.", "danger");
            localStorage.removeItem("token");
            window.location.href = "/login.html";
        } else {
            console.error("Erro ao cadastrar usuário:", response.status);
            showAlert("Erro ao cadastrar usuário.", "danger");
        }
    }).catch(function (error) {
        console.error("Erro ao conectar ao servidor:", error);
        showAlert("Erro ao conectar ao servidor.", "danger");
    });
}

function limparCampos() {
    IfirstName.value = "";
    IlastName.value = "";
    Iemail.value = "";
    Idepartment.value = "";
    Iposition.value = "";
    console.log("Campos de cadastro limpos.");
}

form.addEventListener("submit", function (event) {
    event.preventDefault();
    cadastrar();
});