const form = document.querySelector("form");
const Iregistration = document.getElementById("registration");
const IDate = document.getElementById("datePicker");
const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}
let mes = "";
let ano = "";

console.log("Token:", token);
function showAlert(message) {
    const alertContainer = document.getElementById('alert-container');
    alertContainer.innerHTML = `
        <div class="custom-alert">
            ${message}
            <span class="close-btn" onclick="this.parentElement.style.display='none';">×</span>
        </div>
    `;
}

function listHours() {
    if (!Iregistration.value || !IDate.value) {
        showAlert("Por favor, preencha o registro e a data.");
        return;
    }

    const dateParts = IDate.value.split("-");
    ano = dateParts[0];
    mes = dateParts[1];

    if (!ano || !mes) {
        showAlert("Data inválida.");
        return;
    }

    fetch(`http://localhost:8080/workEntry/${Iregistration.value}/month/${mes}/year/${ano}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Erro ao buscar os dados do servidor.");
        }
    })
    .then(data => {
        const tBody = document.getElementById("tBody");
        tBody.innerHTML = "";

        if (data.length === 0) {
            showAlert("Nenhum dado encontrado para a data e registro fornecidos.");
            return;
        }

        let lastHoursWorked = 0;
        data.forEach(entry => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td><input type="text" class="form-control" value="${entry.day}" readonly></td>
                <td><input type="text" class="form-control" value="${entry.month}" readonly></td>
                <td><input type="time" class="form-control" value="${entry.startTime}" readonly></td>
                <td><input type="time" class="form-control" value="${entry.endTime}" readonly></td>
                <td><input type="text" class="form-control" value="${entry.hoursWorked}" readonly></td>
            `;
            tBody.appendChild(row);
            lastHoursWorked = entry.hoursWorked;
        });

        document.getElementById("totalHours").textContent = lastHoursWorked;
    })
    .catch(error => {
        console.error("Erro ao buscar os dados:", error);
        showAlert("Erro ao conectar ao servidor.");
    });
}

function limparCampos() {
    Iregistration.value = "";
    IDate.value = "";
    document.getElementById("tBody").innerHTML = "";
    document.getElementById("totalHours").textContent = "0";
}

// Substituir o botão "submit" por apenas exibir os dados
form.addEventListener("submit", function (event) {
    event.preventDefault();
    listHours();
    limparCampos();
});
