# CHANGELOG

## 0.5 (2020): bug fix and refactoring release

## 0.4 (2019): bug fix and refactoring release

- **Fix**
  - refactor to use maven

## 0.3 (2007-04-15)

- **Feature**
  - xmltocsv, finftocsv
  - dmlexptab, xmltodml, finftodml: generate sql (dml) scripts
  - xmlimptab, finfimptab:
    - new option "--sync":  to synchronize changes from the webrowset document 
      ( using insertRow-,modifyRow-,deleteRow-tags ), back to the datasource
    - new option "--all":  import and synchronize changes
  - xmltodml, finftodml:
    - new option "--sync":  create sql script to synchronize changes from the webrowset 
      document back to the datasource
    - new option "--all":   create sql script to import and to synchronize changes

## 0.2.2 (2006-12-03): bug fix and refactoring release

- **Fix**
  - xmltofinf, finftoxml: wildcard support
  - stax: changed to http://stax.codehaus.org/
  - csvimptab: workaround for drivers that do not have ParameterMetaData.getParameterType

## 0.2.1 (2006-11-19): bug fix and refactoring release

- **Fix**
  - fixed: sorting of tables into database sequence order
  - fixed: webrowset: date/time/timestamp value should be written as the number of 
     milliseconds since January 1, 1970 UTC.
     (reading of iso date format is supported further on
  - fixed: wrong command line option causes a nullpointer exception
  - fixed: broken csv import
  - changed all imports: default batchsize is now set to 50
  - changed env-cfg.* to add more heap space
  
## 0.2 (2006-11-07)

- **Feature**
  - xmiimptab, finfimptab and csvimptab: added Option "--maprelaxed"
      skip columns not found in the target table(s)
  - xmiimptab, finfimptab and csvimptab: added Option "--nosort"
      do not sort the tables into database sequence order
      this may result in foreign key constraint violations
- **Fix**
  - fixed NullPointerException (importing file without any row)

## 0.1 (2006-10-28): initial release
