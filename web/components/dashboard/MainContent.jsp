<%@page contentType="text/html" pageEncoding="UTF-8"%>

<form
    data-list
    hx-target="this"
    hx-get="files"
    hx-trigger="load, refresh"
    hx-ext="morph"
    hx-swap="morph:innerHTML"
    class="content"
    id="mainFileList"
>
    <div class="content__loading">
        <div class="lds-spinner"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>
    </div>
</form>