package main

import (
	"bufio"
	"flag"
	"fmt"
	"github.com/tarm/goserial"
	"log"
	"net"
)

var PortName = "/dev/tty.usbserial-A4013GAY"

func main() {

	flag.Parse()

	if len(flag.Args()) > 0 {
		PortName = flag.Args()[0]
	}

	xbeeCFG := new(serial.Config)
	xbeeCFG.Name = PortName
	xbeeCFG.Baud = 9600

	xbeeFile, err := serial.OpenPort(xbeeCFG)
	fmt.Println("port opened")
	if err != nil {
		log.Fatal(err)
	}

	xbee := bufio.NewReader(xbeeFile)

	// Dial our pd application
	conn, err := net.Dial("tcp", ":8080")
	if err != nil {
		log.Fatal(err)
	}

	for err == nil {
		line, err := xbee.ReadString('\n')
		if err != nil {
			log.Println(err)
			break
		}
		log.Println("sending: ", line)
		fmt.Fprintf(conn, "%s;", line)
	}

	conn.Close()
}
