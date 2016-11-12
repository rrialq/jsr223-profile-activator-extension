File buildLog = new File(basedir, 'build.log');
assert buildLog.text.contains('[INFO] JSR223ProfileActivator core extension is ready');
assert buildLog.text.find('(?m)The following profiles are active:\\s+- testprofile');
