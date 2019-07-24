# Spring Hibernate Entity Encryption

This library will allow you to to easily store data in encrypted format.
It will store the attributes of your choice encrypted in your database and 
gives you the decrypted version when you need to read it. 

Here's the Loopback version of the library https://github.com/mekuanent/loopback-encryption-mixin


##Installation

For maven based projects

```$xslt
<dependency>
  <groupId>com.github.mekuanent</groupId>
  <artifactId>spring-hibernate-entity-encryption</artifactId>
  <version>1.0.0</version>
</dependency>
```

For gradle based projects

```$xslt
implementation 'com.github.mekuanent:spring-hibernate-entity-encryption:1.0.0'
```

You can check out this url for other builds https://search.maven.org/artifact/com.github.mekuanent/spring-hibernate-entity-encryption/1.0.0/jar

##Setup

Add the following annotation to import the encryption configuration class.

```$xslt
...
@Import(EnableEncryptionConfig.class)
public class Application {
...
```

After that you need to specify your encryption key, salt,... globally.
To do that you need to write the following in your application's
main method.

```$xslt
EncryptionHandler.set(new PBEHandler("<your password>",
                "<your salt>", "<your IV>", <iteration>, 
                <derived key length (optional with default 256)>));
```

Next, put ```@Encrypted ``` annotation on all fields of the entities you want to be encrypted when stored.

E.g.

```$xslt
@Encrypted
private String title;
```

**That's it, you're all set.**

##Custom Encryption Algorithm

If you don't want to use the default encryption scheme. you can define your own by creating an ```@Component``` annotated class implementing ```IEncryptionHandler```

E.g.

```$xslt
@Component
public class CustomEncryptionHandler implements IEncryptionHandler {

    @Override
    public String encrypt(String raw) {}
    
    @Override
    public String decrypt(String cipherText) {}

}
```

There are two ways of setting your custom encryption handler, 

**_setting handler Globally_**

you can set it up in the main method of your application. 
```$xslt
EncryptionHandler.set(new CustomEncryptionHandler("<your password>",
                "<your salt>", "<your IV>", <iteration>, 
                <derived key length (optional with default 256)>));

```

**_setting handlers for every field_**

In this case, your custom encryption handler is required to have an empty constructor.

You can set your handler to the field of your choice by just including it as a handler parameter

E.g.

```$xslt
@Encrypted(handler = CustomEncryptionHandler.class)
private String description;
```

##Resources
you can checkout the complete sample application here: https://github.com/mekuanent/SpringEntityEncryptionLibExample

##License
Spring Hibernate Entity Encryption is released under the terms of the Apache Software License Version 2.0 (see license.txt).