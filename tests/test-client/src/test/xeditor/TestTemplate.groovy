<html>
    <head>
        <style>
            body { font: 11pt/145% 'arial', 'tahoma', 'sans'; }
        </style>
    </head>
    <body>
        <b>Message from Template:</b>
        <br>
        <ul>
        <% items.each {%>
            <li><a href="click?${it}">${it}</a></li>
        <%}%>
        </ul>
        <br>
        <a href="refresh">Refresh</a>
    </body>
</html>