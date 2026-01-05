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

function editCategory(id, name) {
    const idField = document.getElementById('catId');
    const nameField = document.getElementById('catName');
    const submitBtn = document.getElementById('catSubmitBtn');
    const cancelBtn = document.getElementById('catCancelBtn');

    if (idField && nameField) {
        idField.value = id;
        nameField.value = name;
        if (submitBtn) submitBtn.innerText = 'Uložiť zmeny';
        if (cancelBtn) cancelBtn.classList.remove('hidden');
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}

function resetCatForm() {
    document.getElementById('catId').value = '';
    document.getElementById('catName').value = '';
    document.getElementById('catSubmitBtn').innerText = 'Pridať';
    document.getElementById('catCancelBtn').classList.add('hidden');
}

function editTag(id, name, color) {
    document.getElementById('tagId').value = id;
    document.getElementById('tagName').value = name;
    document.getElementById('tagColor').value = color;
    document.getElementById('tagSubmitBtn').innerText = 'Uložiť zmeny';
    document.getElementById('tagCancelBtn').classList.remove('hidden');
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function resetTagForm() {
    document.getElementById('tagId').value = '';
    document.getElementById('tagName').value = '';
    document.getElementById('tagColor').value = '#808080';
    document.getElementById('tagSubmitBtn').innerText = 'Pridať';
    document.getElementById('tagCancelBtn').classList.add('hidden');
}