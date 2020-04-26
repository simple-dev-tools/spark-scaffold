package com.log2.scaffold.execution

import org.apache.spark.sql.SparkSession
import com.log2.scaffold.utils.Transform.addColumn

object CsvJob extends Main[CsvJobParam] {

  /**
   * You need to define the logic how to parse extra parameters
   * @param args
   * @return
   */
  override def buildParams(args: Array[String]): CsvJobParam= {
    new ArgsParser[CsvJobParam]("Example Csv Job") {
      opt[String]('c', "csv")
        .required()
        .action {
          (csv_path, p) => p.copy(csvFilePath= csv_path)
        }
        .validate(path =>
          if (path.endsWith("csv")) { success } else {
          failure("The csv path must ends with .csv and must exists")
        })
        .text("The path of an csv file")

      opt[Int]('l', "line")
        .required()
        .action {
          (line, p) => p.copy(showLines = line)
        }
        .validate( line => if (line >= 1) { success } else {
          failure(s"$line is not able to be used for show, it should >= 1")
        })
        .text("The path of an csv file")

    }.parse(args, CsvJobParam()).get
  }

  /**
   * You may need to update the SparkConf by adding / overwriting
   * @param params
   * @return
   */
  override def createContext(params: CsvJobParam) =
    new RunContextImpl[CsvJobParam](
      params,
      (builder: SparkSession.Builder, _: RunContext[CsvJobParam]) => {
        /**
         * Add extra config to the builder
         */
        builder.appName(s"${this.getClass.getSimpleName}-CSV-Appending-Job")

        /**
         * overwrite the config from super impl
         */
        builder.config("spark.sql.crossJoin.enabled","false")
      }
    )

  /**
   * The actual business of the Spark job, such as read data and transform
   * @param context
   */
  override def run(implicit context: RunContext[CsvJobParam]): Unit = {

    logger.info(s"The params is : ${context.params.toString()}")

    /**
     * you can access three items from the context
     * spark - the SparkSession
     * params - the parsed arguments
     * config - the application Config based on Env
     */
    val spark = context.spark
    val argCsvPath = context.params.csvFilePath
    val argDebug = context.params.debug

    /**
     * Use the parameter from base [[Params]] trait
     */
    if (argDebug) {
      logger.info("The debug model is ON")
    } else {
      logger.info("The debug model is OFF")
    }


    /**
     * Let's use the paramInt as the number of rows to be show
     */
    val argNumberToShow = context.params.showLines

    val hiveDbName = context.appConfig.getString("hive.dbname")

    /**
     * of course you can use the logger
     */
    logger.info(s"The hiveDbName = $hiveDbName")


    /**
     * actual job logic, read the csv file
     */
    val csvDF = spark.read.format("csv")
        .option("header","true")
        .option("inferSchema","true")
        .load(argCsvPath)


    /**
     * do Transformation, and chaining them together
     */
    val resultDF = csvDF
        .transform(addColumn("new_col", "value"))
        .transform(addColumn("another_col","great"))


    /**
     * do Action - we just show it
     */
    resultDF.show(argNumberToShow)

  }


}


