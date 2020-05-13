#!/usr/bin/env bash
echo "Using shell script to build triominos: $BASH_VERSION"

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
    open ./dependency-updates.log
fi

echo "Producing upgrade data..."
mvn versions:dependency-updates-report | tee dependency-updates.log
rc=$?
if [[ "$rc" -ne 0 ]] ; then
    echo " Could not produce upgrade data: Return code:$rc"
else
    echo " Review upgrade data in dependency-updates.log."
    open ./target/site/dependency-updates-report.html
fi

# echo "Checkstyle, pmd, cpd, spotbugs, and findbugs analysis..."
# mvn -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd spotbugs:spotbugs findbugs:findbugs
# rc=$?
# if [[ "$rc" -ne 0 ]] ; then
#     echo " Could not perform checkstyle: Return code:$rc"
#     exit $rc
# else
#     echo " Review checkstyle results in ./target/site/checkstyle-result.html."
#     open ./target/site/checkstyle.html
# fi

echo "Building triominos..."
mvn -batch-mode -V -U -e checkstyle:checkstyle findbugs:findbugs | tee build.log
rc=$?
if [[ "$rc" -ne 0 ]] ; then
    echo " Error building triominos: Return code:$rc"
else
    echo " Success building triominos...moving on to testing."
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