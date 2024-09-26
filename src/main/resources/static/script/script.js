

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

    const randomIndex = Math.floor(Math.random() * setGenreContets.length);
    const selectedGenre = setGenreContets[randomIndex];
    document.getElementById('genre-text').textContent = selectedGenre;
    console.log(setGenreContets)
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
    console.log(selectedOption)
    // 選ばれている要素を除外した新しい配列を作成
    let filteredTargets = setTargetContets.filter(target => target !== selectedOption);

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
window.onload = function() {
    //クイズ出題者のselectの初期値を設定
    let selectElement = document.getElementById('targets');
    let deleteSelectElement = document.getElementById('delete-target');
    for (let i = 0; i < setTargetContets.length; i++) {
        //クイズ出題者の選択メニュー
        let selectTargetOption = document.createElement('option');
        selectTargetOption.value = setTargetContets[i];
        selectTargetOption.text = setTargetContets[i];
        selectElement.appendChild(selectTargetOption);
        //削除するターゲットの選択メニュー
        let deleteTargetOption = document.createElement('option');
        deleteTargetOption.value = setTargetContets[i];
        deleteTargetOption.text = setTargetContets[i];
        deleteSelectElement.appendChild(deleteTargetOption);
    }
    //クイズジャンル削除のselectの初期値を設定
    let deleteGenre = document.getElementById('delete-genre');
    for (let i = 0; i < setGenreContets.length; i++) {
        let deleteGenreOption = document.createElement('option');
        deleteGenreOption.value = setGenreContets[i];
        deleteGenreOption.text = setGenreContets[i];
        deleteGenre.appendChild(deleteGenreOption);
    }
}


//削除ボタン押下でジャンルを消す
let deleteGenreButton = document.getElementById('delete-genre-button');
deleteGenreButton.addEventListener('click', function() {
    let deleteGenreSelect = document.getElementById('delete-genre');
    let selectedGenre = deleteGenreSelect.value;

    // setGenreContets配列から選ばれた要素を削除
    const index = setGenreContets.indexOf(selectedGenre);
    if (index > -1) {
        setGenreContets.splice(index, 1);
    }

    // selectの内容を更新
    while (deleteGenreSelect.firstChild) {
        deleteGenreSelect.removeChild(deleteGenreSelect.firstChild);
    }
    for (let i = 0; i < setGenreContets.length; i++) {
        let option = document.createElement('option');
        option.value = setGenreContets[i];
        option.text = setGenreContets[i];
        deleteGenreSelect.appendChild(option);
    }
    console.log(setGenreContets)
});

// 削除ボタン押下で選ばれたターゲットを消す
let deleteTargetButton = document.getElementById('delete-target-button');
deleteTargetButton.addEventListener('click', function() {
    let deleteTargetSelect = document.getElementById('delete-target');
    let selectedTarget = deleteTargetSelect.value;

    // setTargetContets配列から選ばれた要素を削除
    const index = setTargetContets.indexOf(selectedTarget);
    if (index > -1) {
        setTargetContets.splice(index, 1);
    }

    // selectの内容を更新
    while (deleteTargetSelect.firstChild) {
        deleteTargetSelect.removeChild(deleteTargetSelect.firstChild);
    }
    let selectElement = document.getElementById('targets');
    while (selectElement.firstChild) {
        selectElement.removeChild(selectElement.firstChild);
    }
    for (let i = 0; i < setTargetContets.length; i++) {
        let option1 = document.createElement('option');
        option1.value = setTargetContets[i];
        option1.text = setTargetContets[i];
        deleteTargetSelect.appendChild(option1);

        let option2 = document.createElement('option');
        option2.value = setTargetContets[i];
        option2.text = setTargetContets[i];
        selectElement.appendChild(option2);
    }
});

// 追加ボタンを取得
let addTargetButton = document.getElementById('add-target-button');
addTargetButton.addEventListener('click', function() {
    // 入力されたターゲットを取得
    let newTargetInput = document.getElementById('new-target');
    let newTarget = newTargetInput.value;

    // setTargetContets配列に新しいターゲットを追加
    setTargetContets.push(newTarget);

    // selectの内容を更新
    let deleteTargetSelect = document.getElementById('delete-target');
    let option1 = document.createElement('option');
    option1.value = newTarget;
    option1.text = newTarget;
    deleteTargetSelect.appendChild(option1);

    let selectElement = document.getElementById('targets');
    let option2 = document.createElement('option');
    option2.value = newTarget;
    option2.text = newTarget;
    selectElement.appendChild(option2);

    // 入力フィールドをクリア
    newTargetInput.value = '';
});

// 新しいジャンル追加ボタンを取得
// let addGenreButton = document.getElementById('add-genre-button');
// addGenreButton.addEventListener('click', function() {
//     // 入力されたジャンルを取得
//     let newGenreInput = document.getElementById('new-genre');
//     let newGenre = newGenreInput.value;

//     // setGenreContets配列に新しいジャンルを追加
//     setGenreContets.push(newGenre);

//     // selectの内容を更新
//     let deleteGenreSelect = document.getElementById('delete-genre');
//     let option = document.createElement('option');
//     option.value = newGenre;
//     option.text = newGenre;
//     deleteGenreSelect.appendChild(option);

//     // 入力フィールドをクリア
//     newGenreInput.value = '';
// });





// ジャンルの配列が変更されたときに呼び出される関数
function updateGenres() {
    let deleteGenreSelect = document.getElementById('delete-genre');
    // selectの内容を更新
    while (deleteGenreSelect.firstChild) {
        deleteGenreSelect.removeChild(deleteGenreSelect.firstChild);
    }
    for (let i = 0; i < setGenreContets.length; i++) {
        let option = document.createElement('option');
        option.value = setGenreContets[i];
        option.text = setGenreContets[i];
        deleteGenreSelect.appendChild(option);
    }
}



