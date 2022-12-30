#!/bin/bash
#
# Execution:  % pmd -cos126 HelloWorld.java
# Execution:  % pmd -cos226 /Users/wayne/cos226/percolation/

# [wayne s18] should we allow pmd to just defer commands to "run.sh pmd" if no -cos126 options?
# [wayne s18] what to do if argument is not a single .java file, directory, or jar?

usage() {
    echo "Usage: ${0##*/} [-cos126 | -cos226 | -coursera] [filename.java | jar | directory]"
}

LIFT_INSTALL="D:\princeton_algo\code\.lift"
PMD_VERSION="6.34.0"
PMD_XML="${LIFT_INSTALL}/pmd-${PMD_VERSION}.xml"
PMD="pmd-bin-${PMD_VERSION}"
PMD_EXECUTE="${LIFT_INSTALL}/${PMD}/bin/run.sh"

# no command-line arguments
if [ ! -n "$1" ]; then
    usage
    exit 1
fi

# ignore -cos126 arguments (since currently only one version of pmd.xml)
if [ "$1" == "-cos126" ]; then
        shift
elif [ "$1" = "-cos226" ]; then
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

# execution command
EXECUTE="$PMD_EXECUTE pmd -format csv -no-cache -shortnames -rulesets $PMD_XML -version 1.8 -language java"

echo "Running pmd on $*:"

$EXECUTE -dir "$@" \
    | grep -v "Rule set" \
    | awk  'BEGIN {
                FS = ",\""; warnings = 0
            }
            {
                if ($0 == "Mandatory arguments:") exit;
                if (length($8) == 0) print $0;
                else                 { warnings++; print $3":"$5": "$6" ["$8"]" }
            }
            END {
                if      (warnings == 1) print "PMD ends with " warnings " warning."
                else if (warnings >  1) print "PMD ends with " warnings " warnings."
            }' \
    | sed 's/"//g'
