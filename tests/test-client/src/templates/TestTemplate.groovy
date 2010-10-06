<html>
    <body>
        <b>Message from Template:</b><br>
        <ul>
        <% items.each {%>
            <li>${it}</li>
        <%}%>
        </ul>
    </body>
</html>