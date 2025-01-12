# son-editor::son-validate-web

The SONATA validator tool, son-validate-web, offers a web service that
allows to validate JSON files against a given JSON schema. By default 
son-validate-web checks against the standard JSON-Schema Draft-04. If 
the JSON file to check provides a _$schema_-key, the file is checked 
against that schema. You may also provide your own schema, which 
overrides the previous actions. The provided schema file must meet the 
JSON-Schema Draft-04 standard.

## Usage

You may find a bash script that runs starts the son-validate-web service
in './src/main/bash'. To start the web service you may type:

```
usage: son-validate-web [OPTIONS]
 -h,--help          Give this help list
 -p,--port <PORT>   The port the server listens to.
 ```
 
By default, the web service listens to port 4567.

### The Web Service

Once the service is started, you can verify JSON strings against a
given schema. To this end, you have to send a JSON of the following
format to the web service:

```
POST: /validate - application/json

{
  "file": {
    // Here goes your (mandatory) JSON to check.
  },
  "schema": {
    // Here goes your (optional) schema.
  },
  "options": {
     // Here go your (optional) options.
     "skip-schema-check": false,
     "skip-schema-key": true,
     "schema-key": "$schema"
  }
}
```

The service responds with OK (Status 200) if the JSON meets the given
schema. It returns an error code (Status 4xx) if something went
wrong.

For an example you might want to use _curl_: 

```
curl -vX POST http://localhost:4567/validate -d @../resources/data.json --header "Content-Type: application/json"
```

