#!/bin/bash
if [ "$1" == "" ]
then
    echo "Usage:"
    echo "    ./runScript.sh package.module.classname arg1 arg2 ..."
    echo ""
    echo "**Notice**"
    echo "    just support 8(fa) arguments at most."
    exit
fi
libs=`jws classpath | grep '\[.*\]' | gsed "s/\[\|\]\|'//g" | gsed "s/, /:/g"`

cd ../
for lib in `ls lib/*.jar`
do
    libs+=":$lib"
done
for module in `ls modules/`
do
    for lib in `ls modules/${module}/lib/*.jar`
    do
        libs+=":$lib"
    done
done
JAVA_TOOL_OPTIONS='-Dfile.encoding=UTF8' java -Xmx512m -cp `pwd`/precompiled/java:${libs} -Djws.id=script -Dprecompiled=true -Dapplication.path=`pwd` common.script.ScriptRunner $1 $2 $3 $4 $5 $6 $7 $8
