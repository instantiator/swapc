# swapc

`swapc` (prounounced 'swapsy') is a tool for finding certificates by criteria (in individual files, or inside yaml) and replacing them with a new certificate _in the same format._

## Building

The project will build in IntelliJ, and so will the tests.

### Build the JAR

To build the jar, use the `shadowJar` gradle action, either through IntelliJ or from the terminal:

```bash
./gradlew shadowJar
```

The jar can now be found at: `build/libs/swapc-1.0-SNAPSHOT-all.jar`

### Run

To run the jar, use:

```bash
java -jar build/libs/swapc-1.0-SNAPSHOT-all.jar
```

You'll see the standard help:

```bash
$ java -jar build/libs/swapc-1.0-SNAPSHOT-all.jar
Missing required parameters: '<verb>', '<path>'
Usage: swapc [-bdhr] [-c=<replacementCertFile>] [-f=<filters>]... <verb> <path>
Search and replace certificates recursively by attributes.
      <verb>               Command: find / replace
      <path>               Path to search.
  -b, --backup-files       Set to make backup copies of files before changing
                             them. (Default disabled.)
  -c, --replacement-cert=<replacementCertFile>
                           The certificate to apply for replacement (required
                             if 'replace' chosen).
  -d, --dry-run            If set, will not apply temporary changes to
                             permanent files. (Default disabled.)
  -f, --filter=<filters>   Apply a filter, eg. "invalid" or "invalid=YYYYMMDD",
                             or "valid", or "valid=YYYYMMDD"
  -h, --help               Show this help.
  -r, --recursive          Search recursively. (Default disabled.)
```

## What it does

`swapc` will:

* Search a file or directory you specify (optionally recursively) for certificates.
* Filter those certificates by criteria you provide.
* Note the location and format of the certificates it has found.
* Reformat a replacement certificate you provide, to match the format of each find, and swap it into place.

### Filters

The following filters are available:

* Valid right now: `-f valid`
* Valid on a date: `-f valid=YYYYMMDD`
* Invalid right now: `-f invalid`
* Invalid on a date: `-f invalid=YYYYMMDD`

_More are planned._

### File types

`swapc` will search files with the following suffixes:

* `yaml`, `yml`
* `cert`, `crt`, `pem`, `der`

### Certificate formats

Certificates come in a number of formats:

* PEM
  * Base64 encdoded.
  * Optional headers.
  * Optional line length.
  * Optional line break (spaces or new lines).
  * Optionally re-encoded with Base64.
* DER
  * Binary format.

### YAML files

YAML files can contain certificates anywhere inside them, encoded in one of the Base64 variants. `swapc` tracks the path inside the YAML to each certificate that it finds.

NB. YAML interprets new lines as spaces in Base64 encoded certificates. In some cases, it's important to preserve the line breaks, too - and a solution for this is to Base64 encode the whole certificate - including the headers and line breaks. `swapc` detects this and understands it as another certificate format.

## Sample uses

### Find

To search for all certificates in a folder recursively:

```bash
java -jar build/libs/swapc-1.0-SNAPSHOT-all.jar find -r path/to/folder
```

To search for all certificates that are invalid right now:

```bash
java -jar build/libs/swapc-1.0-SNAPSHOT-all.jar find -r -f=invalid path/to/folder
```

To search for all certificates that are invalid on 2020-01-01:

```bash
java -jar build/libs/swapc-1.0-SNAPSHOT-all.jar find -r -f invalid=20200101 path/to/folder
```

To search for all certificates that are valid right now:

```bash
java -jar build/libs/swapc-1.0-SNAPSHOT-all.jar find -r -f valid path/to/folder
```

To search for all certificates that are valid on 2020-01-01:

```bash
java -jar build/libs/swapc-1.0-SNAPSHOT-all.jar find -r -f valid=20200101 path/to/folder
```

__You may apply as many filters as you like by repeatedly using the -f option.__

### Replace

To replace all the certificates found inside a folder, with another:

```bash
java -jar build/libs/swapc-1.0-SNAPSHOT-all.jar replace -r -c=path/to/certificate path/to/folder
```

To do the same, but first as a dry-run:

```bash
java -jar build/libs/swapc-1.0-SNAPSHOT-all.jar replace -rd -c=path/to/certificate path/to/folder
```

To do the replacements, but first create a backup of any files that are going to be altered:

```bash
java -jar build/libs/swapc-1.0-SNAPSHOT-all.jar replace -rb -c=path/to/certificate path/to/folder
```
