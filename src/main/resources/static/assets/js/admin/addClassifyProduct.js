let classifyIndex = 1;

function addClassify() {
    const classifySection = document.getElementById('classify-section');
    const classifyItem = document.createElement('div');
    classifyItem.classList.add('classify-item', 'mb-4', 'p-4', 'border', 'border-gray-300', 'rounded');
    classifyItem.innerHTML = `
        <div class="mb-4">
            <label class="block text-gray-700">Color</label>
            <input type="text" name="classifies[${classifyIndex}].color" class="border border-gray-300 rounded px-4 py-2 w-full" placeholder="Enter color">
        </div>
        <div class="mb-4">
            <label class="block text-gray-700">Images</label>
            <input type="file" name="classifies[${classifyIndex}].images" class="border border-gray-300 rounded px-4 py-2 w-full" multiple>
        </div>
        <div class="mb-4">
            <label class="block text-gray-700">Size</label>
            <input type="text" name="classifies[${classifyIndex}].size" class="border border-gray-300 rounded px-4 py-2 w-full" placeholder="Enter size">
        </div>
        <div class="mb-4">
            <label class="block text-gray-700">Quantity</label>
            <input type="text" name="classifies[${classifyIndex}].quantity" class="border border-gray-300 rounded px-4 py-2 w-full" placeholder="Enter quantity">
        </div>
        <button type="button" class="bg-red-500 text-white px-4 py-2 rounded" onclick="removeClassify(this)">Remove Classify</button>
    `;
    classifySection.appendChild(classifyItem);
    classifyIndex++;
}

function removeClassify(button) {
    const classifyItem = button.parentElement;
    classifyItem.remove();
    updateClassifyIndices();
}

function updateClassifyIndices() {
    const classifyItems = document.querySelectorAll('.classify-item');
    classifyItems.forEach((item, index) => {
        item.querySelector('[name^="classifies["][name$="].color"]').name = `classifies[${index}].color`;
        item.querySelector('[name^="classifies["][name$="].images"]').name = `classifies[${index}].images`;
        item.querySelector('[name^="classifies["][name$="].size"]').name = `classifies[${index}].size`;
        item.querySelector('[name^="classifies["][name$="].quantity"]').name = `classifies[${index}].quantity`;
    });
    classifyIndex = classifyItems.length;
}
