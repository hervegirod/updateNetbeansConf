# updateNetbeansConf
Upadte with a Java program on the command linee the Netbeans configuration file. The program witll update the
etc.netbeans.conf file in the Netbeans installation

# Usage
Call updateNetbeansConf.jar with the following properties:
* "install": the install directory of Netbeans  
* "jdk": the JDK home directory
* "cache": the Netbeans cache directory (this directory can exist, but the "userDir" and "cacheDir" sub-directories must be empty

Examples:
```
java -jar updateNetbeansConf.jar jdk=C:\Programs\Java\jdk8.0 install=C:\Programs\Netbeans8.0 cache=D:\NBCache
```

# Licence
The updateNetbeansConf Library uses a BSD license for the source code.
