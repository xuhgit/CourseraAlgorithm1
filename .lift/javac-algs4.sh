#!/bin/bash

# this must match the install directory
LIFT_LIB="/d/princeton_algo/code/.lift"

# set classpath to include the textbook libraries
CLASSPATH=".:${LIFT_LIB}/algs4.jar"

# compile on Windows/Linux using "javac" but on OS X using "java_home -exec javac"
JAVAC="javac"
if [ -f /usr/libexec/java_home ]; then
    JAVAC="/usr/libexec/java_home -exec javac"
fi

# abort if javac command does not exist
if ! [ "$(command -v $JAVAC)" ]; then
    echo 'Aborting: javac command not found'
    exit 1
fi

# get javac version
JAVAC_VERSION=$($JAVAC -version 2>&1)

# execute javac command with various commmand-line options
# --release option not introduced until Java 9

# Java 8
if [[ "$JAVAC_VERSION" == "javac 1.8"* ]]; then
    $JAVAC -cp "$CLASSPATH"   \
           -g                 \
           -encoding UTF-8    \
           -Xlint:all         \
           -Xlint:-overrides  \
           -Xlint:-serial     \
           -Xdiags:verbose    \
           -Xmaxwarns 10      \
           -Xmaxerrs 10       \
           "$@"

# Java 7, 6, 5, ...
elif [[ "$JAVAC_VERSION" == "javac 1."* ]]; then
    echo "Aborting: requires Java 8 or above (javac reports version ${JAVAC_VERSION#javac })"
    exit 1

# Java 9, 10, 11, ...
else
    $JAVAC -cp "$CLASSPATH"   \
           -g                 \
           -encoding UTF-8    \
           --release 8        \
           -Xlint:all         \
           -Xlint:-overrides  \
           -Xlint:-serial     \
           -Xdiags:verbose    \
           -Xmaxwarns 10      \
           -Xmaxerrs 10       \
           "$@"
fi
