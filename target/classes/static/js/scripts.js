document.addEventListener('DOMContentLoaded', function () {
    const deleteButtons = document.querySelectorAll('.delete-btn');
    deleteButtons.forEach(btn => {
        btn.addEventListener('click', function (e) {
            if (!confirm('Naozaj chcete odstrániť túto položku?')) {
                e.preventDefault();
            }
        });
    });

    const nameInput = document.getElementById('product-name');
    const slugInput = document.getElementById('product-url');

    if (nameInput && slugInput) {
        nameInput.addEventListener('input', function () {
            if (window.isProductNew) {
                let slug = nameInput.value
                    .toLowerCase()
                    .normalize("NFD")
                    .replace(/[\u0300-\u036f]/g, "")
                    .replace(/[^a-z0-9 -]/g, "")
                    .replace(/\s+/g, "-")
                    .replace(/-+/g, "-");
                slugInput.value = slug;
            }
        });
    }
});

function addImageRow() {
    const list = document.getElementById('image-list');
    const index = list.children.length;
    const newRow = document.createElement('div');
    newRow.className = "flex items-center gap-3 bg-gray-50 p-3 rounded-lg border border-gray-200";
    newRow.innerHTML = `
            <input type="text" name="imageUrls" placeholder="URL adresa obrázka" class="flex-grow px-3 py-2 border rounded-md">
            <label class="flex items-center gap-2 whitespace-nowrap text-sm">
                <input type="radio" name="mainImageIndex" value="${index}"> Hlavný
            </label>
            <button type="button" onclick="this.parentElement.remove()" class="text-red-500 hover:text-red-700">
                <i class="fa fa-trash"></i>
            </button>
        `;
    list.appendChild(newRow);
}