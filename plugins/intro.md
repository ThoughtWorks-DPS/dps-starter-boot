# Gradle Mixins

NOTE: This readme is generated from the plugin source files.
Do not edit directly.

## README.md Construction

The README.md is constructed from the plugin source and uses snippet files to piece the content together.
The snippet files are located in the `src/main/resource/gradle` folder.
The `toptick` and `bottomtick` files bracket the plugin code with Markdown code blocks.

The `header` file provides the header formatting, to which the plugin filename is appended.
Note, the `header` file **MUST NOT** have a trailing newline character.
If you edit the file with `vi` or Intellij, they will add the trailing newline.
In this situation, it is simple to reconstruct the `header` file:

```bash
% (echo && echo -n "## ") > plugins/src/main/resources/gradle/header
```

## Mixin Plugins

The mixins are meant to provide snippets of Gradle configuration based on specific functional groupings.
The groups are identified as starter scripts, and roughly grouped:

* java - basic configuration around a typical build objective
* metrics - build timing configuration
* std - top-level configs based on target type

The `std` level of configuration is organized by artifact type.
This is the only level meant to organize or include other configs.
The design is meant to be easily overridden by teams that wish to depart from the standard configs.
Teams are able to just copy the `std` level conventions to their project and override those they wish to change.
Items they don't need to change can continue to be referred in their local configs.

The intention is that the `starter-boot` package can externalize these mixins as plugins so the team can continue to refer to those mixins that do not need to change.
To that end, do not include or build upon other convention files, except at the `std` level.

I think the only exception to this rule is `starter.java.style-conventions`, which includes `starter.java.checktyle-conventions`.
This was only allowed because `style-conventions` is aggregating `checkstyle` and `spotless`.
If 'spotless' gets more complicated, then these two should be split and propagated upwards instead of being aggregated under `style-conventions`.
