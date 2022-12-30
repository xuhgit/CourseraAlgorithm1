#!/bin/bash
#
#  Execution:  % spotbugs -cos226 HelloWorld.class
#  Execution:  % spotbugs -cos226 *.class
#
#  To see descriptions of bugs:
#  https://spotbugs.readthedocs.io/en/latest/bugDescriptions.html

SPOTBUGS_VERSION="4.2.3"
LIFT_INSTALL="D:\princeton_algo\code\.lift"
AUX_CLASSPATH="${LIFT_INSTALL}/lib/algs4.jar:${LIFT_INSTALL}/lib/stdlib.jar:${LIFT_LIB}/lib/introcs.jar"
SPOTBUGS="spotbugs-${SPOTBUGS_VERSION}"
SPOTBUGS_JAR="${LIFT_INSTALL}/${SPOTBUGS}/lib/spotbugs.jar"
SPOTBUGS_XML="${LIFT_INSTALL}/spotbugs.xml"
JAVA="java"


usage() {
    echo "Usage: ${0##*/} [-cos126 | -cos226 | -coursera] [filename1.class filename2.class | jar | directory]"
    exit 1
}

# no command-line arguments
if [ ! -n "$1" ]; then
    usage
    exit 1
fi

# ignore -cos126 arguments (since currently only one version of spotbugs.xml)
if [ "$1" == "-cos126" ]; then
        shift
elif [ "$1" = "-cos226" ]; then
        shift
elif [ "$1" = "-introcs" ]; then
        shift
elif [ "$1" = "-algs4" ]; then
        shift
elif [ "$1" = "-coursera" ]; then
        shift
fi

# no command-line arguments (except possibly course-specific one)
if [ ! -n "$1" ]; then
    usage
    exit 1
fi

# if the first remaining argument is either a .class or .java file that exists, runs spotbugs
if [ "${1##*.}" = "class" ] || [ "${1##*.}" = "jar" ]; then
    if [ -e "$1" ]; then
        echo "Running spotbugs on $*:"
        $JAVA -Duser.language=novice -jar "${SPOTBUGS_JAR}" -textui -low -auxclasspath "${AUX_CLASSPATH}" -longBugCodes -exclude "${SPOTBUGS_XML}" "$@"
    else
        echo "File not found! Make sure you are specifying the path correctly."
        echo "The filename is case sensitive."
    fi
    exit
fi

echo "Spotbugs needs .class or .jar files as arguments!"
echo "The filename is case sensitive."

exit
