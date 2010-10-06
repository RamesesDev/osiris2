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
            <li>${it}</li>
        <%}%>
        </ul>
        <br>
        <a href="refresh">Refresh</a>
    </body>
</html>