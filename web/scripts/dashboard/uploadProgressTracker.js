class UploadProgressTracker extends HTMLElement {
    static get observedAttributes() {
        return ['visible'];
    }

    style = `
    .container {
        position: fixed;
        bottom: 10px;
        right: 10px;
        background: red;
        width: 600px;
        height: 300px;
        border-radius: 5px;
        z-index: 999;
        padding: 20px;
        background: rgba(25, 25, 25, 0.7);
        backdrop-filter: blur(10px);
        transition: width 300ms, height 300ms, opacity 300ms, transform 300ms;
        transform: translateY(10px);
        opacity: 0;
        pointer-events: none;
        overflow: hidden;
    }

    .container.visible {
        opacity: 1;
        pointer-events: all;
        transform: translateY(0);
    }

    .container.collapsed {
        width: 30px;
        height: 30px;
    }

    .container__reveal {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        display: grid;
        place-items: center;
        opacity: 0;
        pointer-events: none;
        transition: opacity 300ms;
        background: rgba(25, 25, 25);
        z-index: 100;
        cursor: pointer;
    }

    .container.collapsed .container__reveal {
        opacity: 1;
        pointer-events: all;
    }

    .container * {
        margin: 0;
    }

    .container__header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding-bottom: 5px;
        margin-bottom: 10px;
        border-bottom: 1px solid rgba(100, 100, 100);
    }

    .container__header-button {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: transparent;
        border: 0;
        font-size: 20px;
        display: grid;
        place-items: center;
        transition: background 300ms;
        cursor: pointer;
    }

    .container__header-button:hover {
        background: rgba(255, 255, 255, 0.3);
    }

    .container__processing {
        display: flex;
        flex-direction: column;
        gap: 4px;
    }
    `;

    constructor() {
        super();

        const shadow = this.attachShadow({ mode: 'open' });

        const style = document.createElement('style');
        style.textContent = this.style;
        
        this.container = document.createElement('div');
        this.container.classList.add('container');
        this.container.innerHTML = `
        <div id="revealButton" class="container__reveal">
            <ion-icon name="cloud-upload-outline" size="large"></ion-icon>
        </div>
        <div class="container__header">
            <h3><ion-icon name="cloud-upload-outline" style="position: relative; top: 4px; margin-right: 5px;"></ion-icon> In progress</h3>
            <button id="collapseButton" type="button" class="container__header-button">
                <ion-icon name="close-outline"></ion-icon>
            </button>
        </div>
        <div id="progressItems" class="container__processing">
        </div>
        `;

        shadow.appendChild(style);
        shadow.appendChild(this.container);

        const collapseButton = shadow.querySelector('#collapseButton');
        const revealButton = shadow.querySelector('#revealButton');
        this.progressItems = shadow.querySelector('#progressItems');

        collapseButton.onclick = () => {
            this.container.classList.add('collapsed');
        };

        revealButton.onclick = () => {
            this.container.classList.remove('collapsed');
        };

        this.manager = {};
    }

    attributeChangedCallback(name, _, newValue) {
        switch(name) {
            case 'visible':
                if (newValue == null) {
                    this.container.classList.remove('visible');
                } else {
                    this.container.classList.add('visible');
                }
                break;
        }
    }

    setValue(id, { progress, filename }) {
        this.setAttribute('visible', '');

        let item = this.manager[id];
        if (!item) {
            item = document.createElement('upload-progress-item');
            this.progressItems.appendChild(item);
            this.manager[id] = item;
        }
        
        progress && item.setAttribute('progress', progress);
        filename && item.setAttribute('filename', filename);
    }

    removeItem(id) {
        const item = this.manager[id];
        if (item) {
            item.remove();
            delete this.manager[id];
        }

        if (Object.keys(this.manager) <= 0) {
            this.removeAttribute('visible');
        }
    }
}

class UploadProgressItem extends HTMLElement {
    static get observedAttributes() {
        return ['progress', 'filename'];
    }

    style = `
    .uploading-item {
        background: rgba(255, 255, 255, 0.05);
        padding: 10px 8px;
        border-radius: 5px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .uploading-item ion-icon {
        position: relative; 
        top: 3px;
    }
    `;

    constructor() {
        super();

        const shadow = this.attachShadow({ mode: 'open' });
        const style = document.createElement('style');
        style.textContent = this.style;

        shadow.innerHTML = `
        <div class="uploading-item">
            <div>
                <ion-icon name="document-outline"></ion-icon>
                <span id="text">Unknown</span>
            </div>
            <progress id="progress" max="100" value="0"></progress>
        </div>
        `;

        this.progress = shadow.querySelector('#progress');
        this.text = shadow.querySelector('#text');

        shadow.appendChild(style);
    }

    attributeChangedCallback(name, _, newValue) {
        switch(name) {
            case 'progress':
                this.progress.value = parseInt(newValue);
                break;
            case 'filename':
                this.text.textContent = newValue;
                break;
        }
    }
}

customElements.define('upload-tracker', UploadProgressTracker);
customElements.define('upload-progress-item', UploadProgressItem);