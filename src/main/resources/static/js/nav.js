document.addEventListener("DOMContentLoaded", function () {
    // Toggle dropdown submenu
    document.querySelectorAll("nav ul li a:not(:only-child)").forEach(function (link) {
      link.addEventListener("click", function (e) {
        const dropdown = this.nextElementSibling;
        if (dropdown && dropdown.classList.contains("nav-dropdown")) {
          // Fecha outros dropdowns abertos
          document.querySelectorAll(".nav-dropdown").forEach(function (menu) {
            if (menu !== dropdown) menu.style.display = "none";
          });

          // Alterna o dropdown atual
          dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";

          e.stopPropagation(); // Impede fechamento por clique fora
        }
      });
    });

    // Fecha dropdowns se clicar fora
    document.documentElement.addEventListener("click", function () {
      document.querySelectorAll(".nav-dropdown").forEach(function (menu) {
        menu.style.display = "none";
      });
    });

    // Toggle do menu principal (mobile)
    const navToggle = document.getElementById("nav-toggle");
    if (navToggle) {
      navToggle.addEventListener("click", function () {
        const navList = document.querySelector("nav ul");
        if (navList) {
          navList.style.display =
            navList.style.display === "block" ? "none" : "block";
        }
        this.classList.toggle("active");
      });
    }
  });
