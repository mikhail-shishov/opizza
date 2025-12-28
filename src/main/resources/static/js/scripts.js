document.addEventListener('DOMContentLoaded', function () {
    const deleteButtons = document.querySelectorAll('.delete-btn');
    deleteButtons.forEach(btn => {
        btn.addEventListener('click', function (e) {
            if (!confirm('Naozaj chcete odstrániť túto položku?')) {
                e.preventDefault();
            }
        });
    });
});