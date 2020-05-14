#!/usr/bin/env bash
echo "Using shell script to build triominos: $BASH_VERSION"

echo "--------------------------------------------------------------"
echo "  JAVA_HOME is currently set to: $JAVA_HOME"
echo "  JAVA_HOME should be set to JDK v1.8 or greater in your environment."
echo "  For example:"
echo "        export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home"
echo "--------------------------------------------------------------"

echo "Cleaning target area..."
mvn clean
rc=$?
if [[ "$rc" -ne 0 ]] ; then
    echo " Could not clean target area. Return code:$rc"
fi

echo "Analyzing dependencies..."
mvn -U dependency:analyze | tee dependency-analyze.log
rc=$?
if [[ "$rc" -ne 0 ]] ; then
    echo " Could not analyze dependencies: Return code:$rc"
else
    echo " Review dependency analysis in dependency-analyze.log."
    open ./dependency-analyze.log
fi

echo "Resolving all dependencies..."
mvn -U dependency:resolve | tee dependency-resolve.log
rc=$?
if [[ "$rc" -ne 0 ]] ; then
    echo " Could not resolve all dependencies: Return code:$rc"
else
    echo " Review dependency resolution information in dependency-resolve.log."
    open ./dependency-resolve.log
fi

echo "Constructing dependency tree..."
mvn -U dependency:tree | tee dependency-tree.log
rc=$?
if [[ "$rc" -ne 0 ]] ; then
    echo " Could not produce dependency tree data: Return code:$rc"
else
    echo " Review dependency tree data in dependency-tree.log."
    open ./dependency-tree.log
fi

echo "Producing upgrade data..."
mvn versions:dependency-updates-report
rc=$?
if [[ "$rc" -ne 0 ]] ; then
    echo " Could not produce upgrade data: Return code:$rc"
else
    echo " Review upgrade data in dependency-updates-report.html"
    open ./target/site/dependency-updates-report.html
fi

echo "Building triominos..."
echo "Checkstyle, pmd, cpd, spotbugs, and findbugs analysis..."
mvn -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd spotbugs:spotbugs findbugs:findbugs | tee build.log
rc=$?
if [[ "$rc" -ne 0 ]] ; then
    echo " Could not perform checkstyle: Return code:$rc"
    exit $rc
else
    echo " Review checkstyle results in ./target/site/checkstyle-result.html."
    open ./target/site/checkstyle.html
fi

echo "Building triominos documentation"
mvn -batch-mode -V -U -e javadoc:jar | tee -a build.log
rc=$?
if [[ "$rc" -ne 0 ]] ; then
    echo " Error building triominos documentation: Return code:$rc"
    echo "   Review JavaDoc Document APIs genereated in ./target/apidocs/index.html."
    open ./target/apidocs/index.html
else
    echo " Success building triominos documentation...moving on to testing."
    echo "   JavaDoc Document APIs available in ./target/apidocs/index.html."
    open ./target/apidocs/index.html
fi

echo "Testing..."
mvn -V verify | tee -a build.log
rc=$?
if [[ "$rc" -ne 0 ]] ; then
    echo " Could not perform tests: Return code:$rc"
else
    echo " Opening code coverage results."
    open ./target/site/jacoco-ut/index.html
fi

echo "Packaging..."
mvn -V package
rc=$?
if [[ "$rc" -ne 0 ]] ; then
    echo " Could not perform packaging: Return code:$rc"
else
    echo " Packaging of triominos complete."
fi
