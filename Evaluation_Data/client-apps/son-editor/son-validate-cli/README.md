# son-editor::son-validate-cli

The SONATA validator tool, son-validate-cli, allows to validate YAML or JSON files against a given JSON schema,
that again can be provided in either JSON or YAML. By default son-validate-cli checks against the standard
JSON-Schema Draft-04. If the YAML/JSON file to check has provides a _$schema_-key, the file is checked against
that schema. You may also provide your own schema, which overrides the previous actions. The provided schema file
must meet the JSON-Schema Draft-04 standard.

## Usage

You may find a bash script that runs son-validate in './src/main/bash'.

```
usage: son-validate [OPTIONS] FILE
 -c,--colored-output            Have colored output, i.e. green for
                                success, and red for errors.
 -d,--skip-schema-check         Skip the schema check. Default is 'false'.
 -h,--help                      Give this help list.
 -k,--schema-key <SCHEMA_KEY>   The key that is used to identify the
                                schema URI in FILE. Default is '$schema'.
 -s,--schema <SCHEMA_FILE>      A schema file used to check FILE.
 -v,--verbose                   Increase the verbose level.
 ```
