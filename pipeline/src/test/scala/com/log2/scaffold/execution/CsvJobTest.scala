package com.log2.scaffold.execution

import com.log2.scaffold.test.ContextSuite
import org.scalatest.{FlatSpec, Matchers}
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.NoSuchElementException

import com.log2.scaffold.constants.Values.DefaultDateFormat

class CsvJobTest extends FlatSpec with Matchers with ContextSuite {

  val exampleDtStr = "2020-02-02"
  val exampleDt = new Date(new SimpleDateFormat(DefaultDateFormat).parse(exampleDtStr).getTime())

  val csvPath = "data/example.csv"

  it should "build correct params" in {
    val params = CsvJob.buildParams(Array(
      "--env=local",
      "--debug=true",
      s"--p_dt=${exampleDtStr}",
      "--p_int=3",
      "--p_string=abc",
      s"--csv=${csvPath}"
    ))

    params shouldBe
      CsvJobParam(
        env = "local",
        debug = true,
        paramDate = exampleDt,
        paramInt = 3,
        paramString = "abc",
        csvFilePath = csvPath
      )
  }

  it should "throw exception because of the wrong Date format" in {
    intercept[NoSuchElementException] {
      CsvJob.buildParams(Array(
        "--env=local",
        "--debug=true",
        s"--p_dt=20200202",
        "--p_int=3",
        "--p_string=abc",
        s"--csv=${csvPath}"
      ))
    }
  }

  it should "throw exception because of --csv is not ending with csv" in {
    intercept[NoSuchElementException] {
      CsvJob.buildParams(Array(
        "--env=local",
        "--debug=true",
        s"--p_dt=20200202",
        "--p_int=3",
        "--p_string=abc",
        s"--csv=some/path/text.txt"
      ))
    }
  }

}
