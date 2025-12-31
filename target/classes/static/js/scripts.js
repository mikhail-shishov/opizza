document.addEventListener('DOMContentLoaded', function () {
    const deleteButtons = document.querySelectorAll('.delete-btn');
    deleteButtons.forEach(btn => {
        btn.addEventListener('click', function (e) {
            if (!confirm('Naozaj chcete odstrániť túto položku?')) {
                e.preventDefault();
            }
        });
    });

    function addImageRow() {
        const container = document.getElementById('image-container');
        const index = container.children.length;
        const html = `
            <div class="flex items-center gap-4 bg-gray-50 p-2 rounded border">
                <input type="text" name="imageUrls" placeholder="URL obrázka" class="flex-grow p-2 border rounded">
                <label class="flex items-center gap-1 text-xs">
                    <input type="radio" name="mainImageIndex" value="${index}"> Hlavný
                </label>
                <button type="button" onclick="this.parentElement.remove()" class="text-red-500">
                    <i class="fa fa-trash"></i>
                </button>
            </div>`;
        container.insertAdjacentHTML('beforeend', html);
    }
});