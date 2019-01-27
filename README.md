# jdbmigr

## Introduction

**jdbmigr** is a Java(JDBC) command-line tools to export and import database data as XML, Fast-Infoset, CSV or DML.
XML and Fast-Infoset are using the Webrowset schema, so you can synchronize changes in the webrowset document (insertRow-, modifyRow- and deleteRow-tags) back to the datasource.
This tool is aimed at an audience of developers or administrators who need to quickly transfer data between different DBMS using roughly the same schema.

This was my first open source project (2006-2007) distributed from [sourceforge.net](https://sourceforge.net/projects/jdbmigr/)

btw and this was my first attempt programming in Java ;) and sorry, this project does not have any automatic tests yet.

> NOTE: PRs Welcome!

## License

**jdbmigr** is released under the terms of the MIT License. A copy of the License is provided in the LICENSE file.

## Contacts

[github.com](https://github.com/gms1/jdbmigr)

## Installation

just unzip to a new directory

### Prerequisites

- Java 1.8+
- A JDBC Driver with Support for Batch-Inserts (PreparedStatement)

## Build from Source with Maven

```Bash
gms@orion:~/work/jdbmigr/src (master)$ mvn clean package
...
[INFO] Building tar : /home/gms/work/jdbmigr/src/jdbmigr/target/jdbmigr-0.4-SNAPSHOT-bin.tar.gz
[INFO] Building zip: /home/gms/work/jdbmigr/src/jdbmigr/target/jdbmigr-0.4-SNAPSHOT-bin.zip
[INFO] Building tar : /home/gms/work/jdbmigr/src/jdbmigr/target/jdbmigr-0.4-SNAPSHOT-src.tar.gz
[INFO] Building zip: /home/gms/work/jdbmigr/src/jdbmigr/target/jdbmigr-0.4-SNAPSHOT-src.zip
...
gms@orion:~/work/jdbmigr/src (master)$
```

if you are on Linux and want to have a test installation ready for use:

```Bash
gms@orion:~/work/jdbmigr (master)$ ./build/install
```

Additional artifacts (e.g drivers, datasource.xml) can be placed in the 'external' directory so they are automatically added to the 'install' directory

## Configuration

### Customize the configuration file "etc/datasource.xml"

This configuration-file sets the details on the datasources you are going to connect to.

The command "dbping" can be used to test your configuration:

In a command prompt, type:

```Bash
dbping -U username -P password datasourcename
```

> NOTE: If you are providing the jdbc url, please enter the driver name instead of the datasource name:

```Bash
dbping -u url -U username -P password drivername
```


### Supported Formats and corresponding program options

- csv (comma-separated values), with following options

  - column delimiter:                                      --coldel column-delimiter
  - row delimiter:                                         --rowdel row-delimiter
  - disable character quoting:                             --no-quotes
  - do not escape the quote character using double quotes: --no-doublequotes
  - disable column header:                                 --no-columnheader

> BINARY, VARBINARY, LONGBINARY and BLOB are NOT supported

- XML using the "WebRowSet XML Schema (by Jonathan Bruce (Sun Microsystems Inc.))"

> BINARY, VARBINARY, LONGBINARY and BLOB are supported (base64 encoding)

- Fast Infoset Document (Binary XML) using the "WebRowSet XML Schema (by Jonathan Bruce (Sun Microsystems Inc.))"

> BINARY, VARBINARY, LONGBINARY and BLOB supported (base64 encoding)

### How to export

- To export a result set of specific query:

```Bash
xmlexpqry -o outputfile -U username -P password datasourcename "select ..."
```

- To export a set of tables (using schema- and table- pattern)

```Bash
xmlexptab -s schemapattern -t tablepattern -U username -P password datasourcename
```

- To export a set of tables (using a filelist)

```Bash
xmlexptab --filelist filelist -U username -P password datasourcename
```

### How to import

The table list will be sorted, to avoid foreign key constraint violations

- To import a set of tables (using schema- and table- pattern)

```Bash
xmlimptab -s schemapattern -t tablepattern -U username -P password datasourcename
```

- To import a set of tables (using a filelist)

```Bash
xmlimptab --filelist filelist -U username -P password datasourcename
```

- you can modify the webrowset document using insertRow-,modifyRow- and deleteRow-tags
 and synchronize this changes back to the datasource

  - use the "--sync" option to synchronize insertRow-, modifyRow- and deleteRow-tags with the datasource
  - use the "--all" option, to import all and synchronize changes

### How to create a filelist

```Bash
dbtables -s schemapattern1 -t tablepattern1 -U username -P password datasourcename >filelist
dbtables -s schemapattern2 -t tablepattern2 -U username -P password datasourcename >>filelist
```

you can customize the generated filelist:
simple csv format (without character quoting):

- column 1 (mandatory): schemaname.tablename
- column 2 (optional): input/output filename
- column 3 (optional): select statement

### How to convert a Fast Infoset Document to XML and vice versa

```Bash
finftoxml inputfile.finf outputfile.xml
xmltofinf inputfile.xml outputfile.finf
```