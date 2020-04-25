PUBLISHED_VERSION := $(shell git rev-parse --short HEAD)
# you need to give the full path of your local spark-submit command
SPARK_SUBMIT := spark-submit
ENV ?= local
JAR := package/spark-scaffold-pipeline-0.0.1-$(PUBLISHED_VERSION).jar
MAIN_CLASS := com.log2.scaffold.execution.CsvJob
CSV_PATH := data/example.csv


.PHONY: help
help:
	@cat $(MAKEFILE_LIST) | grep -e "^[a-zA-Z_\-]*: *.*## *" | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'


build-spark-jar: ## fat jar for cluster submission without provided dependencies
	sbt 'pipeline/assembly'

submit-job: build-spark-jar ## submit the example job to local
	$(SPARK_SUBMIT) --master local --class $(MAIN_CLASS) $(JAR)  -e local -d false --p_dt 2020-03-09 --p_int 3 --p_string col3 --csv $(CSV_PATH)

clean: ## remove metastore_db and spark-warehouse folders
	find . -path '*/metastore_db/*' -delete
	find . -path '*/spark-warehouse/*' -delete
	find . -type d -name "metastore_db" -delete
	find . -type d -name "spark-warehouse" -delete
