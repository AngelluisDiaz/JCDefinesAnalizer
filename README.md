# JCDefinesAnalizer
Java library to get the constants created with #define directive from a *.h file.

## Goals
This library makes the testing easier. It is created to allow to test recognize itself his applicability reading directly from the source.

If the software under test is a c language project and it has *.h files wich describes the binary behaviour and you have to test differents builts with differentes behaviours, this is your most useful library to make your testing framework really automatic. 

The goal is to have a test repository where the tests are going to be executed if the software under test implements a particular functionality, i.e. if it is compiled with a particular define/constant enabled/included.

