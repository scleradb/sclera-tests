/**
* Sclera - Tests
* Copyright 2012 - 2020 Sclera, Inc.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.scleradb.test

import java.io.{File, PrintWriter}

import org.scalatest.CancelAfterFailure
import org.scalatest.funspec.AnyFunSpec

import com.scleradb.sql.statements.{SqlStatement, SqlRelQueryStatement}
import com.scleradb.sql.types.SqlCharVarying
import com.scleradb.sql.datatypes.Column
import com.scleradb.sql.result.TableRow

import com.scleradb.exec.Processor

class CSVTestSuite extends AnyFunSpec with CancelAfterFailure {
    var processor: Processor = null

    val csvFile: File = File.createTempFile("test", "csv")
    val csvFileName: String = csvFile.getAbsolutePath()

    describe("CSV Query Processing") {
        it("should setup") {
            val csv: PrintWriter = new PrintWriter(csvFile)
            csv.write(
                """|FIELD1, field2, FIELD3
                   |1, 2, foo
                   |3, 4, foo bar
                   |""".stripMargin
            )
            csv.close()

            processor = Processor()
            processor.init()
        }

        it("should execute a CSV query") {
            val query: String =
                "select * from external csv ('" + csvFileName + "')"

            val parseResult: List[SqlStatement] =
                processor.parser.parseSqlStatements(query)
            assert(parseResult.size === 1)
            assert(parseResult.head.isInstanceOf[SqlRelQueryStatement] === true)

            val qstmt: SqlRelQueryStatement =
                parseResult.head.asInstanceOf[SqlRelQueryStatement]
            
            processor.handleQueryStatement(qstmt, { ts =>
                val columns: List[Column] = ts.columns
                assert(columns.size === 3)

                assert(columns(0) === Column("FIELD1", SqlCharVarying(None)))
                assert(columns(1) === Column("FIELD2", SqlCharVarying(None)))
                assert(columns(2) === Column("FIELD3", SqlCharVarying(None)))

                val rows: List[TableRow] = ts.rows.toList
                assert(rows.size === 2)

                assert(rows(0).getStringOpt("FIELD1") === Some("1"))
                assert(rows(0).getStringOpt("FIELD2") === Some("2"))
                assert(rows(0).getStringOpt("FIELD3") === Some("foo"))

                assert(rows(1).getStringOpt("FIELD1") === Some("3"))
                assert(rows(1).getStringOpt("FIELD2") === Some("4"))
                assert(rows(1).getStringOpt("FIELD3") === Some("foo bar"))
            })
        }

        it("should teardown") {
            csvFile.delete()
            processor.close()
        }
    }
}
