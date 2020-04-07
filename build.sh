export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home
mvn -batch-mode -V -U -e checkstyle:checkstyle findbugs:findbugs
