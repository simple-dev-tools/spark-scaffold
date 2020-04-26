package com.log2.scaffold.execution

import com.log2.scaffold.test.ContextSuite
import org.scalatest.{FlatSpec, Matchers}
import java.util.NoSuchElementException


class CsvJobTest extends FlatSpec with Matchers with ContextSuite {

  val csvPath = "data/example.csv"

  it should "build correct params" in {
    val params = CsvJob.buildParams(Array(
      "--env=local",
      "--debug=true",
      s"--csv=${csvPath}",
      "-l=3"
    ))

    params shouldBe
      CsvJobParam(
        env = "local",
        debug = true,
        csvFilePath = csvPath,
        showLines = 3
      )
  }

  it should "throw exception because of lines < 1" in {
    intercept[NoSuchElementException] {
      CsvJob.buildParams(Array(
        "--env=local",
        "--debug=true",
        s"--csv=${csvPath}",
        "-l=0"
      ))
    }
  }

  it should "throw exception because of --csv is not ending with csv" in {
    intercept[NoSuchElementException] {
      CsvJob.buildParams(Array(
        "--env=local",
        "--debug=true",
        s"--csv=some/path/text.txt",
        "-l=3"
      ))
    }
  }

}
