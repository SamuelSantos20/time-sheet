document.addEventListener('DOMContentLoaded', () => {
    localStorage.removeItem('token'); // Clear old token on page load
    const form = document.querySelector('#login-form');
    form.addEventListener('submit', (event) => {
        event.preventDefault();
        logar();
    });
});

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

function limparCampos() {
    document.querySelector('.username').value = "";
    document.querySelector('.password').value = "";
    console.log("Campos de login limpos.");
}

function logar() {
    const username = document.querySelector('.username').value;
    const password = document.querySelector('.password').value;

    console.log("Enviando requisição com:", { username, password });

    fetch("http://localhost:8080/authenticate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            username: username,
            password: password,
        }),
    })
    .then(async response => {
        console.log("Status HTTP:", response.status);
        if (!response.ok) {
            const data = await response.text();
            switch (response.status) {
                case 401:
                    showAlert("Usuário ou senha inválidos.", "danger");
                    break;
                case 404:
                    showAlert(data || "Usuário ou senha inválidos.", "danger");
                    break;
                case 403:
                    showAlert("Acesso negado.", "danger");
                    break;
                case 500:
                    showAlert("Erro interno do servidor.", "danger");
                    break;
                default:
                    showAlert("Erro ao fazer login.", "danger");
            }
            throw new Error(`HTTP error: ${response.status}`);
        }

        const data = await response.json();
        console.log("Resposta recebida:", data);

        if (data.token) {
            console.log("Login bem-sucedido!");
            localStorage.setItem("token", data.token);
            console.log("Token armazenado:", data.token);
            limparCampos();
            console.log("Tentando redirecionar para: cadastro-usuario.html");
            window.location.href = "timesheet-entry.html"; // Adjust to your desired page*/
            console.log("Redirecionamento iniciado");
        } else {
            throw new Error("Token não encontrado na resposta");
        }
    })
    .catch(function (error) {
        console.error("Erro ao fazer login:", error);
        showAlert("Erro ao conectar ao servidor: " + error.message, "danger");
    });
}