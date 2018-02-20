# Custom PMD Rules
This project contains some custom PMD rules which are not included in the official PMD release.
For a list of these custom rules, we refer to the corresponding [Java package](src/main/java/de/chw).

## IDE Integration
We recommend to use the QA Eclipse Plugin which supports, amongst others, PMD with custom rules without using an Eclipse fragment plugin:
https://github.com/ChristianWulf/qa-eclipse-plugin

If you insist on using the PMD Eclipse Plugin (from https://github.com/pmd/pmd-eclipse-plugin), 
we refer to http://www.eclipsezone.com/articles/pmd for details about how to integrate custom PMD rules.

## Download
Via Maven:

```
<dependency>
    <groupId>net.sourceforge.teetime</groupId>
    <artifactId>pmd.ruleset</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Via direct download:

https://oss.sonatype.org/content/repositories/snapshots/net/sourceforge/teetime/pmd.ruleset
