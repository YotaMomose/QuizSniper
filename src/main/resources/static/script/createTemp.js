let contentCount = 1;

function addTemplateContent() {
    contentCount++; 
    
    // 新しいdivを作成してラップする
    const newItemContainer = document.createElement('div');
    newItemContainer.classList.add('template-item');
    
    // ラベルを作成
    const newLabel = document.createElement('label');
    newLabel.textContent = '・';
    newLabel.htmlFor = `templateContent${contentCount}`;
    
    // 新しい入力フィールドを作成
    const newInput = document.createElement('input');
    newInput.type = 'text';
    newInput.name = 'templateContent';
    newInput.id = `templateContent${contentCount}`;
    newInput.required = true;

    // 新しいチェックボックスを作成
    const newCheckBox = document.createElement('input');
    newCheckBox.type = 'checkbox';
    newCheckBox.name = 'deleteContent';
    newCheckBox.value = contentCount;

    // div にラベル、入力フィールド、チェックボックスを追加
    newItemContainer.appendChild(newLabel);
    newItemContainer.appendChild(newInput);
    newItemContainer.appendChild(newCheckBox);
    
    // コンテナに新しいアイテムを追加
    const container = document.getElementById('templateContentContainer');
    container.appendChild(newItemContainer);
}

function deleteTemplateContent() {
    // 全てのチェックボックスを取得
    const checkboxes = document.querySelectorAll('input[name="deleteContent"]');

    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            // チェックされたチェックボックスの親の .template-item を削除
            const templateItem = checkbox.closest('.template-item');
            if (templateItem) {
                templateItem.remove();
            }
        }
    });
}
