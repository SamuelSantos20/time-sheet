import { showAlert } from './alert.js';
import { startClock } from './clock.js';

document.addEventListener("DOMContentLoaded", () => {
    const statusDisplay = document.getElementById('statusDisplay');
    const finalizarBtn = document.getElementById('finalizar-btn');

    console.log("Página atual:", window.location.href);

    if (!statusDisplay) {
        console.warn("Elemento 'statusDisplay' não encontrado no DOM.");
    }

    if (!finalizarBtn) {
        console.warn("Botão 'finalizar-btn' não encontrado no DOM.");
        return;
    }

    finalizarBtn.addEventListener("click", finalizePunch);
    startClock();

    async function finalizePunch() {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                showAlert("Token não encontrado. Faça login novamente.", "danger");
                return;
            }

            const response = await fetch(`http://localhost:8080/workEntry/exit`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });

            if (response.ok || response.status === 201) {
                const data = await response.json();
                localStorage.setItem("token", data.jwt);
                showAlert("Ponto registrado com sucesso!", "success");

                if (statusDisplay) {
                    statusDisplay.textContent = "Ponto registrado";
                }

                setTimeout(() => {
                    window.location.href = "timesheet-BuscaFuncionario.html";
                }, 1000);
            } else {
                const errorText = await response.text();
                switch (response.status) {
                    case 400:
                        showAlert(`Erro: ${errorText}`, "danger");
                        break;
                    case 404:
                        showAlert("Usuário não encontrado.", "danger");
                        break;
                    case 401:
                        showAlert("Usuário não autorizado.", "danger");
                        break;
                    case 403:
                        showAlert("Acesso negado.", "danger");
                        break;
                    case 500:
                        showAlert("Erro interno do servidor.", "danger");
                        break;
                    default:
                        showAlert(`Erro ao registrar ponto: ${errorText}`, "danger");
                        break;
                }
            }
        } catch (error) {
            console.error("Erro na requisição:", error);
            showAlert("Erro de conexão com o servidor.", "danger");
        }
    }
});
