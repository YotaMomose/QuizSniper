

let genreButton = document.getElementById('genre-button');
let targetButton = document.getElementById('target-button');

// ジャンル選択ボタンをクリックしたときの処理
genreButton.addEventListener('click', function() {
    let genreTextContainer = document.getElementById('genre-text-container');
    genreTextContainer.style.display = 'none';
    let loading = document.getElementById('genre-loading');
    loading.style.display = 'block';
    setTimeout(function() {
        loading.style.display = 'none';
        genreTextContainer.style.display = 'block';
    }, 500);

    const randomIndex = Math.floor(Math.random() * setGenreStringList.length);
    const selectedGenre = setGenreStringList[randomIndex];
    document.getElementById('genre-text').textContent = selectedGenre;
});

// ターゲット選択ボタンをクリックしたときの処理
targetButton.addEventListener('click', function() {

    let targetTextContainer = document.getElementById('target-text-container');
    targetTextContainer.style.display = 'none';
    let loading = document.getElementById('target-loading');
    loading.style.display = 'block';
    setTimeout(function() {
        loading.style.display = 'none';
        targetTextContainer.style.display = 'block';
    }, 500);

    // index.htmlのselectで選ばれている要素を取得
    let selectedOption = document.getElementById('targets').value;
    // 選ばれている要素を除外した新しい配列を作成
    let filteredTargets = setTargetStringList.filter(target => target !== selectedOption);

    const randomCount = Math.floor(Math.random() * (filteredTargets.length - 1)) + 1;
    const selectedTargets = [];
    for (let i = 0; i < randomCount; i++) {
        const randomIndex = Math.floor(Math.random() * filteredTargets.length);
        selectedTargets.push(filteredTargets[randomIndex]);
        filteredTargets.splice(randomIndex, 1);
    }
    document.getElementById('target-text').textContent = selectedTargets.join(', ');

    
});

//削除時の確認
function confirmDelete() {
    const checkboxes = document.querySelectorAll('input[name="deleteContentId"]:checked');
    
    if (checkboxes.length === 0) {
        alert("削除する項目を選んでください");
        return false; 
    }
    return confirm("本当に削除しますか？");
}

