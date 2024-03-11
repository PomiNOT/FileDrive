(function() {
    const fileLists = document.querySelectorAll('[data-list]');

    const undoTrashButton = document.querySelector('#undoTrashButton');
    const deleteItemsButton = document.querySelector('#deleteItemsButton');
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

                undoTrashButton.disabled = false;
                deleteItemsButton.disabled = false;
            } else if (e.target.checked === false) {
                count--;

                if (count <= 0) {
                    undoTrashButton.disabled = true;
                    deleteItemsButton.disabled = true;
                    count = 0;
                }
            }

            console.log(count);
        }

        const resetCount = function() {
            count = 0;
            undoTrashButton.disabled = true;
            deleteItemsButton.disabled = true;
        }

        return { setCount, resetCount };
    }();

    mainFileList.addEventListener('htmx:afterSettle', () => {
        toolbarManager.resetCount();
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

    undoTrashButton.addEventListener('click', async (e) => {
        const form = getSelectedFilesForm();
        form.append('action', 'markTrash');
        form.append('trashed', 'false');

        await fetch('files', {
            method: 'POST',
            body: form
        });

        refresh();
    });

    deleteItemsButton.addEventListener('click', async (e) => {
        const form = getSelectedFilesForm();
        form.append('action', 'completelyRemove');

        await fetch('files', {
            method: 'POST',
            body: form
        });

        refresh();
    });

    mainFileList.addEventListener('click', toolbarManager.setCount);
})();