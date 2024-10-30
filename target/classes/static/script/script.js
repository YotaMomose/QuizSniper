

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


//selectの初期値を設定する
//クイズ出題者の選択メニュー・削除するジャンルの選択メニュー・削除するターゲットの選択メニューの３箇所を設定する
// window.onload = function() {
//     //クイズ出題者のselectの初期値を設定
//     let selectElement = document.getElementById('targets');
//     let deleteSelectElement = document.getElementById('delete-target');
//     for (let i = 0; i < setTargetStringList.length; i++) {
//         //クイズ出題者の選択メニュー
//         let selectTargetOption = document.createElement('option');
//         selectTargetOption.value = setTargetStringList[i];
//         selectTargetOption.text = setTargetStringList[i];
//         selectElement.appendChild(selectTargetOption);
//         //削除するターゲットの選択メニュー
//         let deleteTargetOption = document.createElement('option');
//         deleteTargetOption.value = setTargetStringList[i];
//         deleteTargetOption.text = setTargetStringList[i];
//         deleteSelectElement.appendChild(deleteTargetOption);
//     }
//     //クイズジャンル削除のselectの初期値を設定
//     let deleteGenre = document.getElementById('delete-genre');
//     for (let i = 0; i < setGenreStringList.length; i++) {
//         let deleteGenreOption = document.createElement('option');
//         deleteGenreOption.value = setGenreStringList[i];
//         deleteGenreOption.text = setGenreStringList[i];
//         deleteGenre.appendChild(deleteGenreOption);
//     }
// }








// ジャンルの配列が変更されたときに呼び出される関数
// function updateGenres() {
//     let deleteGenreSelect = document.getElementById('delete-genre');
//     // selectの内容を更新
//     while (deleteGenreSelect.firstChild) {
//         deleteGenreSelect.removeChild(deleteGenreSelect.firstChild);
//     }
//     for (let i = 0; i < setGenreStringList.length; i++) {
//         let option = document.createElement('option');
//         option.value = setGenreStringList[i];
//         option.text = setGenreStringList[i];
//         deleteGenreSelect.appendChild(option);
//     }
// }



