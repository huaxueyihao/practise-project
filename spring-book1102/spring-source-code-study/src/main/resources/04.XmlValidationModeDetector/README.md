XmlValidationModeDetector(xml校验模式探测器)

## 1.简介

> 这个类比较简单，就是单一的类，目的是根据Resouce(xml源文件流)，判断该xml是xsd的还是dtd的


## 2.XmlBeanDefinitionReader.doLoadDocument(InputSource inputSource, Resource resource)跟踪


```

/**
 * Actually load the specified document using the configured DocumentLoader.
 * @param inputSource the SAX InputSource to read from
 * @param resource the resource descriptor for the XML file
 * @return the DOM Document
 * @throws Exception when thrown from the DocumentLoader
 * @see #setDocumentLoader
 * @see DocumentLoader#loadDocument
 */
protected Document doLoadDocument(InputSource inputSource, Resource resource) throws Exception {
    // 注意getValidationModeForResource(resource)方法是切入点
    return this.documentLoader.loadDocument(inputSource, getEntityResolver(), this.errorHandler,
            getValidationModeForResource(resource), isNamespaceAware());
}


// 获取验证模式
protected int getValidationModeForResource(Resource resource) {
    // 
    int validationModeToUse = getValidationMode();
    if (validationModeToUse != VALIDATION_AUTO) {
        return validationModeToUse;
    }
    // 核心方法
    int detectedMode = detectValidationMode(resource);
    if (detectedMode != VALIDATION_AUTO) {
        return detectedMode;
    }
    // Hmm, we didn't get a clear indication... Let's assume XSD,
    // since apparently no DTD declaration has been found up until
    // detection stopped (before finding the document's root tag).
    return VALIDATION_XSD;
}


public int getValidationMode() {
    return this.validationMode;
}


protected int detectValidationMode(Resource resource) {
    if (resource.isOpen()) {
        throw new BeanDefinitionStoreException(
                "Passed-in Resource [" + resource + "] contains an open stream: " +
                "cannot determine validation mode automatically. Either pass in a Resource " +
                "that is able to create fresh streams, or explicitly specify the validationMode " +
                "on your XmlBeanDefinitionReader instance.");
    }

    InputStream inputStream;
    try {
        inputStream = resource.getInputStream();
    }
    catch (IOException ex) {
        throw new BeanDefinitionStoreException(
                "Unable to determine validation mode for [" + resource + "]: cannot open InputStream. " +
                "Did you attempt to load directly from a SAX InputSource without specifying the " +
                "validationMode on your XmlBeanDefinitionReader instance?", ex);
    }

    try {
        // 这里跳到了XmlValidationModeDetector这个类进行处理
        return this.validationModeDetector.detectValidationMode(inputStream);
    }
    catch (IOException ex) {
        throw new BeanDefinitionStoreException("Unable to determine validation mode for [" +
                resource + "]: an error occurred whilst reading from the InputStream.", ex);
    }
}



```


### 2.XmlValidationModeDetectordetectValidationMode(InputStream inputStream)方法

```

public int detectValidationMode(InputStream inputStream) throws IOException {
    // Peek into the file to look for DOCTYPE.
    // 
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    try {
        boolean isDtdValidated = false;
        String content;
        while ((content = reader.readLine()) != null) {
            content = consumeCommentTokens(content);
            if (this.inComment || !StringUtils.hasText(content)) {
                continue;
            }
            // 判断读取的字符串中是否有DOCTYPE，有就是dtd
            if (hasDoctype(content)) {
                isDtdValidated = true;
                break;
            }
            if (hasOpeningTag(content)) {
                // End of meaningful data...
                break;
            }
        }
        return (isDtdValidated ? VALIDATION_DTD : VALIDATION_XSD);
    }
    catch (CharConversionException ex) {
        // Choked on some character encoding...
        // Leave the decision up to the caller.
        return VALIDATION_AUTO;
    }
    finally {
        reader.close();
    }
}



```

## 3 总结

> XmlValidationModeDetector这个类就是为了获得验证模式是dtd，还是xsd的，职责单一
