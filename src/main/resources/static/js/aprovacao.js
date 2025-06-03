// Definindo a enumeração para o status (alinhado com backend ApprovalStatus)
const Status = Object.freeze({
    PENDING: 'PENDING',
    APPROVED: 'APPROVED',
    REJECTED: 'REJECTED'
});

// Função para exibir alertas
function showAlert(message, isError = true) {
    const alertContainer = document.getElementById("alert-container");
    alertContainer.innerHTML = `
        <div class="custom-alert" style="background-color: ${isError ? '#f8d7da' : '#d4edda'}; color: ${isError ? '#721c24' : '#155724'}">
            ${message}
            <span class="close-btn" onclick="this.parentElement.style.display='none';">×</span>
        </div>
    `;
}

// Função para enviar aprovação ou reprovação
function enviarAprovacao(idUser, idTimesheet, approvalStatus, comentario) {
    const token = localStorage.getItem("token");
    if (!token) {
        showAlert("Token não encontrado. Usuário não autenticado.");
        setTimeout(() => window.location.href = "login.html", 2000);
        return;
    }

    const body = {
        idUser: idUser,
        idTimesheet: idTimesheet,
        approvalStatus: approvalStatus,  // 'APPROVED' ou 'REJECTED' ou 'PENDING'
        comments: comentario
    };

    fetch("http://localhost:8080/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(body)
    })
    .then(response => {
        if (!response.ok) {
            if (response.status === 400) return response.text().then(text => { throw new Error(text); });
            if (response.status === 401) throw new Error("Usuário não autorizado.");
            if (response.status === 403) throw new Error("Acesso negado.");
            if (response.status === 404) throw new Error("Registro não encontrado.");
            if (response.status === 500) throw new Error("Erro interno do servidor.");

            // tratar erros...
            throw new Error(`Erro: ${response.statusText}`);
        }
        return response;
    })
    .then(() => {
        showAlert(`Registro ${approvalStatus.toLowerCase()} com sucesso!`, false);
        carregarRegistros();
    })
    .catch(error => {
        showAlert(error.message);
    });
}


// Função para carregar registros e preencher a tabela
function carregarRegistros() {
    const token = localStorage.getItem("token");
    console.log("Token:", token);
    if (!token) {
        showAlert("Token não encontrado. Usuário não autenticado.");
        setTimeout(() => window.location.href = "login.html", 2000);
        return;
    }

    fetch("http://localhost:8080/approval/listapproval", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        cache: 'no-store' // Evita cache para dados atualizados
    })
    .then(response => {
        if (!response.ok) {
            if (response.status === 401) throw new Error("Usuário não autorizado.");
            if (response.status === 403) throw new Error("Acesso negado.");
            throw new Error("Erro ao buscar registros.");
        }
        return response.json();
    })
    .then(data => {
        const tBody = document.getElementById("tBody");
        tBody.innerHTML = "";
        data.forEach(entry => {
            if (entry.error) {
                console.log("Skipping error entry:", entry.error);
                return; // Ignora mapas de erro
            }
            const row = document.createElement("tr");
            row.setAttribute("data-work-entry-id", entry.workEntryId);
            row.innerHTML = `
                <td>${entry.firstName || 'N/A'}</td>
                <td>${entry.data || 'N/A'}</td>
                <td>${entry.start || 'N/A'}</td>
                <td>${entry.end || 'N/A'}</td>
                <td>${entry.approvalStatus || 'N/A'}</td>
                <td>${entry.comentario || ''}</td>
                <td>
                    <button class="btn aprovar" ${entry.approvalStatus !== Status.PENDING ? 'disabled' : ''}>Aprovar</button>
                    <button class="btn rejeitar" ${entry.approvalStatus !== Status.PENDING ? 'disabled' : ''}>Rejeitar</button>
                </td>
            `;
            tBody.appendChild(row);
        });

        document.querySelectorAll(".btn.aprovar, .btn.rejeitar").forEach(button => {
            button.addEventListener("click", () => {
                const row = button.closest("tr");
                const workEntryId = row.dataset.workEntryId;
                const status = button.classList.contains("aprovar") ? Status.APPROVED : Status.REJECTED;
                const comentario = document.getElementById("comment").value;

                document.getElementById("aprovacao").value = workEntryId;
                document.getElementById("nome").value = row.children[0].textContent;
                document.getElementById("data").value = row.children[1].textContent;

                enviarAprovacao(workEntryId, status, comentario);
            });
        });
    })
    .catch(error => {
        console.error("Erro ao buscar registros:", error);
        showAlert(error.message);
        if (error.message.includes("não autorizado")) {
            setTimeout(() => window.location.href = "login.html", 2000);
        }
    });
}

// Inicializa ao carregar a página
document.addEventListener("DOMContentLoaded", carregarRegistros);

// Eventos dos botões manuais
document.getElementById("btn-aprovar").addEventListener("click", () => {
    const workEntryId = document.getElementById("aprovacao").value;
    const comentario = document.getElementById("comment").value;
    if (!workEntryId) {
        showAlert("Selecione um registro para aprovar.");
        return;
    }
    enviarAprovacao(workEntryId, Status.APPROVED, comentario);
});

document.getElementById("btn-reprovar").addEventListener("click", () => {
    const workEntryId = document.getElementById("aprovacao").value;
    const comentario = document.getElementById("comment").value;
    if (!workEntryId) {
        showAlert("Selecione um registro para reprovar.");
        return;
    }
    enviarAprovacao(workEntryId, Status.REJECTED, comentario);
});