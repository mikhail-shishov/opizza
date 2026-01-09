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

if (document.getElementById('productForm')) {
    document.getElementById('productForm').addEventListener('submit', function (e) {
        const fileInputs = document.querySelectorAll('input[type="file"][name="imageFiles"]');
        const inputsToRemove = [];
        fileInputs.forEach(input => {
            if (!input.files || input.files.length === 0) {
                const row = input.closest('div');
                if (row && !row.classList.contains('image-row') && row.querySelector('input[type="file"]')) {
                    inputsToRemove.push(row);
                }
            }
        });
        inputsToRemove.forEach(row => row.remove());

        const variantRows = document.querySelectorAll('.variant-row');
        let variants = [];
        variantRows.forEach(row => {
            const sizeId = row.getAttribute('data-size-id');
            const price = row.querySelector('.variant-price').value;
            if (price && price > 0) {
                variants.push(sizeId + ":" + price);
            }
        });
        document.getElementById('variantData').value = variants.join(",");

        const imageRows = document.querySelectorAll('.image-row');
        let imageIds = [];
        imageRows.forEach(row => {
            const imgId = row.getAttribute('data-img-id');
            if (imgId) {
                imageIds.push(imgId);
            }
        });
        document.getElementById('existingImageIdsData').value = imageIds.join(",");

        const tagCheckboxes = document.querySelectorAll('.tag-checkbox:checked');
        let tagIds = [];
        tagCheckboxes.forEach(cb => {
            tagIds.push(cb.value);
        });
        document.getElementById('tagData').value = tagIds.join(",");
    });
}

function addImageUploadRow() {
    const list = document.getElementById('image-list');
    const index = list.querySelectorAll('input[type="radio"]').length;
    const newRow = document.createElement('div');
    newRow.className = "flex items-center gap-3 bg-gray-50 p-3 rounded-lg border border-gray-200";
    newRow.innerHTML = `
        <input type="file" name="imageFiles" class="flex-grow text-sm file:mr-4 file:py-1 file:px-3 file:rounded-full file:border-0 file:bg-blue-50 file:text-blue-700">
        <label class="flex items-center gap-2 whitespace-nowrap text-sm">
            <input type="radio" name="mainImageIndex" value="${index}"> Hlavný
        </label>
        <button type="button" onclick="this.parentElement.remove()" class="text-red-500"><i class="fa fa-trash"></i></button>
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
        window.scrollTo({top: 0, behavior: 'smooth'});
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
    window.scrollTo({top: 0, behavior: 'smooth'});
}

function resetTagForm() {
    document.getElementById('tagId').value = '';
    document.getElementById('tagName').value = '';
    document.getElementById('tagColor').value = '#808080';
    document.getElementById('tagSubmitBtn').innerText = 'Pridať';
    document.getElementById('tagCancelBtn').classList.add('hidden');
}

function editSize(id, name, weight) {
    document.getElementById('sizeId').value = id;
    document.getElementById('sizeName').value = name;
    document.getElementById('sizeWeight').value = weight;

    document.getElementById('sizeSubmitBtn').innerText = 'Aktualizovať';
    document.getElementById('sizeCancelBtn').classList.remove('hidden');

    document.getElementById('sizeForm').scrollIntoView({behavior: 'smooth'});
}

function resetSizeForm() {
    document.getElementById('sizeId').value = '';
    document.getElementById('sizeName').value = '';
    document.getElementById('sizeWeight').value = '';

    document.getElementById('sizeSubmitBtn').innerText = 'Uložiť';
    document.getElementById('sizeCancelBtn').classList.add('hidden');
}

function filterUsers() {
    const input = document.getElementById('userSearch').value.toLowerCase();
    const rows = document.querySelectorAll('.user-row');

    rows.forEach(row => {
        const text = row.querySelector('.search-data').textContent.toLowerCase();
        row.style.display = text.includes(input) ? "" : "none";
    });
}

function updateOrderNotifications() {
    fetch('/orders/api/notifications/count')
        .then(response => response.json())
        .then(count => {
            const badges = document.querySelectorAll('.order-badge-count, #order-badge-mobile');
            badges.forEach(badge => {
                if (count > 0) {
                    badge.innerText = count;
                    badge.classList.remove('hidden');
                } else {
                    badge.classList.add('hidden');
                }
            });
        })
        .catch(err => console.error(err));
}

if (document.getElementById('order-badge') || document.getElementById('order-badge-mobile')) {
    updateOrderNotifications();
    setInterval(updateOrderNotifications, 60000);
}

document.addEventListener('DOMContentLoaded', function() {
    const editSizeButtons = document.querySelectorAll('.edit-size-btn');
    editSizeButtons.forEach(button => {
        button.addEventListener('click', function() {
            const id = this.getAttribute('data-size-id');
            const name = this.getAttribute('data-size-name');
            const weight = this.getAttribute('data-size-weight');
            editSize(id, name, weight);
        });
    });

    const deleteSizeButtons = document.querySelectorAll('.delete-size-btn');
    deleteSizeButtons.forEach(button => {
        button.addEventListener('click', function() {
            if (!confirm('Naozaj odstrániť túto veľkosť? Odstránenie môže ovplyvniť existujúce produkty.')) {
                return;
            }
            
            const sizeId = this.getAttribute('data-size-id');
            const row = this.closest('.size-row');
            const errorDiv = document.getElementById('error-message');
            const errorText = document.getElementById('error-text');

            errorDiv.classList.add('hidden');

            const csrfInput = document.getElementById('csrfToken');
            const csrfToken = csrfInput ? csrfInput.value : '';

            const formData = new FormData();
            if (csrfInput) {
                formData.append(csrfInput.name, csrfToken);
            }
            
            fetch('/admin/sizes/delete/' + sizeId, {
                method: 'POST',
                body: formData,
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    row.remove();

                    const tbody = document.querySelector('tbody');
                    if (tbody && tbody.querySelectorAll('.size-row').length === 0) {
                        tbody.innerHTML = '<tr><td colspan="3" class="p-8 text-center text-gray-400 italic">Neboli nájdené žiadne veľkosti.</td></tr>';
                    }
                } else {
                    errorText.textContent = data.error || 'Chyba pri odstraňovaní veľkosti.';
                    errorDiv.classList.remove('hidden');
                    errorDiv.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
                }
            })
            .catch(error => {
                errorText.textContent = 'Chyba pri odstraňovaní veľkosti.';
                errorDiv.classList.remove('hidden');
                errorDiv.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            });
        });
    });
});