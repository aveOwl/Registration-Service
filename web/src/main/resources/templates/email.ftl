<html>
<body>
<h1>Thank you for registration</h1>

<#assign picture_resource = "cid:mail-logo" />

<img src=${picture_resource} alt="mailPic" style="align-content: center; width: 200px; height: 200px" />

<div>
    <p>
        Your email address is <a href="mailto:${email}">${email}</a>
        and your pass ends on ${password}
    </p>
</div>

<div>
    <p>
        To confirm your account please click on this link:
    </p>
    <p>
        <a href=${confirmUrl}>${confirmUrl}</a>
    </p>
</div>
</body>
</html>