#JSR223 Profile Activator Maven Extension

## About

According to the documentation at [Sonatype](http://www.sonatype.com/books/mvnref-book/reference/profiles-sect-activation.html) a profile is activated when all trigger conditions are met.

But conditions are very simple: scripts are not supported, therefore it is not possible to write complex conditions.

This extension allows you to use the JSR223 support in Java6 and following for writing scripts to determine whether to activate or not a profile.

## Implementation details
Instead of changing the schema of Project Object Model (or POM) this extension works as property condition activation, delegating to the original ```PropertyProfileActivator``` when it does not apply the following syntax for JSR223:

* The name of the property must starts with '=' (equals char), and, optionally, followed by the short name of the ScriptEngine, mime type or file extension (i.e. JavaScript).
* The default short name of the ScriptEngine is "JavaScript", because it comes with the JDK.
* If you want to use another language supported by JSR223 you must to add the libraries to the Maven classpath.

## How this extension works
The ```value``` tag of the property profile activator is the script to execute. Last sentence in script will return a boolean value, ```true``` to activate the profile, ```false``` otherwise.

## Which properties are visible
The following set of properties are visible (project properties and user properties are experimental):
* System properties.
* Project properties
* User properties.

## Resources available in script
### The expression manager
The expression manager is a simplified vision of the Maven interpolator. The expression manager evaluates a simple Maven expression, returning an ```InterpolatedExpression```.

You can use the expression manager as an object in the script language; its default name is ```aitor```.

### The interpolated expression
The interpolated expression is the object returned by the expression manager. It encapsulates the interpolated value, and it has some methods to get the interpolated value or a default value, String, boolean or int, directly.

## Install the extension
You need to copy the following file to the folder ```${MAVEN_HOME}/lib/ext```:
* jsr223-profile-activator-extension-${version}.jar

## Examples
### About the examples
Please, keep in mind that the following examples was written considering the simplicity of the same, not in its correct syntax, so the XML presented here is not well formed.
For example, instead of the ```&&``` you must write ```&amp;&amp;```

### A simple profile in Maven
```xml
<profiles>
    <profile>
        <id>MY_SITE_SKIP_ACTIVATED</id>
        <property>
            <name>MY_SITE_SKIP</name>
            <value>true</value>
        </property>
```
### The above example written with jsr223-profile-activator-extension
```xml
<profiles>
    <profile>
        <id>MY_SITE_SKIP_ACTIVATED</id>
        <property>
            <name>=</name>
            <value>
                aitor.eval('${MY_SITE_SKIP}').getBoolean(false);
            </value>
        </property>
```

### Of course, it is a script...
```xml
<profiles>
    <profile>
        <id>MY_SITE_SKIP_ACTIVATED</id>
        <property>
            <name>=</name>
            <value>
                var mySiteSkip = aitor.eval('${MY_SITE_SKIP}').getBoolean(false);
                var reportsEnabled = aitor.eval('${REPORTS_ENABLED}').getBoolean(false);
                var reportsLevel = aitor.eval('${REPORTS_LEVEL}').getInt(0);
                var siteUrl = aitor.eval('${SITE_URL}').getString('');
                var siteDefined = ( siteUrl != '' );
                mySiteSkip &amp;&amp; reportsEnabled &amp;&amp; ( reportsLevel > 0 ) &amp;&amp; siteDefined
            </value>
        </property>
```
