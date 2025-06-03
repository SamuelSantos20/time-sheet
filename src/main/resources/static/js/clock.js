export function startClock() {
    const currentTimeEl = document.getElementById("current-time");
    if (!currentTimeEl) {
        console.warn("Elemento 'current-time' não encontrado.");
        return;
    }

    function updateClock() {
        const now = new Date();
        const formatted = now.toLocaleTimeString("pt-BR", { hour12: false });
        currentTimeEl.textContent = formatted;
    }

    setInterval(updateClock, 1000);
    updateClock();
}
