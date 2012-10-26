#!/bin/sh

if [ ! -f "/Library/Java/Extentions/RXTXcomm.jar"]; then

	echo "sudo ditto references/RxTx/RXTXcomm.jar /Library/Java/Extensions/"
	echo "sudo ditto references/RxTx/Mac_OS_X/librxtxSerial.jnilib /Library/Java/Extensions/"
fi

java -jar -d32 Articusoft.jar Main
