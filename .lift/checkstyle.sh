#!/bin/bash
#
#  Execution:  % checkstyle -cos226 HelloWorld.java
#  Execution:  % checkstyle -cos226 *.java

usage() {
    echo "Usage: ${0##*/} [-cos126 | -cos226 | -algs4 | -introcs | -coursera] filename1 filename2 ..."
    exit 1
}

# This must match the install directory
LIFT_INSTALL="/usr/local/lift"

# execute on Windows/Linux using "java" but on OS X using "java_home -exec java"
JAVA="java"
if [ -f /usr/libexec/java_home ]; then
    JAVA="/usr/libexec/java_home -exec java"
fi

# Find most recent version of Checkstyle
CHECKSTYLE_VERSION="8.31"
CHECKSTYLE="checkstyle-${CHECKSTYLE_VERSION}"
CHECKSTYLE_JAR="${LIFT_INSTALL}/${CHECKSTYLE}/${CHECKSTYLE}-all.jar"
CHECKSTYLE_LIFT_JAR="${LIFT_INSTALL}/${CHECKSTYLE}/checkstyle-lift.jar"

# xml files that specify which Checkstyle checks to run or suppress
CHECKSTYLE_XML="${LIFT_INSTALL}/checkstyle-cos226.xml"
SUPPRESSIONS_XML="${LIFT_INSTALL}/checkstyle-suppressions.xml"

# set base directory to current working directory
BASEDIR=$(pwd -P)

# If there are no arguments
if [ ! -n "$1" ]; then
    echo 'Specify .java files as arguments.'
        usage
fi

if [ "$1" == "-cos126" ]; then
        echo 'Using Checkstyle checks for Princeton COS 126'
        CHECKSTYLE_XML="${LIFT_INSTALL}/checkstyle-cos126.xml"
        shift
elif [ "$1" = "-cos226" ]; then
        echo 'Using Checkstyle checks for Princeton COS 226'
        CHECKSTYLE_XML="${LIFT_INSTALL}/checkstyle-cos226.xml"
        shift
elif [ "$1" = "-introcs" ]; then
        echo 'Using Checkstyle checks for code in introcs.jar'
        echo 'Detected introcs'
        CHECKSTYLE_XML="${LIFT_INSTALL}/checkstyle-introcs.xml"
        shift
elif [ "$1" = "-algs4" ]; then
        echo 'Using Checkstyle checks for code in algs4.jar'
        CHECKSTYLE_XML="${LIFT_INSTALL}/checkstyle-algs4.xml"
        shift
elif [ "$1" = "-coursera" ]; then
        echo 'Using Checkstyle checks for Coursera courses Algorithms, Part I and II'
        CHECKSTYLE_XML="${LIFT_INSTALL}/checkstyle-coursera.xml"
        shift
else
        echo 'Using default Checkstyle checks from Sun'
        echo "Running checkstyle on $*:"
        $JAVA -jar "${CHECKSTYLE_JAR}" \
              -c /sun_checks.xml       \
              "$@" | grep -v '^Starting audit\.\.\.$' | grep -v '^Audit done\.$'
        exit
fi

echo $CHECKSTYLE_XML

# If the first remaining argument is .java file that exists, runs Checkstyle
if [ "${1##*.}" = "java" ]; then
    if [ -e "$1" ]; then
        echo "Running checkstyle on $*:"
        output="$($JAVA -cp "${CHECKSTYLE_JAR}:${CHECKSTYLE_LIFT_JAR}" \
                        -Dsuppressions="${SUPPRESSIONS_XML}"           \
                        -Dbasedir="${BASEDIR}"                         \
                        com.puppycrawl.tools.checkstyle.Main           \
                        -c "${CHECKSTYLE_XML}"                         \
                       "$@" )"
        rc=$?
        echo "$output" | grep -v '^Starting audit\.\.\.$' | grep -v '^Audit done\.$'
        exit $rc
    else
        echo "File not found! Make sure you are specifying the path correctly."
        echo "The filename is case sensitive."
        usage
    fi
else
    echo "Checkstyle needs a .java file as an argument!"
    echo "The filename is case sensitive."
    usage
fi
