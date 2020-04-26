# Spark Scaffold

![Spark Scaffold CI](https://github.com/zhongdai/spark-scaffold/workflows/Spark%20Scaffold%20CI/badge.svg?branch=master)

A framework to develop production grade Spark jobs.

## The Idea

The key concept of this framework is the `Run Context`, which made of three major components,

1. The Spark session - which you have to use it for Transformation and Action
2. The Parameters (parsed arguments from command input) - which from the `spark-submit` command
3. The config - which should have different config for different environments.

This framework helps you to manage those three components and enable you focus on the actual business logic - DataFrame
transformation. 

## Quick Start

To build and run test cases, just simply run,

```bash
sbt test
```

To package the Jar run,
```bash
make build-spark-jar
```

> The `sbt-assembly` plugin to build a fat Jar with dependencies

To run the example Spark jobs as local mode, just run,
```bash
make submit-job
```

## Use it in your project


