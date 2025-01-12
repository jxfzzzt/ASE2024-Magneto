# son-editor [![Build Status](https://jenkins.sonata-nfv.eu/buildStatus/icon?job=son-editor)](https://jenkins.sonata-nfv.eu/job/son-editor/)

For the SONATA editor we envision an Eclipse or IntelliJ plugin that supports function and service developers. This, however, is our vision on the long run. For now, this repository contains some Java libraries and tools that validate SONATA descriptors using the SONATA schema.

## Development

To contribute to the development of the SONATA editor, you may use the very same development workflow as for any other SONATA Github project. That is, you have to fork the repository and create pull requests.

#### Contributing

You may contribute to the editor similar to other SONATA (sub-) projects, i.e. by creating pull requests.

#### Building

The SONATA editor is written in Java and uses the Maven build tool. Thus, you can build the whole editor by typing:

```
 $ mvn clean package
```

In addition (and because I am a lazy person), there is a Makefile that allows you to build the whole projects, and also sub-projects, like the *-cli and *-web sub-projects, individually.

```
 $ make son-validate-cli
```

#### Dependencies

The son-editor relies on some great libraries that have been written before. Below you can find a list of these libraries including their version and license.

* [Logback 1.1.3](http://logback.qos.ch/) (EPL 1.0)
* [Apache Commons CLI](https://commons.apache.org/proper/commons-cli/) 1.3.1 (Apache 2.0)
* [Apache Commons IO](https://commons.apache.org/proper/commons-io/) 2.4 (Apache 2.0)
* [JUnit](http://junit.org) 4.12 (EPL 1.0) 
* [Google Guice DI](https://github.com/google/guice) 4.0 (Apache 2.0)
* [Guice JUnit Test Runner](https://github.com/caarlos0/guice-junit-test-runner) 1.1 (MIT)
* [SnakeYAML](https://bitbucket.org/asomov/snakeyaml) 1.16 (Apache 2.0)
* [JSON-Java](https://github.com/stleary/JSON-java) 20160212 ([The JSON License](http://www.json.org/license.html))
* [JSON-Sanitizer](https://www.owasp.org/index.php/OWASP_JSON_Sanitizer) 1.1 (Apache 2.0)
* [JSON Schema Validator](http://www.everit.org/) 1.1.1 (Apache 2.0)
* [FasterXML Jackson](https://github.com/FasterXML/jackson) 2.7.1 (Apache 2.0)
* [Spark Core](http://sparkjava.com/) 2.3 (Apache 2.0)
 
## Usage

You may find examples on how to use the different tools in the corresponding sub-directories.


## License

The license of the SONATA editor is BSD for now. However, we intend to change it to Apache 2.0 later.

---
#### Lead Developers

The following lead developers are responsible for this repository and have admin rights. They can, for example, merge pull requests.

- Michael Bredel (mbredel)

#### Feedback Channel

Please use the GitHub issues and the SONATA development mailing list sonata-dev@lists.atosresearch.eu for feedback.

