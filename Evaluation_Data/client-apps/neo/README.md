# NEO

`neo` is a project scaffolding tool that can create ready-to-use projects based on templates. It is somewhat
like `yeoman` but different too. First, there are no dependencies to the project, except the requirement of
a `Java Virtual Machine`. 

## Birth of Neo?

As I start working on **Adobe Experience Manager** big time, I realize that the only project scaffolding tool
that is available is `lazybones` which again is dependent on `sdkman`. `sdkman` is written in `shell`,
`cucumber`, and `grovy` and thus is very tightly tied to `*nix` systems. This in turn leaves that `lazybones`
itself can only run on `*nix*` or `*OSX*` systems. As many developers within the **Adobe Experience Manager** community,
use Windows for development, they are unable to utilize the power of `lazybones`.

This led to the birth of `neo` - which being entirely written in Java - can be used across all platforms.

## Why Neo?

Why do I had to write `neo` when I could have added a `yeoman` based **AEM** template. Many reasons:

* I haven't worked with `yeoman` before.
* Its based on `nodejs` that I don't have on my machine
* **AEM** is built on `Java` and thus a Java solution makes sense
* Security out of the box 
* Allows for templates to be used from github/bitbucket/gitlab and more
* Last but not the least, I love to code

To achieve security, neo only provides basic commands/directives for a script to work. All actions like
reading a file, writing a file, managing files/directories can happen only within the project folder specified.
One cannot just go outside the project folder to work.

There is no need to use the central repository that indexes all templates. You know of a repository on
Github/Bitbucket/Gitlab - specify the url and use the template directly.

## Features

* Create a project using `neo.json` scaffolding template
* Install a template from local machine
* Download a template from Github.com, or Bitbucket.org
* Filenames can themselves be variables


## Road Map

Please refer to open issues at https://github.com/sangupta/neo/labels/enhancement for all the planned enhancements.

## Usage

Using `neo` is pretty simple. In the following examples, `neo` will mean `java -jar neo.jar`:

### Creating a project

```
$ neo create -t aem62 c:/newProjectFolder
```

### Installing a template from local machine

```
$ neo install c:/templates/newTemplate
```

### Downloading a template from internet

```
$ neo download github:sangupta/neo-templates/aem62
```

### Listing all available templates in cache

```
$ neo list
```

## Developing NEO templates

This section describes how you can build `neo` templates for project scaffolding.


## How does it work?

* A template is defined by presence of a `neo.json` file
* The file contains user parameters to be read, and processing actions that need to be performed
* Based on the template, all user input parameters are read
* Everything in the `data` folder (relative to `neo.json` file) is copied to project folder
* All files that have an extension of `txt`, `md`, `json`, `xml` are processed via Velocity for property embedding
* Once the `data` folder is copied, process execution starts
* All actions are performed sequentially as present in the template file

## Available commands

* addProperty - adds a property to current context
* copyDir - copy directory from base folder to project folder
* copyFile - copy file from base folder to project folder, without variable injection
* copyTemplate - copy file from base folder to project folder, without variable injection
* copyTemplateDirfile from base folder to project folder, with variable injection
* log - log the following line to logs
* mkdir - create a new directory in the project folder
* moveDir - move a directory within the project folder
* moveFile - move a file within the project folder
* printContext - print all context properties on console
* print - print a line to console
* rmdir - remove a directory from the project folder
* rmFile - remove a file from the project folder
* writeFile - write a new file in the project folder, without variable injection

## TODO commands

* zip
* unzip
* wget


## License

```
neo - project scaffolding tool
Copyright (c) 2016, Sandeep Gupta

http://sangupta.com/projects/neo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
