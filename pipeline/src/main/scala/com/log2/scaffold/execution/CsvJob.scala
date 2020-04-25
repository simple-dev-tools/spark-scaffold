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

    /**
     * Let's use the paramString as new column name
     */
    val argColName = context.params.paramString

    /**
     * Let's use the paramInt as the number of rows to be show
     */
    val argNumberToShow = context.params.paramInt

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

    val requiredColNames = Seq("name", "age")


    /**
     * do Transformation, and chaining them together
     */
    val resultDF = csvDF
        .transform(addColumn(argColName, "value"))
        .transform(addColumn("another_col","great"))


    /**
     * do Action - we just show it
     */
    resultDF.show(argNumberToShow)

  }


}


