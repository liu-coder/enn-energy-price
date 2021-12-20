<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
<form id="fileUploadForm" name="fileUploadForm" enctype="multipart/form-data" action="${request.contextPath}/cityPlant/fileUpload.action" method="post">
    <div>
        <div>
            <input type="file" id="file"  name="file"/>
                <a class="tijiao on1 mlmr0" href="##" onclick="javascript:fileUpload();"><span>批量导入</span></a>

        </div>
    </div>
</form>
</body>
</html>