const form = document.querySelector("form");
const IDate = document.getElementById("datePicker");

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
    if (!IDate.value) {
        showAlert("Por favor, preencha o registro e a data.");
        return;
    }

    const dateParts = IDate.value.split("-"); // Formato: "YYYY-MM"
    const ano = dateParts[0];
    const mes = dateParts[1];

    if (!ano || !mes) {
        showAlert("Data inválida.");
        return;
    }
    const token = localStorage.getItem('token');
    if (!token) {
        showAlert("Token não encontrado. Faça login novamente.");
        return;
    }


    fetch(`http://localhost:8080/workEntry/month/${mes}/year/${ano}`, {
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
            console.log("Dados recebidos do servidor:", data);
            const tBody = document.getElementById("tBody");
            tBody.innerHTML = ""; // Limpa a tabela

            if (data.length === 0) {
                showAlert("Nenhum dado encontrado para a data e registro fornecidos.");
                return;
            }

            let totalHours = 0;
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
                lastHoursWorked = entry.hoursWorked; // apenas atribuição do texto
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

form.addEventListener("submit", function (event) {
    event.preventDefault();
    listHours();
    limparCampos();
});


// Opcional: Lógica para o botão "Submit"
document.getElementById("submit").addEventListener("click", function () {
    const rows = document.querySelectorAll("#tBody tr");
    const entries = Array.from(rows).map(row => ({
        day: row.querySelector("td:nth-child(1) input").value,
        month: row.querySelector("td:nth-child(2) input").value,
        startTime: row.querySelector("td:nth-child(3) input").value,
        endTime: row.querySelector("td:nth-child(4) input").value,
        hoursWorked: row.querySelector("td:nth-child(5) input").value,
    }));

    fetch("http://localhost:8080/workEntry/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(entries),
    })
        .then(response => {
            if (response.ok) {
                showAlert("Dados salvos com sucesso!");
            } else {
                throw new Error("Erro ao salvar os dados.");
            }
        })
        .catch(error => {
            console.error("Erro ao salvar os dados:", error);
            showAlert("Erro ao conectar ao servidor.");
        });
});