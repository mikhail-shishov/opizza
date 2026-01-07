document.addEventListener('DOMContentLoaded', function () {
    const menuToggle = document.getElementById('menu-toggle');
    const mobileMenu = document.getElementById('mobile-menu');

    if (menuToggle && mobileMenu) {
        menuToggle.addEventListener('click', function() {
            mobileMenu.classList.toggle('hidden');
            // Optional: Change icon from bars to X
            const icon = menuToggle.querySelector('i');
            if (mobileMenu.classList.contains('hidden')) {
                icon.classList.replace('fa-times', 'fa-bars');
            } else {
                icon.classList.replace('fa-bars', 'fa-times');
            }
        });
    }

    document.addEventListener('DOMContentLoaded', function() {
        const itemsPerPage = 10;
        const container = document.getElementById('pizza-container');
        if (!container) return;

        const items = Array.from(container.getElementsByClassName('pizza-item'));
        const controls = document.getElementById('pagination-controls');
        let currentPage = 1;

        function showPage(page) {
            currentPage = page;
            const start = (page - 1) * itemsPerPage;
            const end = start + itemsPerPage;

            items.forEach((item, index) => {
                item.style.display = (index >= start && index < end) ? 'block' : 'none';
            });

            updateControls();
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }

        function updateControls() {
            const totalPages = Math.ceil(items.length / itemsPerPage);
            if (totalPages <= 1) {
                controls.innerHTML = '';
                return;
            }

            let html = '';
            html += `<button onclick="changePage(${currentPage - 1})" ${currentPage === 1 ? 'disabled' : ''} 
                    class="px-4 py-2 border rounded-lg ${currentPage === 1 ? 'text-gray-300' : 'hover:bg-gray-100'}">Predchádzajúca</button>`;

            for (let i = 1; i <= totalPages; i++) {
                html += `<button onclick="changePage(${i})" 
                        class="px-4 py-2 border rounded-lg ${currentPage === i ? 'bg-red-600 text-white' : 'hover:bg-gray-100'}">${i}</button>`;
            }

            html += `<button onclick="changePage(${currentPage + 1})" ${currentPage === totalPages ? 'disabled' : ''} 
                    class="px-4 py-2 border rounded-lg ${currentPage === totalPages ? 'text-gray-300' : 'hover:bg-gray-100'}">Nasledujúca</button>`;

            controls.innerHTML = html;
        }

        window.changePage = function(page) {
            const totalPages = Math.ceil(items.length / itemsPerPage);
            if (page < 1 || page > totalPages) return;
            showPage(page);
        };

        showPage(1);
    });
});