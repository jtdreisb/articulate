package main

import (
	"bufio"
	"fmt"
	"github.com/tarm/goserial"
	"log"
	"net"
)

func main() {

	xbeeCFG := new(serial.Config)
	xbeeCFG.Name = "/dev/tty.usbserial-A4013GAY"
	xbeeCFG.Baud = 9600

	fmt.Println("open port")
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
