export function showAlert(message, type = "danger") {
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
