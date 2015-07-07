package main

import (
	"fmt"
	"log"
	"os"
	"bufio"

	"github.com/codegangsta/cli"
)


func main() {
	app := cli.NewApp()
	app.Name = "ops"
	app.Usage = "IT-PACKAGE(ops tools)"
	app.Version = "v20150706"
	app.Commands = []cli.Command{
		{
			Name:    "generate",
			Aliases: []string{"g"},
			Usage:   "Generate new task",
			Flags: []cli.Flag{
				cli.StringFlag{
					Name:  "name, n",
					Usage: "task name",
				},
			},
			Action: func(c *cli.Context) {
				name := c.String("name")
				if name == ""{
					log.Fatalf("Need name")
				}

				script := fmt.Sprintf("scripts/%s.sh", name)
				if _, err := os.Stat(script); err == nil {
					log.Fatalf("Task %s already exists!", name)
				}
				log.Printf(fmt.Sprintf("Generate task %s", name))

				fd, err := os.Create(script)
				if err!=nil{
					log.Fatalf("Create file %s failed",)
				}

			},
		},
		{
			Name:    "run",
			Aliases: []string{"r"},
			Usage:   "Run task by name",
			Flags: []cli.Flag{
				cli.StringFlag{
					Name:  "environment, e",
					Value: "development",
					Usage: "can be production, development, stage, etc...",
				},
				cli.StringFlag{
					Name:  "task, t",
					Value: "info",
					Usage: "task name",
				},
			},
			Action: func(c *cli.Context) {
				log.Printf("tasks")
			},
		},
		{
			Name:    "list",
			Aliases: []string{"l"},
			Usage:   "Display the tasks with descriptions",
			Action: func(c *cli.Context) {
				log.Printf("tasks")
			},
		},
	}

	app.Run(os.Args)
}
