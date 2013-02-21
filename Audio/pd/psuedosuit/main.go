// We will read input from stdin
// Format: 
// <suit input>
// <delay in ms>
package main

import (
	"bufio"
	"flag"
	"fmt"
	"github.com/tarm/goserial"
	"log"
	"net"
	"os"
	"time"
)

var (
	portName   = flag.String("p", "/dev/tty.usbserial-A4013GAY", "xbee com port")
	recordFile = flag.String("r", "", "enter record mode with outputfile")
)

func main() {

	flag.Parse()

	if *recordFile != "" {

		fmt.Println("recording to: ", *recordFile)

		outputFile, err := os.Create(*recordFile)
		if err != nil {
			log.Fatal(err)
		}
		defer outputFile.Close()

		// record mode

		xbeeCFG := new(serial.Config)
		xbeeCFG.Name = portName
		xbeeCFG.Baud = 9600

		xbeeFile, err := serial.OpenPort(xbeeCFG)
		fmt.Println("port opened")
		if err != nil {
			log.Fatal(err)
		}

		xbee := bufio.NewReader(xbeeFile)

		var lastTime time.Time

		for err == nil {
			line, err := xbee.ReadString('\n')
			if err != nil {
				log.Println(err)
				break
			}

			newTime := time.Now()
			if lastTime != nil {
				delay := newTime.Sub(lastTime)
				fmt.Fprintln(outputFile, delay.String())
			}
			lastTime = newTime

			fmt.Fprintf(outputFile, "%s;\n", line)
		}

	} else {
		// write mode

		fmt.Println("write mode")
		// Dial our pd application
		conn, err := net.Dial("tcp", ":8080")
		if err != nil {
			log.Fatal(err)
		}

		input := bufio.NewReader(os.Stdin)
		for err == nil {
			command, err := input.ReadString('\n')
			if err != nil {
				continue
			}

			delayString, err := input.ReadString('\n')
			if err != nil {
				continue
			}

			fmt.Fprintf(con, "%s;", line)
			delay, err := time.ParseDuration(delayString)
			time.Sleep(delay)
		}
	}
}
