File buildLog = new File(basedir, 'build.log');
// This test should fail if $M2_HOME/lib/ext contains jsr223-profile-activator-extension.jar
// (which would invalidate the other tests).
assert ! buildLog.text.contains('[INFO] JSR223ProfileActivator core extension is ready');
