const fileUploadInput = document.querySelector('#fileUploadInput');
const fileUploadUI = document.querySelector('upload-tracker');
const fileLists = document.querySelectorAll('[data-list]');

const createFolderDialog = document.querySelector('#createFolderDialog');
const createFolderInput = createFolderDialog.querySelector('input[name="folderName"]');
const createFolderButton = createFolderDialog.querySelector('button[type="submit"]');

const moveFilesDialog = document.querySelector('#moveFilesDialog');
const moveFilesForm = moveFilesDialog.querySelector('form');
const moveFilesButton = moveFilesDialog.querySelector('button[type="submit"]');

const moveButton = document.querySelector('#moveButton');
const mainFileList = document.querySelector('#mainFileList');

function refresh() {
    setTimeout(() => {
        fileLists.forEach(list => htmx.trigger(list, 'refresh'));
        console.log('Reloading..');
    }, 500);
}

const toolbarManager = function() {
    let count = 0;

    const setCount = function(e) {
        if (e.target.checked === true) {
            count++;

            moveButton.disabled = false;
        } else if (e.target.checked === false) {
            count--;

            if (count <= 0) {
                moveButton.disabled = true;
                count = 0;
            }
        }

        console.log(count);
    }

    const resetCount = function() {
        count = 0;
        moveButton.disabled = true;
    }

    return { setCount, resetCount };
}();

mainFileList.addEventListener('htmx:afterSettle', () => {
    toolbarManager.resetCount();
});

createFolderButton.addEventListener('click', async (e) => {
    const folderName = createFolderInput.value;
    const form = new FormData();
    form.append('action', 'createFolder');
    form.append('folderName', folderName);

    await fetch ('files', {
        method: 'POST',
        body: form
    });

    refresh();
});

moveFilesButton.addEventListener('click', async (e) => {
    const target = new FormData(moveFilesForm).get('selected');
    const form = new FormData();

    form.append('action', 'moveFiles');
    form.append('target', target);

    const selectedFiles = Array.from(
            document.querySelectorAll('.content li input[type="checkbox"]:checked')
    ).map(input => input.value).forEach(file => {
        form.append('files', file);
    });

    await fetch('files', {
        method: 'POST',
        body: form
    });

    refresh();
});

mainFileList.addEventListener('click', toolbarManager.setCount);

class FileUploader {
    constructor(targetURL) {
        this.map = new Map();
        this.callbacks = new Map();
        this.targetURL = targetURL;
    }

    upload(file) {
        const id = Math.floor(Math.random() * Number.MAX_SAFE_INTEGER);
        const xhr = new XMLHttpRequest();

        xhr.upload.onload = () => {
            if (xhr.status === 200) {
                this.callCallback(id, 'done', true);
            } else {
                this.callCallback(id, 'done', false);
            }
            this.map.delete(id);
        };

        xhr.upload.onerror = () => {
            this.callCallback(id, 'done', false);
        }

        xhr.upload.onprogress = (event) => {
            if (event.lengthComputable) {
                const progress = event.loaded / event.total;
                this.callCallback(id, 'progress', progress);
            }
        };

        const formData = new FormData();
        formData.append('file', file, file.name);

        xhr.open('POST', this.targetURL);
        xhr.send(formData);

        this.map.set(id, xhr);
        return id;
    }

    on(id, type, callback) {
        const key = `${id}-${type}`;
        this.callbacks.set(key, callback);
    }

    callCallback(id, type, data) {
        const key = `${id}-${type}`;
        const callback = this.callbacks.get(key);
        if (callback) {
            callback(data);
        }
    }
}

const uploader = new FileUploader('files');

function chooseAndUploadFile(e) {
    if (!fileUploadInput) console.error('file upload input cannot be found');

    fileUploadInput.click();
}

fileUploadInput.addEventListener('change', (e) => {
    const [file] = fileUploadInput.files;

    const id = uploader.upload(file);

    uploader.on(id, 'progress', (progress) => {
        fileUploadUI.setValue(id, { progress: progress * 100, filename: file.name });
    });

    uploader.on(id, 'done', (success) => {
        fileUploadUI.removeItem(id);
        console.log(success);

        refresh();
    });
});

function createFolder() {
    createFolderDialog.showModal();
}

function moveFiles() {
    htmx.trigger('#moveFilesForm', 'refresh');
    moveFilesDialog.showModal();
}

function goHome() {
    document.querySelector('.content input[name="path"]').value = 'root';
    htmx.trigger('.content', 'refresh');
}