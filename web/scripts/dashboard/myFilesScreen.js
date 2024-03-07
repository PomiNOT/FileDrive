(function() {
    const fileUploadInput = document.querySelector('#fileUploadInput');
    const fileUploadUI = document.querySelector('upload-tracker');
    const fileLists = document.querySelectorAll('[data-list]');

    const createFolderDialog = document.querySelector('#createFolderDialog');
    const createFolderInput = createFolderDialog.querySelector('input[name="folderName"]');
    const createFolderButton = createFolderDialog.querySelector('button[type="submit"]');

    const moveFilesDialog = document.querySelector('#moveFilesDialog');
    const moveFilesForm = moveFilesDialog.querySelector('form');
    const moveFilesButton = moveFilesDialog.querySelector('button[type="submit"]');

    const shareFileDialog = document.querySelector('#shareFileDialog');

    const moveButton = document.querySelector('#moveButton');
    const markTrashButton = document.querySelector('#markTrashButton');

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
                markTrashButton.disabled = false;
            } else if (e.target.checked === false) {
                count--;

                if (count <= 0) {
                    moveButton.disabled = true;
                    markTrashButton.disabled = true;
                    count = 0;
                }
            }
        }

        const resetCount = function() {
            count = 0;
            moveButton.disabled = true;
            markTrashButton.disabled = true;
        }

        return { setCount, resetCount };
    }();

    mainFileList.addEventListener('htmx:afterSettle', () => {
        toolbarManager.resetCount();
    });

    createFolderButton.addEventListener('click', async (e) => {
        const folderName = createFolderInput.value;
        const form = new FormData();
        const parent = document.querySelector('.content input[name="path"]')?.value ?? '-1';
        form.append('action', 'createFolder');
        form.append('folderName', folderName);
        form.append('parent', parent);

        await fetch ('files', {
            method: 'POST',
            body: form
        });

        refresh();
    });

    function getSelectedFilesForm() {
        const form = new FormData();

        const selectedFiles = Array.from(
                document.querySelectorAll('.content li input[type="checkbox"]:checked')
        ).map(input => input.value).forEach(file => {
            form.append('files', file);
        });

        return form;
    }

    moveFilesButton.addEventListener('click', async (e) => {
        const form = getSelectedFilesForm();

        const target = new FormData(moveFilesForm).get('selected');
        form.append('action', 'moveFiles');
        form.append('target', target);

        await fetch('files', {
            method: 'POST',
            body: form
        });

        refresh();
    });

    markTrashButton.addEventListener('click', async (e) => {
        const form = getSelectedFilesForm();
        form.append('action', 'markTrash');
        form.append('trashed', 'true');

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

        upload(file, parent) {
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
            formData.append('parent', parent);

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

        const parent = mainFileList.querySelector('input[name="path"]')?.value ?? '-1';
        const id = uploader.upload(file, parent);

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

    function share(id) {
        shareFileDialog.querySelector('input[name="fileId"]').value = id;
        shareFileDialog.showModal();
    }

    function editName(id) {
        const line = mainFileList.querySelector(`[data-item-id="${id}"`);
        if (!line) return;


        const text = line.querySelector('.content__table-item-name');
        text.setAttribute('contenteditable', true);
        text.focus();

        const origText = text.textContent;

        const onBlur = async () => {
            text.removeAttribute('contenteditable');
            text.removeEventListener('blur', onBlur);

            if (!text.textContent.trim()) {
                text.textContent = origText;
                return;
            }

            const formData = new FormData();
            formData.append('action', 'rename');
            formData.append('newName', text.textContent);
            formData.append('file', id);

            await fetch('files', {
                method: 'POST',
                body: formData
            });
        };

        text.addEventListener('blur', onBlur);
    }

    shareFileDialog.addEventListener('htmx:afterSettle', (e) => {
        shareFileDialog.close();
    });

    window.myFiles = {
        createFolder,
        moveFiles,
        goHome,
        chooseAndUploadFile,
        share,
        editName
    };
})();