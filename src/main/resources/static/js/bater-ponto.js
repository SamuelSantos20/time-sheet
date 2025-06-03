document.addEventListener('DOMContentLoaded', () => {
    // Não limpar o token automaticamente
    // localStorage.removeItem('token'); // Comentar esta linha

    const form = document.getElementById("pontoForm");
    const registrationInput = document.getElementById("registrationInput");
    const passwordInput = document.getElementById("passwordInput");
    const statusDisplay = document.getElementById("status");
    const alertContainer = document.getElementById("alert-container");

    // Função para verificar se o token está expirado
    function isTokenExpired(token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const expiry = payload.exp * 1000;
            return Date.now() > expiry;
        } catch (e) {
            return true;
        }
    }

    form.addEventListener("submit", async function (e) {
        e.preventDefault();

        const username = registrationInput.value.trim();
        const password = passwordInput.value.trim();

        if (!username) {
            showAlert("Por favor, informe a matrícula.", "danger");
            return;
        }
        if (!password) {
            showAlert("Por favor, informe a senha.", "danger");
            return;
        }

        try {
            // Step 1: Authenticate user
            const authResponse = await fetch("http://localhost:8080/authenticate", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ username, password }),
            });

            if (!authResponse.ok) {
                if (authResponse.status === 401) {
                    showAlert("Matrícula ou senha inválidos.", "danger");
                } else if (authResponse.status === 404) {
                    const errorText = await authResponse.text();
                    showAlert(`Usuário não encontrado: ${errorText}`, "danger");
                } else {
                    const errorText = await authResponse.text();
                    showAlert(`Erro na autenticação: ${errorText}`, "danger");
                }
                return;
            }

            // Parse authentication response
            const authData = await authResponse.json();
            let token = authData.token;

            if (!token) {
                showAlert("Token de autenticação não recebido.", "danger");
                return;
            }
            localStorage.setItem("token", token);

            console.log("Token:", token);

            // Verificar se o token está expirado
            if (isTokenExpired(token)) {
                showAlert("Token expirado. Faça login novamente.", "danger");
                //window.location.href = "login.html";
                return;
            }

            // Step 2: Register work entry
            const entryResponse = await fetch(`http://localhost:8080/workEntry/entry`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify({ username }), // Adicionar corpo, se necessário
            });

            console.log("Status HTTP:", entryResponse.status);

            if (entryResponse.ok || entryResponse.status === 201) {
                let data = {};
                const contentType = entryResponse.headers.get("Content-Type");
                if (contentType && contentType.includes("application/json")) {
                    data = await entryResponse.json().catch(() => ({}));
                }

                if (data.jwt) {
                    localStorage.setItem("token", data.jwt);
                }

                showAlert("Ponto registrado com sucesso!", "success");
                statusDisplay.textContent = "Ponto registrado";
                registrationInput.value = "";
                passwordInput.value = "";
                window.location.href = "timesheet-entry-exit.html";
            } else if (entryResponse.status === 400) {
                const errorText = await entryResponse.text();
                showAlert(`Erro: ${errorText}`, "danger");
                window.location.href = "timesheet-entry-exit.html";

            } else if (entryResponse.status === 409) {
                const errorText = await entryResponse.text();
                showAlert(`Erro: ${errorText}`, "danger");
                //window.location.href = "timesheet-BuscaFuncionario.html";
            } else if (entryResponse.status === 401) {
                showAlert("Usuário não autorizado. Faça login novamente.", "danger");
                //window.location.href = "login.html";
            } else if (entryResponse.status === 403) {
                showAlert("Acesso negado.", "danger");
            } else if (entryResponse.status === 404) {
                showAlert("Usuário não encontrado.", "danger");
            } else if (entryResponse.status === 500) {
                showAlert("Erro interno do servidor.", "danger");
            } else {
                const errorText = await entryResponse.text();
                showAlert(`Erro ao registrar ponto: ${errorText}`, "danger");
            }
        } catch (error) {
            showAlert("Erro de conexão com o servidor.", "danger");
            console.error("Erro na requisição:", error);
        }
    });

    function showAlert(message, type) {
        alertContainer.innerHTML = `
            <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                ${message}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
        `;
    }
});