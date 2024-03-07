(function() {
    const fileLists = document.querySelectorAll('[data-list]');

    const undoShareButton = document.querySelector('#undoShareButton');
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

                undoShareButton.disabled = false;
            } else if (e.target.checked === false) {
                count--;

                if (count <= 0) {
                    undoShareButton.disabled = true;
                    count = 0;
                }
            }

            console.log(count);
        }

        const resetCount = function() {
            count = 0;
            undoShareButton.disabled = true;
        }

        return { setCount, resetCount };
    }();

    mainFileList.addEventListener('htmx:afterSettle', () => {
        toolbarManager.resetCount();
    });

    async function copy(id) {
        const url = new URL(window.location.href);
        const paths = url.pathname.split('/');
        paths.pop();
        paths.push('shared')

        url.pathname = paths.join('/');
        url.searchParams.append('id', id);
        await navigator.clipboard.writeText(url);

        alert('Link copied to clipboard');
    }

    function getSelectedFilesForm() {
        const form = new FormData();

        const selectedFiles = Array.from(
                document.querySelectorAll('.content li input[type="checkbox"]:checked')
        ).map(input => input.value).forEach(file => {
            form.append('files', file);
        });

        return form;
    }

    undoShareButton.addEventListener('click', async (e) => {
        const form = getSelectedFilesForm();
        form.append('action', 'undoShare');

        await fetch('files', {
            method: 'POST',
            body: form
        });

        refresh();
    });

    mainFileList.addEventListener('click', toolbarManager.setCount);

    window.sharing = {
        copy
    }
})()

